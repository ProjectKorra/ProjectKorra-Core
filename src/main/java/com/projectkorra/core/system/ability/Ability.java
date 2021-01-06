package com.projectkorra.core.system.ability;

import com.google.common.collect.ImmutableSet;
import com.projectkorra.core.system.skill.Skill;

public abstract class Ability {
	
	private ImmutableSet<Skill> skills;
	
	public Ability(Skill...skills) {
		this.skills = new ImmutableSet.Builder<Skill>().add(skills).build();
	}
	
	public ImmutableSet<Skill> getSkills() {
		return skills;
	}
	
	public abstract String getName();
	public abstract AbilityInstance activate(AbilityActivator activator, Activation trigger);
}
