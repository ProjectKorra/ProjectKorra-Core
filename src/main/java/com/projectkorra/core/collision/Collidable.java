package com.projectkorra.core.collision;

import org.bukkit.Location;
import org.bukkit.util.BoundingBox;

public interface Collidable {
	public String getTag();
	public Location getLocation();
	public BoundingBox getBoundary();
}
