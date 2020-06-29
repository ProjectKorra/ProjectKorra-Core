package com.projectkorra.core.util;

import java.util.Arrays;

import org.apache.commons.lang.Validate;
import org.bukkit.Location;
import org.bukkit.block.Block;

public class MathUtil {

	private MathUtil() {}
	
	/**
	 * Calculates the manhattan distance between two blocks
	 * @param from start block
	 * @param to end block
	 * @return manhattan distance
	 */
	public static int distanceManhattan(Block from, Block to) {
		return Math.abs(to.getX() - from.getX()) + Math.abs(to.getY() - from.getY()) + Math.abs(to.getZ() - from.getZ());
	}
	
	/**
	 * Returns the location along the bezier curve for the given control points and t parameter.
	 * The returned location will be in the same world as the first control point, but the control
	 * points do not all need to be in the same world. The t parameter defines what point on the
	 * curve you want to get. This parameter must be between 0 and 1
	 * <br><br>
	 * A Bezier curve is made using at least 2 control points, and uses the given control points
	 * to create a nice curve. By essence, the first control point is the start of the curve and
	 * the last control point is the end of the curve. Usual cases for this are to create either
	 * parabolic or cubic curves, with 3 or 4 points respectively. If you would like to read more,
	 * you should look at the wikipedia page for bezier curves.
	 * 
	 * @param t must be in the range [0, 1]
	 * @param controls control points for the bezier curve (order is assumed to be the length of the array)
	 * @return location along the bezier curve
	 */
	public static Location bezierCurve(double t, Location...controls) {
		Validate.isTrue(controls != null, "Control point array cannot be null");
		Validate.isTrue(controls.length > 1, "Control point array must contain at least two values");
		Validate.isTrue(!Arrays.asList(controls).contains(null), "A control point cannot be null");
		Validate.isTrue(t >= 0 && t <= 1, "Parameter t must be within the range [0, 1]");
		
		int order = controls.length;
		Location loc = new Location(controls[0].getWorld(), 0, 0, 0);
		
		for (int i = 0; i < order; i++) {
			double coefficient = (double) factorial(order) / (double) (factorial(i) * factorial(order - i));
			double basis = coefficient * Math.pow(t, i) * Math.pow(1.0 - t, order - i);
			
			loc.add(controls[i].clone().multiply(basis));
		}
		
		return loc;
	}
	
	/**
	 * Calculate the factorial of an integer
	 * @param n any nonnegative integer
	 * @return factorial of n
	 */
	public static int factorial(int n) {
		Validate.isTrue(n >= 0, "Given integer must be nonnegative!");
		int product = 1;
		
		while (n > 1) {
			product *= n--;
		}
		
		return product;
	}
}