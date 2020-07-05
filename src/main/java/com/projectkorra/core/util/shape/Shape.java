package com.projectkorra.core.util.shape;

import java.util.function.Consumer;

import org.bukkit.Location;

public interface Shape {
	
	public void construct(Consumer<Location> func);
	public Location getCenter();
	public boolean isHollow();
	public void setHollow(boolean hollow);
}
