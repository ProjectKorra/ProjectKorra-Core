package com.projectkorra.core.game.firebending.firejet;

import org.bukkit.Location;
import org.bukkit.Sound;

import com.projectkorra.core.ability.AbilityUser;
import com.projectkorra.core.ability.attribute.Attribute;
import com.projectkorra.core.ability.attribute.AttributeGroup;
import com.projectkorra.core.game.firebending.FireAbilityInstance;
import com.projectkorra.core.util.Effects;

public class JetDashInstance extends FireAbilityInstance {

	@Attribute(value = Attribute.SPEED, group = AttributeGroup.SPEED)
	private double speed;
	@Attribute(value = Attribute.COOLDOWN, group = AttributeGroup.COOLDOWN)
	private long cooldown;

	public JetDashInstance(FireJetAbility provider, AbilityUser user) {
		super(provider, user);
		this.speed = provider.dashSpeed;
		this.cooldown = provider.dashCooldown;
	}

	@Override
	protected void onStart() {
		if (user.getEntity().isOnGround()) {
			speed *= 1.5;
		}

		Location loc = user.getLocation();
		if (loc.getPitch() < -35) {
			loc.setPitch(-35);
		}

		user.getEntity().setVelocity(loc.getDirection().multiply(speed));
		user.addCooldown("JetExtra", cooldown);
		user.getEntity().setFallDistance(0);
		this.particles(user.getLocation(), 30, 0.4, 0.2, 0.4);
		Effects.playSound(user.getLocation(), Sound.ENTITY_GHAST_SHOOT, 1f, 0.4f);
	}

	@Override
	protected boolean onUpdate(double timeDelta) {
		return false;
	}

	@Override
	protected void postUpdate() {
	}

	@Override
	protected void onStop() {
	}

	@Override
	public String getName() {
		return "JetDash";
	}

	@Override
	protected void preUpdate() {
		// TODO Auto-generated method stub

	}

}
