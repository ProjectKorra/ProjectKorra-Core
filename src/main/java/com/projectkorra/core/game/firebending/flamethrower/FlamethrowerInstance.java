package com.projectkorra.core.game.firebending.flamethrower;

import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import com.projectkorra.core.ability.AbilityUser;
import com.projectkorra.core.ability.attribute.Attribute;
import com.projectkorra.core.game.firebending.FireAbilityInstance;
import com.projectkorra.core.temporary.TempBlock;
import com.projectkorra.core.util.Effects;

public class FlamethrowerInstance extends FireAbilityInstance {

	@Attribute(DAMAGE)
	private double damage;
	@Attribute(RANGE)
	private double range;
	@Attribute(COOLDOWN)
	private long cooldown;
	@Attribute(RADIUS)
	private double radius;
	@Attribute(SPEED)
	private double speed;
	@Attribute(DURATION)
	private long fireLifetime;
	@Attribute(FIRE_TICK)
	private int fireTicks;

	private double staminaCost;
	private double currRange = 0, currRadius = 0, inc;

	public FlamethrowerInstance(Flamethrower provider, AbilityUser user) {
		super(provider, user);
		this.damage = provider.damage;
		this.range = provider.range;
		this.cooldown = provider.cooldown;
		this.radius = provider.radius;
		this.speed = provider.speed;
		this.staminaCost = provider.staminaDrain;
		this.fireLifetime = provider.fireLifetime;
		this.fireTicks = provider.fireTicks;
	}

	@Override
	protected boolean onStart() {
		user.getStamina().pauseRegen(this);
		return true;
	}

	@Override
	protected boolean onUpdate(double timeDelta) {
		if (!user.getBoundAbility().filter((a) -> a == provider).isPresent()) {
			return false;
		} else if (!user.getStamina().consume(timeDelta * staminaCost)) {
			return false;
		}

		if (currRange < range) {
			currRange = Math.min(currRange + timeDelta * speed, range);
			inc = Math.min(0.2, currRange);
		}

		Location loc = user.getEyeLocation().add(user.getDirection());
		Vector dir = user.getDirection().multiply(inc);
		Effects.playSound(loc, Sound.BLOCK_FIRE_AMBIENT, 1f, 1.8f);
		for (double d = 0; d < currRange; d += inc) {
			currRadius = (d / range) * radius;
			loc.add(dir);
			this.particles(loc, (int) Math.floor(1 + currRadius / 40), currRadius * 0.6, currRadius * 0.6, currRadius * 0.6);
			RayTraceResult ray = loc.getWorld().rayTrace(loc, dir, inc, FluidCollisionMode.ALWAYS, true, currRadius, null);
			if (ray != null) {
				boolean br = false;

				if (affect(ray.getHitEntity())) {
					br = true;
				}

				if (ray.getHitBlock() != null) {
					if (ray.getHitBlock().getType().isSolid()) {
						Block place = ray.getHitBlock().getRelative(ray.getHitBlockFace());
						if (place.isEmpty() && place.getRelative(BlockFace.DOWN).getType().isSolid()) {
							TempBlock.from(ray.getHitBlock().getRelative(ray.getHitBlockFace())).setData(getFireType().createBlockData(), fireLifetime + ThreadLocalRandom.current().nextLong(1000) - 500);
						}
					}

					br = true;
				}

				if (br) {
					break;
				}
			}
		}

		return true;
	}

	private boolean affect(Entity entity) {
		if (entity != null && entity instanceof LivingEntity && !entity.getUniqueId().equals(user.getUniqueID())) {
			LivingEntity lent = (LivingEntity) entity;
			Effects.damage(lent, damage, this, false);
			lent.setFireTicks(lent.getFireTicks() + fireTicks);
			return true;
		}

		return false;
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

	@Override
	protected void preUpdate() {
		// TODO Auto-generated method stub

	}

}
