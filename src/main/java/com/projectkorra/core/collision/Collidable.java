package com.projectkorra.core.collision;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.BoundingBox;

import com.projectkorra.core.physics.Collider;

public interface Collidable {

	/**
	 * Gets the identifier for this collidable, using the name of the ability is
	 * recommended.
	 * 
	 * @return collision tag
	 */
	public String getTag();

	/**
	 * Get the hitbox for this collidable. Recommended to use
	 * {@link BoundingBox#of(Location, double, double, double)} to create a
	 * {@link BoundingBox} and then set that object as the return of this method
	 * 
	 * @return hitbox
	 */
	public Collider getHitbox();

	/**
	 * Get the world this collidable exists in
	 * 
	 * @return occupied world
	 */
	public World getWorld();

	/**
	 * Method called when a collision has happened and this collidable should be
	 * removed
	 * 
	 * @param hitbox TODO
	 */
	public void onCollide(BoundingBox hitbox);

	/**
	 * Gets the location of this ability by converting the center of the hitbox into
	 * a location
	 * 
	 * @return center location
	 */
	public default Location getLocation() {
		return getHitbox().getLocation();
	}
}
