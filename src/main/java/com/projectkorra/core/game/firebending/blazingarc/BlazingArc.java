package com.projectkorra.core.game.firebending.blazingarc;

import org.bukkit.event.Event;

import com.projectkorra.core.ability.Ability;
import com.projectkorra.core.ability.AbilityInstance;
import com.projectkorra.core.ability.AbilityUser;
import com.projectkorra.core.ability.activation.Activation;
import com.projectkorra.core.ability.type.Bindable;
import com.projectkorra.core.skill.Skill;
import com.projectkorra.core.util.configuration.Configure;

public class BlazingArc extends Ability implements Bindable {

	@Configure double speed = 25;
	@Configure double range = 15;
	@Configure double damage = 4;
	@Configure long cooldown = 3500;
	@Configure double knockback = 1.2;
	@Configure long delay = 200;
	@Configure double staminaCost = 0.25;

	public BlazingArc() {
		super("BlazingArc", "Create an arcing blaze in front of you", "ProjectKorra", "CORE", Skill.of("firebending"));
	}

	@Override
	public void postProcessed() {
	}

	@Override
	public String getInstructions() {
		return "Left click";
	}

	@Override
	protected AbilityInstance activate(AbilityUser user, Activation trigger, Event provider) {
		if (user.hasCooldown(this)) {
			return null;
		}

		if (trigger == Activation.LEFT_CLICK && user.getStamina().consume(staminaCost)) {
			return new BlazingArcInstance(this, user);
		}

		return null;
	}

	@Override
	protected void onRegister() {
	}

}
