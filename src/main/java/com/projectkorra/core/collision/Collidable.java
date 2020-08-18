package com.projectkorra.core.collision;

import org.bukkit.Location;

public interface Collidable {
	public Location getLocation();
	public Boundary getBoundary();
	public void onCollision(Collidable other);
}
