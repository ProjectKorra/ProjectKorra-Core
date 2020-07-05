package com.projectkorra.core.util.shape;

import java.util.function.Consumer;

import org.bukkit.Location;

import com.projectkorra.core.util.math.Angle;
import com.projectkorra.core.util.math.Angle.AngleMode;
import com.projectkorra.core.util.math.Plane;

public class Circle extends Polygon {

	private double radius;
	private Angle theta;
	
	public Circle(Location center, double radius, Angle theta) {
		this(center, radius, theta, Plane.XZ, true);
	}
	
	public Circle(Location center, double radius, Angle theta, Plane reference) {
		this(center, radius, theta, reference, true);
	}
	
	public Circle(Location center, double radius, Angle theta, Plane reference, boolean hollow) {
		super(center, reference, hollow);
		this.radius = radius;
		this.theta = theta;
		
		if (theta.getRawValue() <= 0) {
			theta.set(AngleMode.DEGREES, 1);
		}
	}
	
	@Override
	public void construct(Consumer<Location> func) {
		double angle = theta.getValue(AngleMode.RADIANS);
		
		if (!hollow) {
			for (double r = 0.0; r < radius; r += 0.1) {
				for (double i = 0.0; i < 2 * Math.PI; i += angle) {
					func.accept(reference.moveAlongAxes(center.clone(), r * Math.cos(i), r * Math.sin(i)));
				}
			}
		}
		
		for (double i = 0.0; i < 2 * Math.PI; i += angle) {
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
}
