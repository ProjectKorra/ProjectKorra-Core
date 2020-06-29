package com.projectkorra.core.util;

import org.apache.commons.lang.Validate;
import org.bukkit.Location;
import org.bukkit.util.Vector;

import com.projectkorra.core.object.Angle;
import com.projectkorra.core.object.Angle.AngleMode;

public class VectorUtil {
	
	private VectorUtil() {}

	public static final Vector ZERO = new Vector(0, 0, 0);

	public static Vector orthogonal(Location axis, double length, Angle rotation) throws IllegalArgumentException {
		Validate.isTrue(!axis.getDirection().equals(ZERO), "Axis vector cannot be zero!");

		double yaw = Math.toRadians(axis.getYaw());
		Vector other = new Vector(-Math.sin(yaw), -axis.getDirection().getY() + 0.1, Math.cos(yaw));
		other.crossProduct(axis.getDirection()).normalize().multiply(length);

		return rotate(other, axis.getDirection(), rotation);
	}

	public static Vector rotate(Vector rotator, Vector axis, Angle rotation) {
		double angle = rotation.getValue(AngleMode.RADIANS);

		if (Math.abs(angle / (2 * Math.PI)) < Vector.getEpsilon()) {
			return rotator;
		} else if (Math.abs(angle / Math.PI) < Vector.getEpsilon()) {
			return rotator.multiply(-1);
		}

		return rotator.rotateAroundAxis(axis, angle);
	}

	/**
	 * Returns a vector pointing in the direction of the from location -> to
	 * location
	 * 
	 * @param from start of vector
	 * @param to end of vector
	 * @return vector from -> to
	 */
	public static Vector direction(Location from, Location to) {
		return new Vector(to.getX() - from.getX(), to.getY() - from.getY(), to.getZ() - to.getZ());
	}

}
