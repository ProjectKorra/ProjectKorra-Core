package com.projectkorra.core.collision;

import org.bukkit.Location;
import org.bukkit.util.BoundingBox;

public interface Collidable {
	
	/**
	 * Gets the identifier for this collidable, using the name
	 * of the ability is recommended.
	 * @return collision tag
	 */
	public String getTag();
	
	/**
	 * Get the current location of this collidable
	 * @return current location
	 */
	public Location getLocation();
	
	/**
	 * Get the hitbox for this collidable
	 * @return hitbox
	 */
	public BoundingBox getHitbox();
}
