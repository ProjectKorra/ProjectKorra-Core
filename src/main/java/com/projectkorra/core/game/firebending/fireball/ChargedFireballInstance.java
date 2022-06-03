package com.projectkorra.core.game.firebending.fireball;

import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import com.projectkorra.core.ability.AbilityManager;
import com.projectkorra.core.ability.AbilityUser;
import com.projectkorra.core.ability.attribute.Attribute;
import com.projectkorra.core.collision.Collidable;
import com.projectkorra.core.game.firebending.FireAbilityInstance;
import com.projectkorra.core.physics.Collider;
import com.projectkorra.core.util.Effects;
import com.projectkorra.core.util.Particles;
import com.projectkorra.core.util.Velocity;

public class ChargedFireballInstance extends FireAbilityInstance implements Collidable {

	@Attribute("max_speed")
	private double maxSpeed;
	@Attribute("min_speed")
	private double minSpeed;
	@Attribute("max_range")
	private double maxRange;
	@Attribute("min_range")
	private double minRange;
	@Attribute("max_damage")
	private double maxDamage;
	@Attribute("min_damage")
	private double minDamage;
	@Attribute(COOLDOWN)
	private long cooldown;
	@Attribute(KNOCKBACK)
	private double knockback;
	@Attribute(CHARGE_TIME)
	private long chargeTime;
	
	private double staminaCost;
	private Location loc;
	private Collider collider;
	private double size = 0.5;
	private boolean shot = false, controlled = true;
	private double range, speed, damage, steering;
	private double minChargeTime;

	public ChargedFireballInstance(Fireball provider, AbilityUser user) {
		super(provider, user);
		this.maxSpeed = provider.chargedMaxSpeed;
		this.maxDamage = provider.chargedMaxDamage;
		this.maxRange = provider.chargedMaxRange;
		this.minSpeed = provider.chargedMinSpeed;
		this.minDamage = provider.chargedMinDamage;
		this.minRange = provider.chargedMinRange;
		this.cooldown = provider.chargedCooldown;
		this.knockback = provider.chargedKnockback;
		this.chargeTime = provider.chargedTime;
		this.minChargeTime = provider.chargedMinTime;
		this.staminaCost = provider.chargedStaminaCost;
	}

	@Override
	protected boolean onStart() {
		loc = user.getEyeLocation();
		collider = new Collider(loc);
		user.getStamina().pauseRegen(this);
		return true;
	}

	@Override
	protected boolean onUpdate(double timeDelta) {
		if (!shot) {
			if (this.timeLived() >= (2 * chargeTime) + minChargeTime) {
				this.shoot();
				return true;
			} else if (!user.getBoundAbility().filter((a) -> a == provider).isPresent()) {
				return false;
			}

			Location display = user.getEyeLocation().add(user.getDirection().multiply(0.7));
			if (this.timeLived() >= chargeTime) {
				this.particles(display, 1, 0.1, 0.1, 0.1);
			} else if (this.timeLived() >= minChargeTime) {
				double chargePercent = Math.min(((double) this.timeLived()) / chargeTime, 1.0);
				display.getWorld().spawnParticle(Particle.SMOKE_NORMAL, display, 1, chargePercent * 0.1, chargePercent * 0.1, chargePercent * 0.1, 0, null);
			}

			return true;
		} else if ((range -= timeDelta * speed) <= 0) {
			return false;
		}

		Vector dir = loc.getDirection().multiply(timeDelta * speed);

		if (controlled) {
			dir.add(user.getDirection().multiply(timeDelta * steering * range));
		}

		loc.add(dir.normalize().multiply(timeDelta * speed));
		loc.setDirection(dir);

		collider.shift(loc);
		collider.add(BoundingBox.of(loc, size * 4, size * 4, size * 4));
		this.particles(loc, (int) (size * size * 100), size + 0.1, size + 0.1, size + 0.1);

		if (this.ticksLived() % 4 == 0) {
			Effects.playSound(loc, Sound.BLOCK_FIRE_AMBIENT, 1f, 1.5f);
		}

		RayTraceResult ray = loc.getWorld().rayTrace(loc, dir, timeDelta * speed, FluidCollisionMode.ALWAYS, true, size + 0.1, null);
		if (ray != null) {
			if (ray.getHitEntity() != null && affect(ray.getHitEntity(), timeDelta)) {
				Particles.spawn(Particle.EXPLOSION_LARGE, ray.getHitPosition().toLocation(loc.getWorld()));
				Effects.playSound(loc, Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, 0.9f, 0.9f);
				return false;
			}

			if (ray.getHitBlock() != null) {
				Particles.spawn(Particle.EXPLOSION_LARGE, ray.getHitPosition().toLocation(loc.getWorld()));
				Effects.playSound(loc, Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, 0.9f, 0.9f);
				return false;
			}
		}

		return true;
	}

	private boolean affect(Entity entity, double timeDelta) {
		if (!(entity instanceof LivingEntity) || entity.getUniqueId().equals(user.getUniqueID())) {
			return false;
		}

		Effects.damage((LivingEntity) entity, damage, this, true);
		Velocity.knockback(entity, loc.getDirection().multiply(timeDelta * knockback), null);
		return true;
	}

	public boolean canShoot() {
		return !shot && this.timeLived() >= this.minChargeTime;
	}

	void shoot() {
		if (shot) {
			return;
		}

		if (!user.getStamina().consume(staminaCost)) {
			AbilityManager.remove(this);
			return;
		}

		double chargePercent = Math.min(((double) this.timeLived() - minChargeTime) / (chargeTime - minChargeTime), 1.0);
		this.steering = (1 - chargePercent);
		this.speed = minSpeed + chargePercent * (maxSpeed - minSpeed);
		this.range = minRange + chargePercent * (maxRange - minRange);
		this.damage = minDamage + chargePercent * (maxDamage - minDamage);
		this.loc = user.getEyeLocation().add(user.getDirection().multiply(0.7));
		this.size *= 0.75 * (1 + chargePercent);
		this.shot = true;
		user.getStamina().unpauseRegen(this);
		user.addCooldown(provider, cooldown);
		user.addCooldown("ChargedFireball", cooldown);
	}

	void releaseSneak() {
		this.controlled = false;
		if (this.canShoot()) {
			this.shoot();
		} else {
			AbilityManager.remove(this);
		}
	}

	void pressSneak() {
		this.controlled = true;
	}

	@Override
	protected void postUpdate() {
	}

	@Override
	protected void onStop() {
	}

	@Override
	public String getName() {
		return "Charged " + provider.getName();
	}

	@Override
	public String getTag() {
		return "Charged" + provider.getName();
	}

	@Override
	public Collider getHitbox() {
		return collider;
	}

	@Override
	public World getWorld() {
		return loc.getWorld();
	}

	@Override
	public void onCollide(BoundingBox hitbox) {
		AbilityManager.remove(this);
	}

	@Override
	protected void preUpdate() {
		// TODO Auto-generated method stub

	}
}
