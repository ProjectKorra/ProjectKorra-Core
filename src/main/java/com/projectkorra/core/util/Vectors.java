package com.projectkorra.core.util;

import java.util.Optional;
import java.util.function.Predicate;

import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import com.projectkorra.core.util.math.AngleType;

public class Vectors {

	private Vectors() {
	}

	/**
	 * Get a vector for the direction between two points
	 * 
	 * @param from start point
	 * @param to   end point
	 * @return vector pointing from {@code ->} to
	 */
	public static Vector direction(Location from, Location to) {
		return new Vector(to.getX() - from.getX(), to.getY() - from.getY(), to.getZ() - from.getZ());
	}

	/**
	 * Get the direction for which the given yaw and pitch point in
	 * 
	 * @param yaw
	 * @param pitch
	 * @return
	 */
	public static Vector direction(float yaw, float pitch) {
		double yawRad = Math.toRadians(yaw), pitchRad = Math.toRadians(pitch);
		double xz = Math.cos(pitchRad);
		return new Vector(-xz * Math.sin(yawRad), -Math.sin(pitchRad), xz * Math.cos(yawRad));
	}

	/**
	 * Calculates an orthogonal unit vector from the given axis that points to the
	 * righthand side when viewed in the direction of the axis.
	 * 
	 * @param axis vector to be orthogonal of
	 * @return optional of an orthogonal vector or empty when axis is null/zero
	 */
	public static Optional<Vector> orthogonal(Vector axis) {
		return orthogonal(axis, AngleType.RADIANS, 0);
	}

	/**
	 * Calculates an orthogonal unit vector from the given axis and then rotates it
	 * counterclockwise from the righthand side when viewed in the direction of the
	 * axis.
	 * 
	 * @param axis     vector to be orthogonal of
	 * @param rotation counterclockwise rotation in radians, 0 = righthand side of
	 *                 the axis
	 * @return optional of an orthogonal vector or empty when axis is null/zero
	 */
	public static Optional<Vector> orthogonal(Vector axis, AngleType angle, double rotation) {
		if (axis == null || axis.lengthSquared() == 0) {
			return Optional.empty();
		}

		double yaw = Math.toRadians(getYaw(axis));
		Vector other = new Vector(-Math.sin(yaw), axis.getY() - 1, Math.cos(yaw));
		Vector third = getPitch(other) > getPitch(axis) ? other.getCrossProduct(axis) : axis.getCrossProduct(other);

		return Optional.of(rotate(third.normalize(), axis, angle, rotation));
	}

	/**
	 * Rotate a vector around an axis, this does mutate the rotator
	 * 
	 * @param rotator  vector to rotate
	 * @param axis     axis to rotate around
	 * @param rotation angle to rotate in radians
	 * @return rotated vector, <b>not a new vector</b>
	 */
	public static Vector rotate(Vector rotator, Vector axis, AngleType angle, double rotation) {
		return rotator.rotateAroundAxis(axis, angle.toRadians(-rotation));
	}

	public static RayTraceResult rayTrace(Location start, double maxDistance, FluidCollisionMode fluids, boolean ignorePassable, double raySize, Predicate<Entity> filter) {
		return start.getWorld().rayTrace(start, start.getDirection(), maxDistance, fluids, ignorePassable, raySize, filter);
	}

	public static RayTraceResult rayTrace(Location start, Vector direction, double maxDistance, FluidCollisionMode fluids, boolean ignorePassable, double raySize, Predicate<Entity> filter) {
		return start.getWorld().rayTrace(start, direction, maxDistance, fluids, ignorePassable, raySize, filter);
	}

	/**
	 * Gets the pitch of the given vector, -90 is up and 90 down
	 * 
	 * @param vector a vector
	 * @return pitch of vector
	 */
	public static float getPitch(Vector vector) {
		double x = vector.getX();
		double z = vector.getZ();

		if (x == 0 && z == 0) {
			return vector.getY() > 0 ? -90 : 90;
		}

		return (float) Math.toDegrees(Math.atan(-vector.getY() / Math.sqrt(x * x + z * z)));
	}

	/**
	 * Gets the yaw of the given vector, a vector pointing straight up or down will
	 * give a yaw of 0
	 * 
	 * @param vector a vector
	 * @return yaw of the vector
	 */
	public static float getYaw(Vector vector) {
		return (float) Math.toDegrees((Math.atan2(-vector.getX(), vector.getZ()) + 2 * Math.PI) % (2 * Math.PI));
	}
}
