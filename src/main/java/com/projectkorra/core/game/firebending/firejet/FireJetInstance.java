package com.projectkorra.core.game.firebending.firejet;

import org.bukkit.FluidCollisionMode;
import org.bukkit.Sound;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import com.projectkorra.core.ability.AbilityInstance;
import com.projectkorra.core.ability.AbilityUser;
import com.projectkorra.core.ability.attribute.Attribute;
import com.projectkorra.core.util.Effects;

public class FireJetInstance extends AbilityInstance {

	@Attribute("max_speed")
	private double maxSpeed;
	@Attribute(COOLDOWN)
	private long cooldown;
	@Attribute("acceleration")
	private double acceleration;
	
	private double staminaDrain;
	private Vector velocity;

	public FireJetInstance(FireJet provider, AbilityUser user) {
		super(provider, user);
		this.maxSpeed = provider.speed;
		this.cooldown = provider.cooldown;
		this.acceleration = provider.acceleration;
		this.staminaDrain = provider.jetDrain;
	}

	@Override
	protected boolean onStart() {
		Effects.playSound(user.getLocation(), Sound.ENTITY_GHAST_SHOOT, 1f, 0.6f);
		user.getStamina().pauseRegen(this);
		velocity = user.getEntity().getVelocity();
		return true;
	}

	@Override
	protected boolean onUpdate(double timeDelta) {
		if (!user.getStamina().consume(timeDelta * staminaDrain)) {
			return false;
		} else if (user.getLocation().getBlock().isLiquid()) {
			return false;
		} else if (!user.getBoundAbility().filter((a) -> a == provider).isPresent()) {
			return false;
		}

		velocity.add(user.getDirection().multiply(acceleration * timeDelta));
		RayTraceResult ray = user.getLocation().getWorld().rayTraceBlocks(user.getLocation(), new Vector(0, -1, 0), 3, FluidCollisionMode.ALWAYS, true);
		if (ray != null) {
			//this.particles(user.getLocation().subtract(0, 0.2, 0), 13, 0.1, 0.4, 0.1);
		} else if (velocity.getY() > 0) {
			double v = velocity.length();
			velocity.setY(velocity.getY() * 0.5).normalize().multiply(v);
		}

		if (velocity.length() > maxSpeed * timeDelta) {
			velocity.normalize().multiply(maxSpeed * timeDelta);
		}

		user.getEntity().setVelocity(velocity);
		user.getEntity().setFallDistance(0);
		//this.particles(user.getLocation(), 7, 0.2, 0.2, 0.2);
		Effects.playSound(user.getLocation(), Sound.BLOCK_FIRE_AMBIENT, 1f, 1.8f);
		return true;
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
	}
}
