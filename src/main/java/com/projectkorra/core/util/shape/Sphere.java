package com.projectkorra.core.util.shape;

import java.util.function.Consumer;

import org.bukkit.Location;
import org.bukkit.util.Vector;

import com.projectkorra.core.util.math.Angle;
import com.projectkorra.core.util.math.Angle.AngleMode;
import com.projectkorra.core.util.math.Plane;
import com.projectkorra.core.util.math.UnitVector;

public class Sphere extends Polyhedron {

	private double radius;
	private Angle theta, phi;
	
	public Sphere(Location center, double radius, Angle both) {
		this(center, radius, both, both, UnitVector.POSITIVE_Y.normal(), true);
	}
	
	public Sphere(Location center, double radius, Angle both, Vector upwards) {
		this(center, radius, both, both, upwards, true);
	}
	
	public Sphere(Location center, double radius, Angle theta, Angle phi) {
		this(center, radius, theta, phi, UnitVector.POSITIVE_Y.normal(), true);
	}
	
	public Sphere(Location center, double radius, Angle theta, Angle phi, Vector upwards) {
		this(center, radius, theta, phi, upwards, true);
	}
	
	public Sphere(Location center, double radius, Angle theta, Angle phi, Vector upwards, boolean hollow) {
		super(center, upwards, hollow);
		this.radius = radius;
		this.theta = theta;
		this.phi = phi;
		
		if (theta.getRawValue() <= 0) {
			theta.set(AngleMode.DEGREES, 1);
		}
		
		if (phi.getRawValue() <= 0) {
			phi.set(AngleMode.DEGREES, 1);
		}
	}
	
	@Override
	public void construct(Consumer<Location> func) {
		double angleTheta = theta.getValue(AngleMode.RADIANS);
		double anglePhi = phi.getValue(AngleMode.RADIANS);
		Plane horizontal = Plane.fromPerpendicular(upwards.normalize());
		
		if (!hollow) {
			for (double r = 0.0; r < radius; r += 0.1) {
				for (double i = 0.0; i < Math.PI; i += angleTheta) {
					double yv = r * Math.cos(i);
					
					for (double j = 0.0; j < 2 * Math.PI; j += anglePhi) {
						double xv = r * Math.sin(i) * Math.cos(j);
						double zv = r * Math.sin(i) * Math.sin(j);
						func.accept(horizontal.moveAlongAxes(center.clone(), xv, zv).add(upwards.clone().multiply(yv)));
					}
				}
			}
		}
		
		for (double i = 0.0; i < Math.PI; i += angleTheta) {
			double yv = radius * Math.cos(i);
			
			for (double j = 0.0; j < 2 * Math.PI; j += anglePhi) {
				double xv = radius * Math.sin(i) * Math.cos(j);
				double zv = radius * Math.sin(i) * Math.sin(j);
				
				func.accept(horizontal.moveAlongAxes(center.clone(), xv, zv).add(upwards.clone().multiply(yv)));
			}
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

	public Angle getPhi() {
		return phi;
	}

	public void setPhi(Angle phi) {
		this.phi = phi;
	}
}
