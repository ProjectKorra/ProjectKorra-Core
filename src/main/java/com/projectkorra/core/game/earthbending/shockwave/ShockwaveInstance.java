package com.projectkorra.core.game.earthbending.shockwave;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.LivingEntity;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;

import com.projectkorra.core.ProjectKorra;
import com.projectkorra.core.ability.AbilityUser;
import com.projectkorra.core.ability.attribute.Attribute;
import com.projectkorra.core.game.BendingBlocks;
import com.projectkorra.core.game.earthbending.EarthAbilityInstance;
import com.projectkorra.core.skill.Skill;
import com.projectkorra.core.temporary.TempBlock;
import com.projectkorra.core.temporary.TempBlock.TempData;
import com.projectkorra.core.util.Blocks;
import com.projectkorra.core.util.Effects;
import com.projectkorra.core.util.Vectors;
import com.projectkorra.core.util.Velocity;
import com.projectkorra.core.util.math.UnitVector;

public class ShockwaveInstance extends EarthAbilityInstance {

	@Attribute("width")
	private double angle;
	@Attribute(RANGE)
	private double range;
	@Attribute(SPEED)
	private double speed;
	@Attribute(COOLDOWN)
	private long cooldown;
	@Attribute(DAMAGE)
	private double damage;

	private Location[] locs = new Location[3];
	private boolean[] blocked = new boolean[51];
	private double maxRange;
	private Predicate<Block> bendable;
	private Location start;
	private Set<Block> blocks = new HashSet<>(), usedBlocks = new HashSet<>();
	private Set<Entity> affected = new HashSet<>();

	public ShockwaveInstance(Shockwave provider, AbilityUser user) {
		super(provider, user);
		this.angle = provider.angle;
		this.range = provider.range;
		this.speed = provider.speed;
		this.cooldown = provider.cooldown;
		this.damage = provider.damage;
	}

	@Override
	protected boolean onStart() {
		start = user.getLocation();
		for (int i = 0; i < 3; ++i) {
			locs[i] = user.getLocation().add(user.getDirection().setY(0));
			locs[i].setPitch(0);
			locs[i].setYaw(locs[i].getYaw() + (float) (((double) (i - 1)) * angle / 2));
		}

		Arrays.fill(blocked, false);
		this.maxRange = this.range;
		this.bendable = (b) -> BendingBlocks.isEarthbendable(b);
		if (user.hasSkill(Skill.LAVABENDING)) {
			bendable = bendable.or((b) -> BendingBlocks.isLavabendable(b));
		}

		user.addCooldown(provider, cooldown);
		return true;
	}

	@Override
	protected boolean onUpdate(double timeDelta) {
		if ((range -= speed * timeDelta) <= 0) {
			return false;
		}

		for (Location loc : locs) {
			loc.add(loc.getDirection().multiply(speed * timeDelta));
		}

		double increment = Math.min(1.0 / blocked.length, 0.25 / ((maxRange - range) * angle));
		for (double t = 0; t <= 1.0; t += increment) {
			if (blocked[(int) Math.round(t * (blocked.length - 1))]) {
				continue;
			}

			Block block = Blocks.findTop(bezier(t), 2, (b) -> b.isPassable() && b.getType() != Material.WATER);

			if (!bendable.test(block)) {
				blocked[(int) Math.round(t * (blocked.length - 1))] = true;
			} else if (!usedBlocks.contains(block.getRelative(BlockFace.UP))) {
				blocks.add(block);
				usedBlocks.add(block);
			}
		}

		for (Block block : blocks) {
			BlockData data = block.getBlockData();
			if (data.getMaterial() == Material.LAVA) {
				data = Material.MAGMA_BLOCK.createBlockData();
			}

			TempData temp = TempBlock.from(block).setData(Material.AIR.createBlockData());
			FallingBlock fb = block.getWorld().spawnFallingBlock(block.getLocation().add(0.5, 0.7, 0.5), data);
			fb.setDropItem(false);
			fb.setMetadata("shockwave", new FixedMetadataValue(JavaPlugin.getPlugin(ProjectKorra.class), temp));
			Velocity.move(fb, UnitVector.POSITIVE_Y.scaled(Math.random() * speed / 4 * timeDelta), this, true);
			Effects.forNearbyEntities(block.getLocation().add(0.5, 1, 0.5), 0.6, (e) -> e instanceof LivingEntity && !affected.contains(e) && !e.getUniqueId().equals(user.getUniqueID()), (e) -> {
				Effects.damage((LivingEntity) e, damage, ShockwaveInstance.this, false);
				Velocity.knockback(e, Vectors.direction(start, block.getLocation().add(0.5, 0, 0.5)).setY(0.8).multiply(speed / 4 * timeDelta), ShockwaveInstance.this);
				affected.add(e);
			});
		}

		blocks.clear();

		return true;
	}

	@Override
	public int interpolationInterval() {
		return 1;
	}

	@Override
	protected void postUpdate() {

	}

	@Override
	protected void onStop() {

	}

	@Override
	public String getName() {
		return provider.getName();
	}

	private Location bezier(double t) {
		return locs[1].clone().add(locs[0].clone().subtract(locs[1]).multiply((1 - t) * (1 - t))).add(locs[2].clone().subtract(locs[1]).multiply(t * t));
	}

	@Override
	protected void preUpdate() {
		// TODO Auto-generated method stub

	}
}
