package com.projectkorra.core.game.firebending.flamethrower;

import org.bukkit.event.Event;

import com.projectkorra.core.ability.Ability;
import com.projectkorra.core.ability.AbilityInstance;
import com.projectkorra.core.ability.AbilityManager;
import com.projectkorra.core.ability.AbilityUser;
import com.projectkorra.core.ability.activation.Activation;
import com.projectkorra.core.ability.type.Bindable;
import com.projectkorra.core.skill.Skill;
import com.projectkorra.core.util.configuration.Configure;

public class Flamethrower extends Ability implements Bindable {

	@Configure
	double damage = 1;
	@Configure
	double range = 13;
	@Configure
	long cooldown = 2500;
	@Configure
	double radius = 1.2;
	@Configure
	double speed = 25;
	@Configure
	double staminaCost = 0.05;
	@Configure
	double staminaDrain = 0.18;
	@Configure
	long fireLifetime = 10000;
	@Configure
	int fireTicks = 2;

	public Flamethrower() {
		super("Flamethrower", "Create large streams of fire.", "ProjectKorra", "CORE", Skill.FIREBENDING);
	}

	@Override
	public String getInstructions() {
		return "Hold sneak to create a stream of fire.";
	}

	@Override
	public void postProcessed() {
	}

	@Override
	protected AbilityInstance activate(AbilityUser user, Activation trigger, Event provider) {
		if (user.isOnCooldown(this)) {
			return null;
		}

		if (trigger == Activation.SNEAK_DOWN && user.getStamina().consume(staminaCost)) {
			return new FlamethrowerInstance(this, user);
		} else if (trigger == Activation.SNEAK_UP) {
			AbilityManager.getInstance(user, FlamethrowerInstance.class).ifPresent((inst) -> AbilityManager.remove(inst));
		}

		return null;
	}

	@Override
	protected void onRegister() {
	}

}
