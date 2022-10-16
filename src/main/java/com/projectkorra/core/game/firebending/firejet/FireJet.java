package com.projectkorra.core.game.firebending.firejet;

import org.bukkit.event.Event;

import com.projectkorra.core.ability.Ability;
import com.projectkorra.core.ability.AbilityInstance;
import com.projectkorra.core.ability.AbilityManager;
import com.projectkorra.core.ability.AbilityUser;
import com.projectkorra.core.ability.activation.Activation;
import com.projectkorra.core.ability.type.Bindable;
import com.projectkorra.core.skill.Skill;
import com.projectkorra.core.util.configuration.Configure;

public class FireJet extends Ability implements Bindable {

	@Configure("jet.maxSpeed")
	double speed = 16;
	@Configure("jet.cooldown")
	long cooldown = 2000;
	@Configure("jet.acceleration")
	double acceleration = 2.9;
	@Configure("jet.staminaCost")
	double jetStamina = 0.05;
	@Configure("jet.staminaDrain")
	double jetDrain = 0.14;
	@Configure("dash.speed")
	double dashSpeed = 1.1;
	@Configure("dash.cooldown")
	long dashCooldown = 3500;
	@Configure("dash.staminaCost")
	double dashStamina = 0.1;
	@Configure("jump.speed")
	double jumpSpeed = 1.05;
	@Configure("jump.cooldown")
	long jumpCooldown = 5500;
	@Configure("jump.staminaCost")
	double jumpStamina = 0.15;

	public FireJet() {
		super("FireJet", "Use firebending to create jet propulsion from your hands and feet.", "ProjectKorra", "CORE", Skill.of("firebending"));
	}

	@Override
	public void postProcessed() {
	}

	@Override
	public String getInstructions() {
		return "Click while running ";
	}

	@Override
	protected AbilityInstance activate(AbilityUser user, Activation trigger, Event provider) {
		if (trigger == Activation.LEFT_CLICK && !user.hasCooldown("JetExtra") && user.getStamina().consume(dashStamina)) {
			return new JetDashInstance(this, user);
		}

		if (trigger == Activation.SNEAK_DOWN) {
			if (user.getEntity().isOnGround() && !user.hasCooldown("JetExtra") && user.getStamina().consume(jumpStamina)) {
				return new JetJumpInstance(this, user);
			} else if (!user.hasCooldown(this)) {
				return new FireJetInstance(this, user);
			}
		}

		if (trigger == Activation.SNEAK_UP) {
			user.getInstance(FireJetInstance.class).ifPresent(AbilityManager::remove);
		}

		return null;
	}

	@Override
	protected void onRegister() {
	}

}
