package com.projectkorra.core.game.lavabending.lavaflow;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

import com.projectkorra.core.ability.AbilityInstance;
import com.projectkorra.core.ability.AbilityUser;
import com.projectkorra.core.ability.attribute.Attribute;
import com.projectkorra.core.game.BendingBlocks;
import com.projectkorra.core.temporary.TempBlock;
import com.projectkorra.core.util.Blocks;
import com.projectkorra.core.util.Particles;
import com.projectkorra.core.util.Vectors;

public class LavaflowInstance extends AbilityInstance {
	
	private static Material[] STONES = { Material.GRANITE, Material.STONE, Material.ANDESITE, Material.DIORITE };
	
	@Attribute("flow_speed")
	private double flowSpeed;
	@Attribute(SELECT_RANGE)
	private double sourceRange;
	@Attribute(RADIUS)
	private double sourceRadius;
	@Attribute(COOLDOWN)
	private long cooldown;
	@Attribute("lava_create_time")
	private long createTime;
	@Attribute("lava_cool_time")
	private long coolTime;
	
	private double staminaDrain;
	private Location curr;
	private Block target;
	private Set<Block> affected = new HashSet<>();
	
	public LavaflowInstance(Lavaflow provider, AbilityUser user) {
		super(provider, user);
		this.flowSpeed = provider.speed;
		this.staminaDrain = provider.staminaDrain;
		this.sourceRange = provider.sourceRange;
		this.sourceRadius = provider.sourceRadius;
		this.cooldown = provider.cooldown;
		this.createTime = provider.createTime;
		this.coolTime = provider.coolTime;
	}

	@Override
	protected boolean onStart() {
		Block source = Blocks.targeted(user.getEyeLocation(), sourceRange, (b) -> b.isPassable() && b.getType() != Material.LAVA);
		
		if (!BendingBlocks.isEarthbendable(source) && !BendingBlocks.isLavabendable(source)) {
			return false;
		}
		
		curr = source.getLocation().add(0.5, 0.5, 0.5);
		user.getStamina().pauseRegen(this);
		return true;
	}

	@Override
	protected boolean onUpdate(double timeDelta) {
		if (!user.getBoundAbility().filter((a) -> a == provider).isPresent()) {
			return false;
		}
		
		target();
		
		if (target == null) {
			return false;
		}
		
		Vector dir = Vectors.direction(curr, target.getLocation());
		if (dir.lengthSquared() > 1) {		
			dir.normalize();
		}
		
		curr.add(dir.multiply(flowSpeed * timeDelta));
		
		double increment = Math.min(0.25, 0.25 * sourceRadius);
		for (double x = -sourceRadius/2; x <= sourceRadius/2; x += increment) {
			for (double z = -sourceRadius/2; z <= sourceRadius/2; z += increment) {
				Location loc = curr.clone().add(x, 0, z);
				Block block = Blocks.findTop(loc, 1);
				if ((!BendingBlocks.isEarthbendable(block) && !BendingBlocks.isLavabendable(block)) || affected.contains(block)) {
					continue;
				} else if (block.getType() == Material.LAVA) {
					continue;
				} else if (!user.getStamina().consume(staminaDrain/(sourceRadius * sourceRadius) * timeDelta)) {
					return false;
				}
				
				affected.add(block);
				TempBlock.from(block).setData(STONES[ThreadLocalRandom.current().nextInt(STONES.length)].createBlockData());
				TempBlock.from(block).setData(Material.MAGMA_BLOCK.createBlockData(), createTime + 2 * coolTime);
				TempBlock.from(block).setData(Material.LAVA.createBlockData(), createTime + coolTime);
				TempBlock.from(block).setData(Material.MAGMA_BLOCK.createBlockData(), createTime);
				Particles.spawn(Particle.LAVA, loc);
			}
		}
		
		return true;
	}

	@Override
	protected void preUpdate() {
	}

	@Override
	protected void postUpdate() {
	}

	@Override
	protected void onStop() {
		user.addCooldown(provider, cooldown);
	}
	
	@Override
	public int interpolationInterval() {
		return 1;
	}

	@Override
	public String getName() {
		return provider.getName();
	}

	private void target() {
		Block source = Blocks.targeted(user.getEyeLocation(), sourceRange, (b) -> b.isPassable() && b.getType() != Material.LAVA);
		
		if (!BendingBlocks.isEarthbendable(source) && !BendingBlocks.isLavabendable(source)) {
			return;
		}
		
		target = source;
	}
}
