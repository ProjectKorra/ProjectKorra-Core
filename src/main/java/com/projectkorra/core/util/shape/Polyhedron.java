package com.projectkorra.core.util.shape;

import org.bukkit.Location;
import org.bukkit.util.Vector;

/**
 * Class containing information important to all 3D shapes
 */
public abstract class Polyhedron implements Shape {

	protected Location center;
	protected Vector upwards;
	protected boolean hollow;
	
	public Polyhedron(Location center, Vector upwards, boolean hollow) {
		this.center = center;
		this.upwards = upwards.normalize();
		this.hollow = hollow;
	}
	
	@Override
	public Location getCenter() {
		return center;
	}
	
	public Vector getUpwards() {
		return upwards.normalize();
	}
	
	@Override
	public boolean isHollow() {
		return hollow;
	}
	
	@Override
	public void setHollow(boolean hollow) {
		this.hollow = hollow;
	}
}
