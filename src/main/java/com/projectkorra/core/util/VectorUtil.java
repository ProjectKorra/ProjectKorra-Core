package com.projectkorra.core.util;

import com.google.common.base.Preconditions;

import org.bukkit.Location;
import org.bukkit.util.Vector;

import com.projectkorra.core.util.math.Angle;
import com.projectkorra.core.util.math.UnitVector;
import com.projectkorra.core.util.math.Angle.AngleMode;

public class VectorUtil {

	private VectorUtil() {}
	
	/**
	 * Get a vector for the direction between two points
	 * @param from start point
	 * @param to end point
	 * @return vector pointing from {@code ->} to
	 */
	public static Vector direction(Location from, Location to) {
		return new Vector(to.getX() - from.getX(), to.getY() - from.getY(), to.getZ() - from.getZ());
	}
	
	/**
	 * Returns an orthogonal vector from an axis
	 * @param axis vector to be orthogonal of
	 * @param length magnitude of orthogonal vector
	 * @param rotation counterclockwise rotation, 0 = righthand side of the axis
	 * @return an orthogonal vector
	 * @throws IllegalArgumentException when the axis vector is a zero vector
	 */
	public static Vector orthogonal(Vector axis, double length, Angle rotation) throws IllegalArgumentException {
		Preconditions.checkArgument(!axis.equals(UnitVector.ZERO.normal()), "Axis direction cannot be the zero vector!");

		double yaw = Math.toRadians(getYaw(axis));
		Vector other = new Vector(-Math.sin(yaw), axis.getY() - 1, Math.cos(yaw));
		Vector third = getPitch(other) > getPitch(axis) ? other.getCrossProduct(axis) : axis.getCrossProduct(other);

		return rotate(third.normalize().multiply(length), axis, rotation);
	}
	
	/**
	 * Rotate a vector around an axis, this does mutate the rotator
	 * @param rotator vector to rotate
	 * @param axis axis to rotate around
	 * @param rotation angle to rotate
	 * @return rotated vector, <b>not a new vector</b>
	 */
	public static Vector rotate(Vector rotator, Vector axis, Angle rotation) {
		return rotator.rotateAroundAxis(axis, -rotation.getValue(AngleMode.RADIANS));
	}
	
	/**
	 * Gets the pitch of the given vector, -90 is up and 90 down
	 * @param vector a vector
	 * @return pitch of vector
	 */
	public static float getPitch(Vector vector) {
		double x = vector.getX();
		double z = vector.getZ();
		
		if (x == 0 && z == 0) {
		    return vector.getY() > 0 ? -90 : 90;
		}
		
		double xz = Math.sqrt(x * x + z * z);
		return (float) Math.toDegrees(Math.atan(-vector.getY() / xz));
	}
	
	/**
	 * Gets the yaw of the given vector, a vector pointing straight
	 * up or down will give a yaw of 0
	 * @param vector a vector
	 * @return yaw of the vector
	 */
	public static float getYaw(Vector vector) {
		double _2PI = 2 * Math.PI;
		double theta = Math.atan2(-vector.getX(), vector.getZ());
		return (float) Math.toDegrees((theta + _2PI) % _2PI);
	}
}
