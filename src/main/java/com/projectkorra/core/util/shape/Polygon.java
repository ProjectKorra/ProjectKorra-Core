package com.projectkorra.core.util.shape;

import org.bukkit.Location;

import com.projectkorra.core.util.math.Plane;

public abstract class Polygon implements Shape {

	protected Location center;
	protected Plane reference;
	protected boolean hollow;
	
	public Polygon(Location center, Plane reference, boolean hollow) {
		this.center = center;
		this.reference = reference;
		this.hollow = hollow;
	}

	@Override
	public Location getCenter() {
		return center;
	}

	public Plane getReference() {
		return reference;
	}

	public void setReference(Plane reference) {
		this.reference = reference;
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
