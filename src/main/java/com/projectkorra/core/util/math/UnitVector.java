package com.projectkorra.core.util.math;

import org.bukkit.util.Vector;

public enum UnitVector {

	ZERO (0, 0, 0), 
	POSITIVE_X (1, 0, 0),
	POSITIVE_Y (0, 1, 0),
	POSITIVE_Z (0, 0, 1),
	NEGATIVE_X (-1, 0, 0),
	NEGATIVE_Y (0, -1, 0),
	NEGATIVE_Z (0, 0, -1);
	
	private double x, y, z;
	
	private UnitVector(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	/**
	 * Gets the normal vector for this unit vector
	 * @return normal vector
	 */
	public Vector normal() {
		return new Vector(x, y, z);
	}
	
	/**
	 * Gets a vector of given magnitude for this unit vector
	 * @param scale magnitude of the vector
	 * @return scaled vector
	 */
	public Vector multiply(double scale) {
		return new Vector(x * scale, y * scale, z * scale);
	}
}
