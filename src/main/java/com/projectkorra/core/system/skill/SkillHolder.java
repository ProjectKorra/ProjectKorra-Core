package com.projectkorra.core.system.skill;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.google.common.collect.ImmutableSet;

public abstract class SkillHolder {

	public static enum SkillToggleResult {
		ON, OFF, INVALID;
	}

	private ImmutableSet<Skill> skills = null;
	private Set<Skill> toggled;
	
	public SkillHolder() {
		this.toggled = new HashSet<>();
	}
	
	public SkillHolder(Collection<Skill> skills, Collection<Skill> toggled) {
		this.skills = new ImmutableSet.Builder<Skill>().addAll(skills).build();
		this.toggled = new HashSet<>(toggled);
	}
	
	/**
	 * Sets the skills of this SkillHolder to the given Skills
	 * @param skills new skills
	 */
	public final void setSkills(Skill...skills) {
		this.skills = new ImmutableSet.Builder<Skill>().add(skills).build();
	}
	
	/**
	 * Sets the skills of this SkillHolder to the given collection of Skills
	 * @param skills new skills
	 */
	public final void setSkills(Collection<Skill> skills) {
		this.skills = new ImmutableSet.Builder<Skill>().addAll(skills).build();
	}
	
	/**
	 * Add a Skill to this SkillHolder if not already present
	 * @param skill new skill to add
	 * @return false if already has sill, true otherwise
	 */
	public final boolean addSkill(Skill skill) {
		if (skills.contains(skill)) {
			return false;
		}
		
		ImmutableSet.Builder<Skill> builder = new ImmutableSet.Builder<Skill>().add(skill);
		
		if (skills != null) {
			builder.addAll(skills);
		}
		
		this.skills = builder.build();
		return true;
	}
	
	/**
	 * Remove a Skill from this SkillHolder if present
	 * @param skill old skill to remove
	 * @return false if skill is not present, true otherwise
	 */
	public final boolean removeSkill(Skill skill) {
		if (skills == null || !skills.contains(skill)) {
			return false;
		}
		
		ImmutableSet.Builder<Skill> builder = new ImmutableSet.Builder<>();
		
		for (Skill curr : skills) {
			if (!curr.equals(skill)) {
				builder.add(curr);
			}
		}
		
		this.skills = builder.build();
		return true;
	}
	
	/**
	 * Returns whether this SkillHolder has the given Skill
	 * @param skill being checked for
	 * @return true if skill is present, false if not
	 */
	public final boolean hasSkill(Skill skill) {
		return skills.contains(skill);
	}
	
	public final boolean hasAny(Collection<Skill> skills) {
		return this.skills.stream().anyMatch((s) -> skills.contains(s));
	}
	
	/**
	 * Toggle the given skill on / off if the SkillHolder has it
	 * @param skill being toggled
	 * @return the result of the attempted toggle
	 */
	public final SkillToggleResult toggle(Skill skill) {
		if (!skills.contains(skill)) {
			return SkillToggleResult.INVALID;
		}
		
		if (!toggled.add(skill)) {
			toggled.remove(skill);
			return SkillToggleResult.ON;
		}
		
		return SkillToggleResult.OFF;
	}
	
	/**
	 * Returns whether this SkillHolder has the given Skill toggled off
	 * @param skill being checked for toggle
	 * @return true if toggled off, false otherwise
	 */
	public final boolean isToggled(Skill skill) {
		return toggled.contains(skill);
	}
}
