package com.projectkorra.core.game.physique.wellbeing;

import org.bukkit.event.Event;

import com.projectkorra.core.ability.Ability;
import com.projectkorra.core.ability.AbilityInstance;
import com.projectkorra.core.ability.AbilityUser;
import com.projectkorra.core.ability.activation.Activation;
import com.projectkorra.core.skill.Skill;
import com.projectkorra.core.util.configuration.Configure;

public class WellbeingPassive extends Ability {

	@Configure("health")
	private double health = 40;

	public WellbeingPassive() {
		super("Wellbeing", "description", "ProjectKorra", "CORE", Skill.of("physique"));
	}

	@Override
	public void postProcessed() {
	}

	@Override
	protected AbilityInstance activate(AbilityUser user, Activation trigger, Event provider) {
		if (health < 0) {
			return null;
		}

		return new WellbeingInstance(this, user, health);
	}

	@Override
	protected void onRegister() {
	}

	@Override
	public boolean hasPassive() {
		return true;
	}
}
