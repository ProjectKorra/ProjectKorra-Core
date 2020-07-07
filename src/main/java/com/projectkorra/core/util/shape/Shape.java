package com.projectkorra.core.util.shape;

import java.util.function.Consumer;

import org.bukkit.Location;

public interface Shape {
	
	/**
	 * Method for constructing the shape and applying
	 * the given Consumer function to each location
	 * @param func what to do with each location
	 */
	public void construct(Consumer<Location> func);
	public Location getCenter();
	public boolean isHollow();
	public void setHollow(boolean hollow);
}
