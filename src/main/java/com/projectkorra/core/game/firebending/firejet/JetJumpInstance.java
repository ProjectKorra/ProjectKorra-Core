package com.projectkorra.core.game.firebending.firejet;

import org.bukkit.Particle;
import org.bukkit.Sound;

import com.projectkorra.core.ability.AbilityUser;
import com.projectkorra.core.ability.attribute.Attribute;
import com.projectkorra.core.game.firebending.FireAbilityInstance;
import com.projectkorra.core.util.Effects;
import com.projectkorra.core.util.Particles;

public class JetJumpInstance extends FireAbilityInstance {

	@Attribute(SPEED)
	private double speed;
	@Attribute(COOLDOWN)
	private long cooldown;

	public JetJumpInstance(FireJetAbility provider, AbilityUser user) {
		super(provider, user);
		this.speed = provider.jumpSpeed;
		this.cooldown = provider.jumpCooldown;
	}

	@Override
	protected boolean onStart() {
		Effects.playSound(user.getLocation(), Sound.ENTITY_GHAST_SHOOT, 1f, 0.6f);
		this.particles(user.getLocation(), 30, 0.2, 0.4, 0.2);
		user.addCooldown("JetExtra", cooldown);
		Particles.spawn(Particle.EXPLOSION_LARGE, user.getLocation());
		return true;
	}

	@Override
	protected boolean onUpdate(double timeDelta) {
		if (this.ticksLived() < 1) {
			user.getEntity().setVelocity(user.getEntity().getVelocity().add(user.getDirection().setY(speed)));
		} else if (this.timeLived() > 400) {
			return false;
		}

		this.particles(user.getLocation(), 10, 0.1, 0.1, 0.1);
		return true;
	}

	@Override
	protected void postUpdate() {
	}

	@Override
	protected void onStop() {
	}

	@Override
	public String getName() {
		return "JetJump";
	}

	@Override
	protected void preUpdate() {
		// TODO Auto-generated method stub

	}

}
