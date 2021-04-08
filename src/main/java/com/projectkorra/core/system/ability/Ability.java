package com.projectkorra.core.system.ability;

import org.bukkit.event.Event;

import com.projectkorra.core.system.ability.activation.Activation;
import com.projectkorra.core.system.skill.Skill;

public abstract class Ability {
	
	public String getName();
	public String getDescription();
	public String getAuthor();
	public String getVersion();
	public Skill getSkill();
	public AbilityInstance activate(AbilityUser user, Activation trigger, Event provider);
	public boolean uses(Activation trigger);
	
	public Ability(String name, String description, String author, String version, Skill skill) {
		this.name = name;
		this.description = description;
		this.author = author;
		this.version = version;
		this.skill = skill;
	}
	
	public final String getName() {
		return name;
	}

	public final String getDescription() {
		return description;
	}

	public final String getAuthor() {
		return author;
	}

	public final String getVersion() {
		return version;
	}

	public final Skill getSkill() {
		return skill;
	}

	public abstract AbilityInstance activate(AbilityUser user, Activation trigger, Event provider);
	public abstract boolean uses(Activation trigger);
	
	public boolean canActivate(AbilityUser user, Activation trigger) {
		return uses(trigger) && user.hasSkill(getSkill());
	}
}
