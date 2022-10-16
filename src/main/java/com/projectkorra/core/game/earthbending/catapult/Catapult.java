package com.projectkorra.core.game.earthbending.catapult;

import org.bukkit.event.Event;

import com.projectkorra.core.ability.Ability;
import com.projectkorra.core.ability.AbilityInstance;
import com.projectkorra.core.ability.AbilityUser;
import com.projectkorra.core.ability.activation.Activation;
import com.projectkorra.core.ability.type.Bindable;
import com.projectkorra.core.game.AvatarSkills;

public class Catapult extends Ability implements Bindable {

	public Catapult() {
		super("Catapult", "Raise the earth below you and launch into the air!", "ProjectKorra", "CORE", AvatarSkills.EARTHBENDING);
	}

	@Override
	public String getInstructions() {
		return "Tap sneak to launch yourself through the air";
	}

	@Override
	protected AbilityInstance activate(AbilityUser user, Activation trigger, Event provider) {
		return null;
	}

}
