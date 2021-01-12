package com.projectkorra.core.system.ability;

import com.google.common.collect.ImmutableSet;
import com.projectkorra.core.system.ability.activation.Activation;
import com.projectkorra.core.system.skill.Skill;
import com.projectkorra.core.util.configuration.Config;

public abstract class Ability {
	
	private ImmutableSet<Skill> skills;
	private Config config;
	
	public Ability(Skill...skills) {
		this.skills = new ImmutableSet.Builder<Skill>().add(skills).build();
		this.config = Config.fromAbility(this);
	}
	
	public final ImmutableSet<Skill> getSkills() {
		return skills;
	}
	
	public final Config getConfig() {
		return config;
	}
	
	public abstract String getName();
	public abstract String getDescription();
	public abstract String getAuthor();
	public abstract String getVersion();
	public abstract AbilityInstance activate(AbilityUser activator, Activation trigger);
}
