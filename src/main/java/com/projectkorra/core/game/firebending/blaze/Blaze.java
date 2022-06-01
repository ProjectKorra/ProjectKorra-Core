package com.projectkorra.core.game.firebending.blaze;

import org.bukkit.event.Event;

import com.projectkorra.core.ability.Ability;
import com.projectkorra.core.ability.AbilityInstance;
import com.projectkorra.core.ability.AbilityManager;
import com.projectkorra.core.ability.AbilityUser;
import com.projectkorra.core.ability.activation.Activation;
import com.projectkorra.core.ability.type.Bindable;
import com.projectkorra.core.skill.Skill;
import com.projectkorra.core.util.configuration.Configure;

public class Blaze extends Ability implements Bindable {

	@Configure
	double range = 15;
	@Configure
	double staminaDrain = 0.15;
	@Configure
	long fireDuration = 15000;

	public Blaze() {
		super("Blaze", "Expand existing fire into a glorious blaze", "ProjectKorra", "CORE", Skill.FIREBENDING);
	}

	@Override
	public void postProcessed() {
	}

	@Override
	public String getInstructions() {
		return "Hold sneak while looking at fire blocks and drag to expand the fire to new blocks";
	}

	@Override
	protected AbilityInstance activate(AbilityUser user, Activation trigger, Event provider) {
		if (user.isOnCooldown(this)) {
			return null;
		}

		if (trigger == Activation.SNEAK_DOWN) {
			return new BlazeInstance(this, user);
		} else if (trigger == Activation.SNEAK_UP) {
			AbilityManager.getInstance(user, BlazeInstance.class).ifPresent(AbilityManager::remove);
		}

		return null;
	}

	@Override
	protected void onRegister() {
	}

}
