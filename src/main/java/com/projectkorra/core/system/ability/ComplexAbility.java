package com.projectkorra.core.system.ability;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import com.projectkorra.core.system.ability.activation.Activation;
import com.projectkorra.core.system.skill.Skill;

public abstract class ComplexAbility extends Ability {

	final Map<Activation, Supplier<AbilityInstance>> TRIGGERS = new HashMap<>();
	
	public ComplexAbility(Skill...skills) {
		super(skills);
	}
	
	@Override
	public AbilityInstance activate(Activation trigger) {
		return TRIGGERS.getOrDefault(trigger, () -> null).get();
	}
}
