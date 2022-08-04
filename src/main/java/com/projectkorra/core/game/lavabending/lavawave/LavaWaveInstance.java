package com.projectkorra.core.game.lavabending.lavawave;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.FallingBlock;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import com.projectkorra.core.ProjectKorra;
import com.projectkorra.core.ability.AbilityUser;
import com.projectkorra.core.ability.attribute.Attribute;
import com.projectkorra.core.game.BendingBlocks;
import com.projectkorra.core.game.lavabending.LavaAbilityInstance;
import com.projectkorra.core.temporary.TempBlock;
import com.projectkorra.core.util.Blocks;
import com.projectkorra.core.util.Particles;
import com.projectkorra.core.util.Vectors;

public class LavaWaveInstance extends LavaAbilityInstance {
	
	@Attribute(COOLDOWN)
	private long cooldown;
	@Attribute(SPEED)
	private double speed;
	@Attribute(DAMAGE)
	private double damage;
	@Attribute(RANGE)
	private double range;
	@Attribute(WIDTH)
	private double width;
	@Attribute("lava_cool_time")
	private long coolTime;
	
	private double staminaDrain;
	private Location loc;
	private Vector dir, ortho;
	private Set<Block> affected = new HashSet<>();
	private double currWidth = 0, maxRange;

	public LavaWaveInstance(LavaWave provider, AbilityUser user) {
		super(provider, user);
		this.staminaDrain = provider.staminaDrain;
		this.cooldown = provider.cooldown;
		this.speed = provider.speed;
		this.damage = provider.damage;
		this.range = provider.range;
		this.width = provider.width;
		this.coolTime = provider.coolTime;
	}

	@Override
	protected boolean onStart() {
		this.dir = Vectors.direction(user.getLocation().getYaw(), 0);
		this.loc = user.getLocation().add(dir.multiply(0.7));
		this.ortho = Vectors.orthogonal(dir).get();
		this.maxRange = range;
		return true;
	}

	@Override
	protected boolean onUpdate(double timeDelta) {
		if (!user.getBoundAbility().filter((a) -> a == provider).isPresent()) {
			return false;
		} else if (!user.getStamina().consume(timeDelta * staminaDrain)) {
			return false;
		}
		
		this.loc.add(dir.normalize().multiply(timeDelta * speed));
		this.loc.setY(Blocks.findTop(loc, 1).getY());
		
		currWidth = Math.min(currWidth + speed/2 * timeDelta, width);
		
		int blocks = 0;
		for (double d = -currWidth/2.0; d <= currWidth/2.0; d += Math.min(0.25, 0.25 * currWidth)) {
			Vector v = ortho.clone().multiply(d);
			loc.add(v);
			Block top = Blocks.findTop(loc, 1, (b) -> b.isPassable() && !b.isLiquid());
			loc.subtract(v);
			
			if (!BendingBlocks.isEarthbendable(top) && !BendingBlocks.isLavabendable(top)) {
				continue;
			} else if (affected.contains(top)) {
				blocks++;
				continue;
			}
			
			if (Math.random() <= range/maxRange) {
				TempBlock.from(top).setData(this.getRandomStone().createBlockData());
				Material lava = this.getRandomLava();
				TempBlock.from(top).setData(lava.createBlockData(), coolTime);
				
				if (lava == Material.LAVA) {
					createShard(top, v.multiply(timeDelta));
				}
				
				Particles.spawn(Particle.LAVA, loc);
				affected.add(top);
				blocks++;
			}
		}
		
		if (blocks < width/3) {
			return false;
		}
		
		return (range -= speed * timeDelta) > 0;
	}
	
	private void createShard(Block block, Vector ortho) {
		FallingBlock fb = block.getWorld().spawnFallingBlock(block.getLocation().add(0.5, 1, 0.5), Material.MAGMA_BLOCK.createBlockData());
		fb.setDropItem(false);
		fb.setFireTicks(200);
		fb.setVelocity(ortho.add(dir).setY(0.3));
		fb.setMetadata("lavawave", new FixedMetadataValue(JavaPlugin.getPlugin(ProjectKorra.class), this));
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
	public String getName() {
		return provider.getName();
	}

}
