package com.projectkorra.core.system.ability;

import com.google.common.collect.ImmutableSet;
import com.projectkorra.core.system.skill.Skill;
import com.projectkorra.core.util.configuration.Config;

public abstract class Ability {
	
	private ImmutableSet<Skill> skills;
	private Config config;
	
	public Ability(Skill...skills) {
		this.config = new Config(this.getName());
		this.skills = new ImmutableSet.Builder<Skill>().add(skills).build();
	}
	
	public Config getConfig() {
		return config;
	}
	
	public ImmutableSet<Skill> getSkills() {
		return skills;
	}
	
	public abstract boolean isBindable();
	public abstract String getName();
	public abstract AbilityInstance activate(AbilityActivator activator, Activation type);
}
