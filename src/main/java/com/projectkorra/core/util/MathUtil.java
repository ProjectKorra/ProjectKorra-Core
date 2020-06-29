package com.projectkorra.core.util;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.util.Vector;

import com.projectkorra.core.enums.Dimension;
import com.projectkorra.core.object.Angle;
import com.projectkorra.core.object.Angle.AngleMode;

public class MathUtil {

	/**
	 * Returns a vector pointing in the direction of the from location -> to location
	 * @param from start of vector
	 * @param to end of vector
	 * @return vector from -> to
	 */
	public static Vector getVectorFromTo(Location from, Location to) {
		return new Vector(to.getX() - from.getX(), to.getY() - from.getY(), to.getZ() - to.getZ());
	}
	
	/**
	 * Create a List of locations for a horizontal circle with the given properties
	 * @param center center location of the circle
	 * @param radius distance from the center to any given point on the circle
	 * @param theta angular interval between points on the circle. <b>Using multiples of Math.PI is recommended</b>
	 * @return a List of points on the edge of a horizontal circle
	 */
	public static List<Location> getCircle(Location center, double radius, Angle theta) {
		return getCircle(center, radius, theta, Dimension.X, Dimension.Z);
	}
	
	/**
	 * Create a List of locations for a circle with the given properties
	 * @param center center Location of the circle
	 * @param radius distance from the center to any given point on the circle
	 * @param theta angular interval between points on the circle.
	 * @param a first dimension for the circle to extend in. This is recommended to be X or Y
	 * @param b second dimension for the circle to extend in. This is recommended to be Y or Z
	 * @return a List of points on the edge of the circle
	 * @throws IllegalArgumentException when given Dimensions are the same
	 */
	public static List<Location> getCircle(Location center, double radius, Angle theta, Dimension a, Dimension b) throws IllegalArgumentException {
		if (a == b) {
			throw new IllegalArgumentException("Dimension axes for the circle cannot be the same!");
		}
		
		List<Location> circle = new ArrayList<>();
		
		for (double angle = 0; angle < Math.PI * 2; angle += theta.getValueInRadians()) {
			double av = Math.cos(angle) * radius;
			double bv = Math.sin(angle) * radius;
			
			Location point = center.clone();
			a.moveAlongAxis(point, av);
			b.moveAlongAxis(point, bv);
			circle.add(point);
		}
		
		return circle;
	}
	
	/**
	 * Create a List of locations for a sphere with the given properties
	 * @param center center Location of the sphere
	 * @param radius distance from the center to any given point on the sphere
	 * @param thetaInterval horizontal angular interval between points on the circle. <b>Using multiples of Math.PI is recommended</b>
	 * @param phiInterval vertical angular interval between points on the circle. <b>Using multiples of Math.PI is recommended</b>
	 * @return a List of locations on the edge of the sphere
	 */
	public static List<Location> getSphere(Location center, double radius, double thetaInterval, double phiInterval) {
		List<Location> coords = new ArrayList<>();
		
		for (double theta = 0; theta < Math.PI; theta += thetaInterval) {
			for (double phi = 0; phi < Math.PI * 2; phi += phiInterval) {
				double x = center.getX() + radius * Math.sin(theta) * Math.cos(phi);
				double y = center.getY() + radius * Math.cos(theta);
				double z = center.getZ() + radius * Math.sin(theta) * Math.sin(phi);
				
				coords.add(new Location(center.getWorld(), x, y, z));
			}
		}
		
		return coords;
	}
}
