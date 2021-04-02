package com.projectkorra.core.system.ability;

import org.bukkit.event.Event;

import com.projectkorra.core.system.ability.activation.Activation;
import com.projectkorra.core.system.skill.Skill;

public interface Ability {
	
	public String getName();
	public String getDescription();
	public String getAuthor();
	public String getVersion();
	public Skill getSkill();
	public AbilityInstance activate(AbilityUser user, Activation trigger, Event provider);
	public boolean uses(Activation trigger);
	
	public default boolean canActivate(AbilityUser user, Activation trigger) {
		return uses(trigger) && user.hasSkill(getSkill());
	}
}
