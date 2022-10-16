package com.projectkorra.core.util.effect;

import org.bukkit.Location;
import org.bukkit.World;

public interface Spawnable {

	public void spawn(Location loc);
	public void spawn(World world, double x, double y, double z);
	
}
