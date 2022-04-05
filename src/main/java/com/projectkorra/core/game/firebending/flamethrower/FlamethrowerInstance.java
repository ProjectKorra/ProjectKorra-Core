package com.projectkorra.core.game.firebending.flamethrower;

import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import com.projectkorra.core.ability.AbilityUser;
import com.projectkorra.core.ability.attribute.Attribute;
import com.projectkorra.core.game.firebending.FireAbilityInstance;
import com.projectkorra.core.util.Effects;

public class FlamethrowerInstance extends FireAbilityInstance {

	@Attribute(Attribute.DAMAGE)
	private double damage;
	@Attribute(Attribute.RANGE)
	private double range;
	@Attribute(Attribute.COOLDOWN)
	private long cooldown;
	@Attribute(Attribute.RADIUS)
	private double radius;
	@Attribute(Attribute.SPEED)
	private double speed;
	@Attribute("stamina_cost")
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
	}

	@Override
	protected void onStart() {
		user.getStamina().pauseRegen(this);
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
						if (ray.getHitBlock().getRelative(ray.getHitBlockFace()).isEmpty()) {
							ray.getHitBlock().getRelative(ray.getHitBlockFace()).setBlockData(getFireType().createBlockData(), false);
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
			lent.setFireTicks(lent.getFireTicks() + 10);
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

}
