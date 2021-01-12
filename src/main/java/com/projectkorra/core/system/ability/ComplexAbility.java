package com.projectkorra.core.system.ability;

import java.util.HashMap;
import java.util.Map;

import com.projectkorra.core.system.ability.activation.Activation;
import com.projectkorra.core.system.ability.activation.ActivationCriteria;
import com.projectkorra.core.system.skill.Skill;

public abstract class ComplexAbility extends Ability {

	final Map<Activation, ActivationCriteria> TRIGGERS = new HashMap<>();
	
	public ComplexAbility(Skill...skills) {
		super(skills);
	}
	
	@Override
	public AbilityInstance activate(AbilityUser activator, Activation trigger) {
		return TRIGGERS.getOrDefault(trigger, ActivationCriteria.EMPTY).update(activator, this, trigger);
	}
}
