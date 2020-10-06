package com.projectkorra.core.system.skill;

import net.md_5.bungee.api.ChatColor;

public class Skill {

	private String name, verb, user, description;
	private ChatColor color;
	
	/**
	 * A skill is something that entities can do, common examples of this
	 * are bending, martial arts, weapon using, and spirit powers.
	 * 
	 * @param name what the skill is called or titled
	 * @param verb the action of using the skill
	 * @param user what someone using the skill is called
	 * @param description explanation of the skill
	 * @param color text color associated with this skill
	 */
	Skill(String name, String verb, String user, String description, ChatColor color) {
		this.name = name;
		this.verb = verb;
		this.user = user;
		this.description = description;
		this.color = color;
	}

	public String getName() {
		return name;
	}

	public String getVerb() {
		return verb;
	}

	public String getUser() {
		return user;
	}

	public String getDescription() {
		return description;
	}

	public ChatColor getColor() {
		return color;
	}
	
	public String getDisplay() {
		return color + name;
	}
}
