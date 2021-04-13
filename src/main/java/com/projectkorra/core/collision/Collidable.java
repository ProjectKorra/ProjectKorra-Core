package com.projectkorra.core.collision;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.BoundingBox;

public interface Collidable {
	
	/**
	 * Gets the identifier for this collidable, using the name
	 * of the ability is recommended.
	 * @return collision tag
	 */
	public String getTag();
	
	/**
	 * Get the hitbox for this collidable. Recommended to use
	 * {@link BoundingBox#of(Location, double, double, double)} to create
	 * a {@link BoundingBox} and then set that object as the return of 
	 * this method
	 * @return hitbox
	 */
	public BoundingBox getHitbox();
	
	/**
	 * Get the world this collidable exists in
	 * @return occupied world
	 */
	public World getWorld();
	
	/**
	 * Method called when a collision has happened and this collidable
	 * should be removed
	 */
	public void remove();
	
	/**
	 * Gets the location of this ability by converting the
	 * center of the hitbox into a location
	 * @return center location
	 */
	public default Location getLocation() {
		return new Location(getWorld(), getHitbox().getCenterX(), getHitbox().getCenterY(), getHitbox().getCenterZ());
	}
}
