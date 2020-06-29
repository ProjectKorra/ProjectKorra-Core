package com.projectkorra.core.enums;

import org.bukkit.Location;
import org.bukkit.util.Vector;

public enum Dimension {
	X (new Vector(1, 0, 0)), 
	Y (new Vector(0, 1, 0)), 
	Z (new Vector(0, 0, 1));
	
	private Vector axis;
	
	private Dimension(Vector axis) {
		this.axis = axis;
	}
	
	/**
	 * Move the given Location along this Dimension's axis
	 * @param position where to start
	 * @param scale how far to move along axis
	 * @return given Location object moved along axis
	 */
	public Location moveAlongAxis(Location position, double scale) {
		return position.add(axis.clone().multiply(scale));
	}
}
