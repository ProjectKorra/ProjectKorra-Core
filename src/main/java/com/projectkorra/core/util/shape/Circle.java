package com.projectkorra.core.util.shape;

import java.util.function.Consumer;

import org.bukkit.Location;

import com.projectkorra.core.util.math.Angle;
import com.projectkorra.core.util.math.Angle.AngleMode;
import com.projectkorra.core.util.math.Plane;

public class Circle extends Polygon {

	private double radius;
	private Angle theta, initial;
	
	public Circle(Location center, double radius, Angle theta) {
		this(center, radius, theta, Angle.radians(0), Plane.XZ, true);
	}
	
	public Circle(Location center, double radius, Angle theta, Angle initial) {
		this(center, radius, theta, initial, Plane.XZ, true);
	}
	
	public Circle(Location center, double radius, Angle theta, Angle initial, Plane reference) {
		this(center, radius, theta, initial, reference, true);
	}
	
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
		double angle = theta.getValue(AngleMode.RADIANS);
		double init = initial.getValue(AngleMode.RADIANS);
		
		if (!hollow) {
			for (double r = 0.0; r < radius; r += 0.1) {
				for (double i = init; i < init + 2 * Math.PI; i += angle) {
					func.accept(reference.moveAlongAxes(center.clone(), r * Math.cos(i), r * Math.sin(i)));
				}
			}
		}
		
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
