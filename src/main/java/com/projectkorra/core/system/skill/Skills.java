package com.projectkorra.core.system.skill;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang.Validate;

import net.md_5.bungee.api.ChatColor;

public class Skills {

	private static Map<String, Skill> skills;
	private static Map<Skill, List<SkillConnection>> connections;
	
	static {
		skills = new HashMap<>();
		connections = new HashMap<>();
	}
	
	private Skills() {}
	
	public static Optional<Skill> getByName(String name) {
		return Optional.ofNullable(skills.get(name));
	}
	
	/**
	 * Attempt to register a skill with the given information
	 * @param name what the skill is called or titled, <b>nonnull</b>
	 * @param verb the action of using the skill, <b>nonnull</b>
	 * @param user what someone using the skill is called, <b>nonnull</b>
	 * @param description explanation of the skill, <b>nonnull</b>
	 * @param color text color associated with this skill, <b>nonnull</b>
	 * @param parents any Skill(s) that is the parent of the skill being registered
	 * @return registered skill
	 */
	public static Skill register(String name, String verb, String user, String description, ChatColor color, Skill... parents) {
		Validate.isTrue(name != null, "Attempted to register Skill with null name");
		Validate.isTrue(!skills.containsKey(name), "Attempted to register Skill with pre-existing name");
		Validate.isTrue(verb != null, "Attempted to register Skill with null verb");
		Validate.isTrue(user != null, "Attempted to register Skill with null user");
		Validate.isTrue(description != null, "Attempted to register Skill with null description");
		Validate.isTrue(color != null, "Attempted to register Skill with null color");
		
		Skill skill = new Skill(name, verb, user, description, color);
		skills.put(name, skill);
		connections.put(skill, new ArrayList<>());
		
		for (Skill parent : parents) {
			if (parent == null) {
				continue;
			}
			
			connections.get(skill).add(new SkillConnection(parent, SkillConnection.Type.PARENT));
			connections.get(parent).add(new SkillConnection(skill, SkillConnection.Type.CHILD));
		}
		
		return skill;
	}
	
	public static List<Skill> getChildren(Skill skill) {
		List<Skill> childs = new ArrayList<>();
		
		for (SkillConnection connected : connections.get(skill)) {
			if (connected.getType() == SkillConnection.Type.CHILD) {
				childs.add(connected.getSkill());
			}
		}
		
		return childs;
	}
	
	public static List<Skill> getParents(Skill skill) {
		List<Skill> parents = new ArrayList<>();
		
		for (SkillConnection connected : connections.get(skill)) {
			if (connected.getType() == SkillConnection.Type.PARENT) {
				parents.add(connected.getSkill());
			}
		}
		
		return parents;
	}
}
