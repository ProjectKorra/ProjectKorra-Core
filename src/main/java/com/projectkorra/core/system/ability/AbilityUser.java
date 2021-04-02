package com.projectkorra.core.system.ability;

import java.util.Collection;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.util.Vector;

import com.projectkorra.core.system.skill.Skill;
import com.projectkorra.core.system.skill.SkillHolder;

/**
 * Interface for identifying something that can activate abilities
 */
public abstract class AbilityUser extends SkillHolder {
	
	private AbilityBinds binds;
	
	public AbilityUser(Collection<Skill> skills) {
		super(skills);
		this.binds = new AbilityBinds();
	}
	
	public AbilityUser(Collection<Skill> skills, Collection<Skill> toggled) {
		super(skills, toggled);
		this.binds = new AbilityBinds();
	}
	
	public final AbilityBinds getBinds() {
		return binds;
	}
	
	/**
	 * Gets the bound ability of the user's current slot
	 * @return currently bound ability
	 */
	public abstract Ability getBoundAbility();
	
	/**
	 * Gets the location of where abilities generally start
	 * from this user
	 * @return ability starting location
	 */
	public abstract Location getLocation();
	
	/**
	 * Gets the direction for abilities to start in
	 * @return ability starting direction
	 */
	public abstract Vector getDirection();
	
	/**
	 * Gets the unique id for this user
	 * @return user unique id
	 */
	public abstract UUID getUniqueID();
	
	/**
	 * Checks whether the user should be removed from
	 * memory or not
	 * @return true to remove user from memory
	 */
	public abstract boolean shouldRemove();
}
