package com.projectkorra.core.system.ability;

import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.util.Vector;

/**
 * Interface for identifying something that can activate abilities
 */
public interface AbilityUser {
	
	public Ability getBoundAbility();
	public Location getLocation();
	public Vector getDirection();
	public UUID getUniqueID();
	
}
