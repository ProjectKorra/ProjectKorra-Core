package com.projectkorra.core.system.ability;

import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.util.Vector;

/**
 * Interface for identifying something that can activate abilities
 */
public interface AbilityUser {
	
	/**
	 * Gets the bound ability of the user's current slot
	 * @return currently bound ability
	 */
	public Ability getBoundAbility();
	
	/**
	 * Gets the location of where abilities generally start
	 * from this user
	 * @return ability starting location
	 */
	public Location getLocation();
	
	/**
	 * Gets the direction for abilities to start in
	 * @return ability starting direction
	 */
	public Vector getDirection();
	
	/**
	 * Gets the unique id for this user
	 * @return user unique id
	 */
	public UUID getUniqueID();
	
	/**
	 * Checks whether the user should be removed from
	 * memory or not
	 * @return true to remove user from memory
	 */
	public boolean shouldRemove();
	
}
