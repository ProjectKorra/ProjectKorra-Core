package com.projectkorra.core.system.ability;

import org.bukkit.Location;
import org.bukkit.util.Vector;

/**
 * Interface for identifying something that can activate abilities
 */
public interface AbilityActivator {
	
	public Location getLocation();
	public Vector getDirection();
	
}
