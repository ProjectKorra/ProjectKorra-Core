package com.projectkorra.core.system.user;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.Validate;
import org.bukkit.entity.LivingEntity;

import com.google.common.collect.ImmutableSet;
import com.projectkorra.core.system.ability.Ability;
import com.projectkorra.core.system.ability.AbilityBindResult;
import com.projectkorra.core.system.skill.Skill;

public abstract class SkilledEntity<T extends LivingEntity> {

	private T entity;
	private ImmutableSet<Skill> skills;
	private Set<Skill>toggled;
	private Ability[] binds; 
	
	SkilledEntity(T entity, Set<Skill> skills, Set<Skill> toggled, Ability[] binds) {
		this.entity = entity;
		this.skills = new ImmutableSet.Builder<Skill>().addAll(skills).build();
		this.toggled = new HashSet<>(toggled);
		this.binds = Arrays.copyOf(binds, 10);
	}
	
	public abstract int getCurrentSlot();
	public abstract void sendMessage(String message);
	
	public T getEntity() {
		return entity;
	}
	
	public ImmutableSet<Skill> getSkills() {
		return skills;
	}
	
	public boolean hasBound(Ability ability) {
		for (Ability bound : binds) {
			if (bound.equals(ability)) {
				return true;
			}
		}
		
		return false;
	}
	
	public Ability getBind(int slot) {
		Validate.isTrue(slot <= 9 && slot >= 0, "Attempt at accessing binds outside [0, 9]");
		return binds[slot];
	}
	
	public Ability getCurrentBind() {
		return getBind(getCurrentSlot());
	}
	
	/**
	 * Bind an ability to the specified slot, or unbind the current ability
	 * if null is given as the ability parameter
	 * @param slot which slot to (un)bind
	 * @param ability ability to bind or null to unbind
	 * @return whether the ability was (un)bound successfully
	 */
	public AbilityBindResult bind(int slot, Ability ability) {
		if (slot < 0 || slot > 9) {
			return AbilityBindResult.INVALID_SLOT;
		}
		
		binds[slot] = ability;
		if (ability == null) {
			return AbilityBindResult.UNBOUND;
		} else {
			return AbilityBindResult.BOUND;
		}
	}
	
	public boolean addSkill(Skill skill) {
		if (!skills.contains(skill)) {
			this.skills = new ImmutableSet.Builder<Skill>().addAll(skills).add(skill).build();
			return true;
		}
		
		return false;
	}
	
	public boolean removeSkill(Skill skill) {
		if (skills.contains(skill)) {
			ImmutableSet.Builder<Skill> old = new ImmutableSet.Builder<>();
			
			for (Skill adding : this.skills) {
				if (!adding.equals(skill)) {
					old.add(adding);
				}
			}
			
			this.skills = old.build();
		}
		
		return false;
	}
	
	public boolean hasSkill(Skill skill) {
		return skills.contains(skill);
	}
	
	public boolean isToggled(Skill skill) {
		return toggled.contains(skill);
	}
	
	/**
	 * Toggle a skill on or off
	 * @param skill which skill to toggle
	 * @return true if toggled, false if not
	 */
	public boolean toggle(Skill skill) {
		if (toggled.contains(skill)) {
			toggled.remove(skill);
			return false;
		} else {
			toggled.add(skill);
			return true;
		}
	}
}
