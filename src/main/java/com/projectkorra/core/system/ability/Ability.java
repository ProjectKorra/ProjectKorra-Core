package com.projectkorra.core.system.ability;

import java.util.Set;

import com.projectkorra.core.system.skill.Skill;
import com.projectkorra.core.system.user.SkilledEntity;

public interface Ability {
	public Skill getSkill();
	public String getName();
	public AbilityInstance activate(SkilledEntity entity, AbilityActivator activator);
	public Set<Class<? extends AbilityInstance>> instanceClasses();
}
