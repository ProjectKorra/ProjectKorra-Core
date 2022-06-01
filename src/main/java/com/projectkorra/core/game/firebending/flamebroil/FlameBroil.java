package com.projectkorra.core.game.firebending.flamebroil;

import org.bukkit.event.Event;

import com.projectkorra.core.ability.Ability;
import com.projectkorra.core.ability.AbilityInstance;
import com.projectkorra.core.ability.AbilityUser;
import com.projectkorra.core.ability.activation.Activation;
import com.projectkorra.core.ability.type.Bindable;
import com.projectkorra.core.skill.Skill;

public class FlameBroil extends Ability implements Bindable {

	public FlameBroil() {
		super("FlameBroil", "Cook some food", "ProjectKorra", "CORE", Skill.FIREBENDING);
	}

	@Override
	public void postProcessed() {
	}

	@Override
	public String getInstructions() {
		return null;
	}

	@Override
	protected AbilityInstance activate(AbilityUser user, Activation trigger, Event provider) {
		if (user.isOnCooldown(this)) {
			return null;
		}

		return null;
	}

	@Override
	protected void onRegister() {
	}

}
