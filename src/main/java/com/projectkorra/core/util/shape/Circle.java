package com.projectkorra.core.util.shape;

import java.util.function.Consumer;

import org.bukkit.Location;

import com.projectkorra.core.util.math.Angle;
import com.projectkorra.core.util.math.Angle.AngleMode;
import com.projectkorra.core.util.math.Plane;

public class Circle extends Polygon {

	private double radius;
	private Angle theta, initial;
	
	/**
	 * Creates a hollow circle in the XZ plane with no initial angle
	 * @param center center location
	 * @param radius distance from center to any point on the circle
	 * @param theta angle between each location in the circle
	 */
	public Circle(Location center, double radius, Angle theta) {
		this(center, radius, theta, Angle.radians(0), Plane.XZ, true);
	}
	
	/**
	 * Creates a hollow circle in the XZ plane
	 * @param center center location
	 * @param radius distance from center to any point on the circle
	 * @param theta angle between each location in the circle
	 * @param initial angle to start constructing circle from
	 */
	public Circle(Location center, double radius, Angle theta, Angle initial) {
		this(center, radius, theta, initial, Plane.XZ, true);
	}
	
	/**
	 * Creates a hollow circle
	 * @param center center location
	 * @param radius distance from center to any point on the circle
	 * @param theta angle between each location in the circle
	 * @param initial angle to start constructing circle from
	 * @param reference plane to construct circle in
	 */
	public Circle(Location center, double radius, Angle theta, Angle initial, Plane reference) {
		this(center, radius, theta, initial, reference, true);
	}
	
	/**
	 * Creates a circle
	 * @param center center location
	 * @param radius distance from center to any point on the circle
	 * @param theta angle between each location in the circle
	 * @param initial angle to start constructing circle from
	 * @param reference plane to construct circle in
	 * @param hollow true to only draw outline
	 */
	public Circle(Location center, double radius, Angle theta, Angle initial, Plane reference, boolean hollow) {
		super(center, reference, hollow);
		this.radius = radius;
		this.theta = theta;
		this.initial = initial;
		
		if (theta.getRawValue() <= 0) {
			theta.set(AngleMode.DEGREES, 1);
		}
	}
	
	@Override
	public void construct(Consumer<Location> func) {
		double angle = theta.getValue(AngleMode.RADIANS); // angle between each location in the circle
		double init = initial.getValue(AngleMode.RADIANS); // initial angle for the circle to start from
		
		if (!hollow) {
			double rinc = radius * angle / (2 * Math.PI); // interval between each circle generated when not hollow
			
			// construct the inner bits of the circle
			for (double r = 0.0; r < radius; r += rinc) {
				for (double i = init; i < init + 2 * Math.PI; i += angle) {
					func.accept(reference.moveAlongAxes(center.clone(), r * Math.cos(i), r * Math.sin(i)));
				}
			}
		}
		
		// construct outer ring of circle only
		for (double i = init; i < init + 2 * Math.PI; i += angle) {
			func.accept(reference.moveAlongAxes(center.clone(), radius * Math.cos(i), radius * Math.sin(i)));
		}
	}
	
	public double getRadius() {
		return radius;
	}
	
	public void setRadius(double radius) {
		this.radius = radius;
	}
	
	public Angle getTheta() {
		return theta;
	}
	
	public void setTheta(Angle theta) {
		this.theta = theta;
	}
	
	public Angle getInitial() {
		return initial;
	}
	
	public void setInitial(Angle initial) {
		this.initial = initial;
	}
}
