package com.projectkorra.core.game.earthbending.earthbolt;

import java.util.function.Predicate;

import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.LivingEntity;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import com.projectkorra.core.ProjectKorra;
import com.projectkorra.core.ability.AbilityManager;
import com.projectkorra.core.ability.AbilityUser;
import com.projectkorra.core.ability.attribute.Attribute;
import com.projectkorra.core.ability.attribute.AttributeGroup;
import com.projectkorra.core.collision.Collidable;
import com.projectkorra.core.game.BendingBlocks;
import com.projectkorra.core.game.earthbending.EarthAbilityInstance;
import com.projectkorra.core.physics.Collider;
import com.projectkorra.core.skill.Skill;
import com.projectkorra.core.temporary.TempBlock;
import com.projectkorra.core.util.Blocks;
import com.projectkorra.core.util.Effects;
import com.projectkorra.core.util.Particles;
import com.projectkorra.core.util.Vectors;
import com.projectkorra.core.util.Velocity;
import com.projectkorra.core.util.data.Holder;
import com.projectkorra.core.util.math.UnitVector;

public class EarthboltInstance extends EarthAbilityInstance implements Collidable {

	@Attribute(value = Attribute.DAMAGE, group = AttributeGroup.DAMAGE)
	private double damage;
	@Attribute(value = Attribute.SELECT_RANGE, group = AttributeGroup.SELECT_RADIUS)
	private double selectRange;
	@Attribute(value = "launch_speed", group = AttributeGroup.SPEED)
	private double speed;
	@Attribute(value = Attribute.COOLDOWN, group = AttributeGroup.COOLDOWN)
	private long cooldown;
	@Attribute(value = Attribute.STAMINA_COST, group = AttributeGroup.STAMINA)
	private double staminaCost;

	private boolean shot = false, launched = false;
	private FallingBlock block;
	private Collider collider;

	public EarthboltInstance(Earthbolt provider, AbilityUser user) {
		super(provider, user);
		this.damage = provider.damage;
		this.selectRange = provider.selectRange;
		this.speed = provider.launchSpeed;
		this.cooldown = provider.cooldown;
		this.staminaCost = provider.staminaCost;
	}

	@Override
	protected void onStart() {
		Predicate<Block> bendable = (b) -> BendingBlocks.isEarthbendable(b);
		if (user.hasSkill(Skill.LAVABENDING)) {
			bendable = bendable.or((b) -> BendingBlocks.isLavabendable(b));
		}

		Block source = Blocks.targeted(user.getEyeLocation(), selectRange, bendable.negate().and((b) -> b.isPassable()));
		if (source.isEmpty()) {
			source = Blocks.findTop(user.getLocation().add(Vectors.direction(user.getLocation().getYaw(), 0).multiply(2)), 2);
		}

		if (!bendable.test(source)) {
			AbilityManager.remove(this);
			return;
		}

		block = createFallingBlock(source);
		user.getStamina().consume(staminaCost / 2);
		collider = new Collider(block.getLocation());
	}

	@Override
	protected boolean onUpdate(double timeDelta) {
		if (!shot) {
			return true;
		}

		if (!launched) {
			launched = true;
			throwBlock(block, timeDelta);
		}

		trackBlock(block);

		return !block.isDead();
	}

	private FallingBlock createFallingBlock(Block target) {
		BlockData data = target.getBlockData();
		if (data.getMaterial() == Material.LAVA) {
			data = Material.MAGMA_BLOCK.createBlockData();
		}

		TempBlock.from(target).setData(Material.AIR.createBlockData());
		FallingBlock block = target.getWorld().spawnFallingBlock(target.getLocation().add(0.5, 0.8, 0.5), data);
		block.setHurtEntities(false);
		block.setDropItem(false);
		block.setMetadata("earthbolt", new FixedMetadataValue(JavaPlugin.getPlugin(ProjectKorra.class), this));
		Velocity.move(block, UnitVector.POSITIVE_Y.scaled(0.5), this, true);
		return block;
	}

	/*
	 * private void position(FallingBlock fb, Location to, double timeDelta) {
	 * Vector dir = Vectors.direction(fb.getLocation(), to);
	 * 
	 * if (dir.lengthSquared() > 1) { dir.normalize(); }
	 * 
	 * Velocity.move(fb, dir.multiply(timeDelta * drag), this, true); }
	 */

	private void throwBlock(FallingBlock block, double timeDelta) {
		double throwRange = selectRange * 3;
		RayTraceResult ray = block.getWorld().rayTrace(user.getEyeLocation(), user.getDirection(), throwRange, FluidCollisionMode.NEVER, true, 1, (e) -> e instanceof LivingEntity && !(e == block) && !e.getUniqueId().equals(user.getUniqueID()));
		Location target;
		if (ray == null) {
			target = user.getEyeLocation().add(user.getDirection().multiply(throwRange));
		} else {
			target = ray.getHitPosition().toLocation(block.getWorld());
		}

		Vector dir = Vectors.direction(block.getLocation(), target);
		if (dir.lengthSquared() > 1) {
			dir.normalize();
		}

		Velocity.move(block, dir.setY(dir.getY() + 0.05).multiply(3 * speed * timeDelta), this, true);
	}

	private void trackBlock(FallingBlock block) {
		Holder<Boolean> hit = Holder.of(false);
		Effects.forNearbyEntities(block.getLocation().add(0, 0.5, 0), 0.9, (e) -> e instanceof LivingEntity && !e.getUniqueId().equals(user.getUniqueID()), (e) -> {
			hit.setHeld(true);
			Effects.damage((LivingEntity) e, damage, EarthboltInstance.this, false);
		});

		RayTraceResult ray = block.getWorld().rayTraceBlocks(block.getLocation().add(0, 0.5, 0), block.getVelocity(), 1, FluidCollisionMode.NEVER, true);
		if (ray != null) {
			hit.setHeld(true);
		}

		if (hit.getHeld()) {
			Particles.spawnBlockCrack(block.getBlockData(), block.getLocation(), 3, 0.3);
			block.remove();
		} else {
			collider.shift(block.getLocation());
			collider.add(block.getBoundingBox());
		}
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

	void launch() {
		if (shot) {
			return;
		}

		shot = true;
		user.addCooldown(provider, cooldown);
		user.getStamina().consume(staminaCost / 2);
	}

	void blockLanding(Block block, BlockData data) {
		if (!shot) {
			user.addCooldown(provider, cooldown);
			TempBlock.from(block).setData(data);
		} else {
			Particles.spawnBlockCrack(block.getBlockData(), block.getLocation(), 3, 0.3);
		}

		AbilityManager.remove(this);
	}

	@Override
	protected void preUpdate() {
		collider.clear();
	}

	@Override
	public String getTag() {
		return this.getName();
	}

	@Override
	public Collider getHitbox() {
		return collider;
	}

	@Override
	public World getWorld() {
		return user.getLocation().getWorld();
	}

	@Override
	public void onCollide(BoundingBox hitbox) {
		block.remove();
		AbilityManager.remove(this);
	}
}
