package com.projectkorra.core.game.lightningbending.bolt;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import com.projectkorra.core.ability.AbilityInstance;
import com.projectkorra.core.ability.AbilityManager;
import com.projectkorra.core.ability.AbilityUser;
import com.projectkorra.core.ability.attribute.Attribute;
import com.projectkorra.core.collision.Collidable;
import com.projectkorra.core.physics.Collider;
import com.projectkorra.core.util.Effects;
import com.projectkorra.core.util.Particles;
import com.projectkorra.core.util.Vectors;
import com.projectkorra.core.util.Velocity;
import com.projectkorra.core.util.math.AngleType;

public class BoltInstance extends AbilityInstance implements Collidable {

	@Attribute("max_charge_time")
	private long maxChargeTime;
	@Attribute("min_charge_time")
	private long minChargeTime;
	@Attribute(DAMAGE)
	private double damage;
	@Attribute(SPEED)
	private double speed;
	@Attribute(RANGE)
	private double range;
	@Attribute(COOLDOWN)
	private long cooldown;

	private double staminaCost;
	private boolean shot = false, subArc = false;
	private Location loc;
	private double maxRange, subarcChance;
	private Collider collider;
	private Set<BoundingBox> hitboxes = new HashSet<>();
	private Set<BoltInstance> subarcs = new HashSet<>();

	public BoltInstance(BoltAbility provider, AbilityUser user, boolean subArc) {
		super(provider, user);
		this.subArc = subArc;
		this.damage = subArc ? provider.subarcDamage : provider.damage;
		this.speed = subArc ? provider.subarcSpeed : provider.speed;
		this.range = subArc ? provider.subarcRange : provider.range;
		this.cooldown = subArc ? 0 : provider.cooldown;
		this.minChargeTime = subArc ? 0 : provider.minChargeTime;
		this.maxChargeTime = subArc ? 0 : provider.maxChargeTime;
		this.subarcChance = subArc ? 0 : provider.subarcChance;
		this.staminaCost = subArc ? 0 : provider.staminaCost;
		this.maxRange = this.range;
	}

	@Override
	protected boolean onStart() {
		collider = new Collider(user.getLocation());
		this.user.getStamina().pauseRegen(this);
		return true;
	}

	@Override
	protected boolean onUpdate(double timeDelta) {
		if (!shot && this.timeLived() <= maxChargeTime) {
			if (!user.getBoundAbility().filter((bound) -> bound == provider).isPresent()) {
				return false;
			}

			if (this.timeLived() >= minChargeTime) {
				double percent = ((double) this.timeLived() - minChargeTime) / ((double) maxChargeTime - minChargeTime);
				for (int i = 0; i < 2; ++i) {
					Vector ortho = Vectors.orthogonal(user.getDirection(), AngleType.RADIANS, Math.random() * Math.PI * 2).get().multiply(0.9 * (1 - percent));
					Particles.lightning(user.getEyeLocation().add(user.getDirection()).add(ortho), 1, 0, 0, 0);
				}
			} else {
				double percent = ((double) this.timeLived()) / (double) minChargeTime;
				Location display = user.getLocation().add(0, 0.5 + percent * 0.6, 0);
				display.setPitch(0);
				double angle = percent * 60;
				float yaw = display.getYaw();
				display.setYaw((float) (yaw + 90 - angle));
				Particles.lightning(display.add(display.getDirection().multiply(0.5)), 1, 0, 0, 0);
				display.subtract(display.getDirection().multiply(0.5));
				display.setYaw((float) (yaw - 90 + angle));
				Particles.lightning(display.add(display.getDirection().multiply(0.5)), 1, 0, 0, 0);
			}

			Effects.playSound(user.getEyeLocation(), Sound.ENTITY_CREEPER_HURT, 1f, 0f);
		} else if (!shot) {
			Effects.damage(user.getEntity(), damage, this, true);
			return false;
		} else {
			if ((range -= speed * timeDelta) <= 0) {
				return false;
			}

			double zag = 0.2 + Math.random() * 0.3;
			Vector ortho = Vectors.orthogonal(loc.getDirection(), AngleType.RADIANS, Math.random() * Math.PI * 2).get().multiply(zag);
			Vector out = loc.getDirection().multiply(speed * timeDelta).add(ortho).multiply(0.1);
			Vector in = loc.getDirection().multiply(speed * timeDelta).subtract(ortho).multiply(0.1);

			if (this.ticksLived() % 3 == 0) {
				Effects.playSound(loc, Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1f, 2.0f);
			}

			double size = 0.1 * range / maxRange;
			double len = speed * timeDelta / 2 / Math.cos(loc.getDirection().angle(out));
			for (double d = 0; d < len; d += 0.1) {
				loc.add(out);
				Particles.lightning(loc, 15, size, size, size);
				if (ray(timeDelta)) {
					Effects.playSound(loc, Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, 1f, 0.4f);
					return false;
				}
				hitboxes.add(BoundingBox.of(loc.clone(), 1 + size, 1 + size, 1 + size));
			}

			if (subarcChance >= 1 || ThreadLocalRandom.current().nextInt(100) < subarcChance * 100) {
				BoltInstance subArc = new BoltInstance((BoltAbility) provider, user, true);
				subArc.shot = true;
				subArc.loc = loc.clone().setDirection(out.add(ortho.multiply(0.2)));
				AbilityManager.start(subArc);
				subarcs.add(subArc);
			}

			collider.shift(loc);

			for (double d = 0; d <= len; d += 0.1) {
				loc.add(in);
				Particles.lightning(loc, 15, size, size, size);
				if (ray(timeDelta)) {
					Effects.playSound(loc, Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, 1f, 0.4f);
					return false;
				}
				hitboxes.add(BoundingBox.of(loc.clone(), 1 + size, 1 + size, 1 + size));
			}

			collider.set(hitboxes);

			if (subarcChance >= 1 || ThreadLocalRandom.current().nextInt(100) < subarcChance * 100) {
				BoltInstance subArc = new BoltInstance((BoltAbility) provider, user, true);
				subArc.shot = true;
				subArc.loc = loc.clone().setDirection(in.add(ortho.multiply(0.2)));
				AbilityManager.start(subArc);
				subarcs.add(subArc);
			}
		}

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
		for (BoltInstance subarc : subarcs) {
			AbilityManager.remove(subarc);
		}
	}

	@Override
	public String getName() {
		return provider.getName();
	}

	private boolean ray(double timeDelta) {
		RayTraceResult ray = loc.getWorld().rayTrace(loc, loc.getDirection(), speed / 2 * timeDelta, FluidCollisionMode.ALWAYS, true, 0.8, null);
		if (ray != null) {
			if (ray.getHitEntity() != null && ray.getHitEntity() instanceof LivingEntity && !ray.getHitEntity().getUniqueId().equals(user.getUniqueID())) {
				Particles.spawn(Particle.EXPLOSION_LARGE, loc);
				Particles.lightning(loc, 30, 0.9, 0.9, 0.9);
				Effects.damage((LivingEntity) ray.getHitEntity(), damage, this, true);
				Velocity.knockback(ray.getHitEntity(), loc.getDirection().setY(0).multiply(0.7), this);
				return true;
			} else if (ray.getHitBlock() != null) {
				Particles.spawn(Particle.EXPLOSION_LARGE, loc);
				Particles.lightning(loc, 30, 0.9, 0.9, 0.9);
				return true;
			}
		}

		return false;
	}

	private void shoot() {
		if (shot) {
			return;
		}
		
		this.shot = true;
		this.loc = user.getEyeLocation();
		this.user.addCooldown(provider, cooldown);
		Effects.playSound(loc, Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1f, 1.5f);
	}

	public void releaseSneak() {
		if (!shot && this.timeLived() >= minChargeTime && user.getStamina().consume(staminaCost)) {
			this.shoot();
		} else {
			AbilityManager.remove(this);
		}
	}

	public void charge() {
		this.minChargeTime = 0;
		this.staminaCost /= 3;
	}

	public boolean canRedirect() {
		return this.timeLived() < minChargeTime;
	}

	@Override
	public String getTag() {
		return provider.getName() + (subArc ? "SubArc" : "");
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
