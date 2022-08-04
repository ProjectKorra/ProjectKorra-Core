package com.projectkorra.core.game.firebending.firewheel;

import java.util.Arrays;
import java.util.List;

import org.bukkit.event.Event;

import com.projectkorra.core.ability.Ability;
import com.projectkorra.core.ability.AbilityInstance;
import com.projectkorra.core.ability.AbilityUser;
import com.projectkorra.core.ability.activation.Activation;
import com.projectkorra.core.ability.activation.SequenceInfo;
import com.projectkorra.core.ability.type.Combo;
import com.projectkorra.core.skill.Skill;
import com.projectkorra.core.util.configuration.Configure;

public class FireWheel extends Ability implements Combo {
	
	@Configure double speed = 30;
	@Configure double range = 20;
	@Configure double damage = 4;
	@Configure double radius = 1.2;

	public FireWheel() {
		super("FireWheel", "Create a flaming wheel that hurtles toward your enemy!", "ProjectKorra", "CORE", Skill.FIREBENDING);
	}

	@Override
	public List<SequenceInfo> getSequence() {
		return Arrays.asList(SequenceInfo.of("FlameShield", Activation.SNEAK_DOWN), SequenceInfo.of("Blaze", Activation.SNEAK_UP));
	}

	@Override
	protected AbilityInstance activate(AbilityUser user, Activation trigger, Event provider) {
		if (user.isOnCooldown(this)) {
			return null;
		}
		
		if (trigger == Activation.COMBO) {
			return new FireWheelInstance(this, user);
		}
		
		return null;
	}

}
