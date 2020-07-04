package com.projectkorra.core.util.math;

import org.bukkit.util.Vector;

public enum UnitVector {

	ZERO (0, 0, 0), 
	X (1, 0, 0),
	Y (0, 1, 0),
	Z (0, 0, 1);
	
	private double x, y, z;
	
	private UnitVector(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Vector normal() {
		return new Vector(x, y, z);
	}
	
	public Vector multiply(double scale) {
		return new Vector(x * scale, y * scale, z * scale);
	}
}
