package com.projectkorra.core.game.firebending.flameshield;

import org.bukkit.event.Event;

import com.projectkorra.core.ability.Ability;
import com.projectkorra.core.ability.AbilityInstance;
import com.projectkorra.core.ability.AbilityManager;
import com.projectkorra.core.ability.AbilityUser;
import com.projectkorra.core.ability.activation.Activation;
import com.projectkorra.core.ability.type.Bindable;
import com.projectkorra.core.skill.Skill;
import com.projectkorra.core.util.configuration.Configure;

public class FlameShield extends Ability implements Bindable {

	@Configure
	double radius = 1.4;
	@Configure
	long cooldown = 2000;
	@Configure
	double staminaDrain = 0.30;
	@Configure
	double collisionDamage = 0.5;
	@Configure
	double damage = 1;

	public FlameShield() {
		super("FlameShield", "A small shield of flames to block basic abilities.", "ProjectKorra", "CORE", Skill.of("firebending"));
	}

	@Override
	public void postProcessed() {
	}

	@Override
	public String getInstructions() {
		return "Hold sneak to create a small shield of flames for a short duration";
	}

	@Override
	protected AbilityInstance activate(AbilityUser user, Activation trigger, Event provider) {
		if (user.hasCooldown(this)) {
			return null;
		}

		if (trigger == Activation.SNEAK_DOWN && user.getStamina().consume(0.1 * staminaDrain)) {
			return new FlameShieldInstance(this, user);
		} else if (trigger == Activation.SNEAK_UP) {
			user.getInstance(FlameShieldInstance.class).ifPresent(AbilityManager::remove);
		}

		return null;
	}

	@Override
	protected void onRegister() {
	}

}
