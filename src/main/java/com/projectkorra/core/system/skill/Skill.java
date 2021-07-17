package com.projectkorra.core.system.skill;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang.Validate;

import com.google.common.collect.ImmutableSet;
import com.projectkorra.core.util.text.DisplayVariant;

public class Skill {
	
	private static final Map<String, Skill> SKILLS = new HashMap<>();

	private DisplayVariant display;
	private String description;
	private ImmutableSet<Skill> parents, children;
	
	/**
	 * A skill is something that entities can do, common examples of this
	 * are bending, martial arts, weapon using, and spirit powers.
	 * 
	 * @param display {@link DisplayVariant} for this skill
	 * @param description What this skill enables players to do
	 */
	Skill(DisplayVariant display, String description) {
		this.display = display;
		this.description = description;
	}

	/**
	 * Returns the internal name of this skill, which is lowercased
	 * @return internal skill name
	 */
	public String getInternalName() {
		return display.getNoun().toLowerCase();
	}

	public String getDescription() {
		return description;
	}
	
	public DisplayVariant getDisplay() {
		return display;
	}
	
	public ImmutableSet<Skill> getChildren() {
		return children;
	}
	
	public ImmutableSet<Skill> getParents() {
		return parents;
	}
	
	public static Optional<Skill> getByName(String name) {
		return Optional.ofNullable(SKILLS.get(name.toLowerCase()));
	}
	
	/**
	 * Attempt to register a new skill with the given parameters
	 * @param display how the skill will be displayed
	 * @param description description of the skill
	 * @param parents which skills this one derives from
	 * @return newly registered skill
	 */
	public static Skill register(DisplayVariant display, String description, Skill... parents) {
		Validate.isTrue(display != null, "Attempted to register Skill with null display");
		Validate.isTrue(display.isValid(), "Attempted to register Skill with invalid display (data inside the display is null)");
		Validate.isTrue(!SKILLS.containsKey(display.getNoun().toLowerCase()), "Attempted to register Skill with pre-existing name");
		
		Skill skill = new Skill(display, description);
		SKILLS.put(display.getNoun().toLowerCase(), skill);
		
		for (Skill parent : parents) {
			if (parent == null) {
				continue;
			}
			
			parent.children = new ImmutableSet.Builder<Skill>().addAll(parent.children).add(skill).build();
		}
		
		skill.parents = ImmutableSet.copyOf(parents);
		skill.children = ImmutableSet.of();
		
		return skill;
	}
}
