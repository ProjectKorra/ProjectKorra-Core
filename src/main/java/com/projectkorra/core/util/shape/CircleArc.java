package com.projectkorra.core.util.shape;

import java.util.function.Consumer;

import org.bukkit.Location;

import com.projectkorra.core.util.math.Angle;
import com.projectkorra.core.util.math.Plane;
import com.projectkorra.core.util.math.Angle.AngleMode;

public class CircleArc extends Polygon {

	private double radius;
	private float yaw;
	private Angle theta, interval;
	
	public CircleArc(Location center, double radius, float yaw, Angle theta, Angle interval, Plane reference, boolean hollow) {
		super(center, reference, hollow);
		this.radius = Math.max(radius, 0.1);
		this.yaw = yaw;
		this.theta = theta;
		this.interval = interval;
	}

	@Override
	public void construct(Consumer<Location> func) {
		float lower = yaw - (float) theta.getValue(AngleMode.DEGREES) / 2;
		float upper = yaw + (float) theta.getValue(AngleMode.DEGREES) / 2;
		double angle = interval.getValue(AngleMode.DEGREES);
		
		if (!hollow) {
			for (double y = lower; y <= upper; y += angle) {
				for (double r = 0.0; r <= radius; r += 0.1) {
					func.accept(reference.moveAlongAxes(center.clone(), r * Math.cos(Math.toRadians(y)), r * Math.sin(Math.toRadians(y))));
				}
			}
		} else {
			for (double r = 0.0; r < radius; r += 0.1) {
				func.accept(reference.moveAlongAxes(center.clone(), r * Math.cos(Math.toRadians(lower)), r * Math.sin(Math.toRadians(lower))));
				func.accept(reference.moveAlongAxes(center.clone(), r * Math.cos(Math.toRadians(upper)), r * Math.sin(Math.toRadians(upper))));
			}
			
			for (double y = lower; y <= upper; y += angle) {
				func.accept(reference.moveAlongAxes(center.clone(), radius * Math.cos(Math.toRadians(y)), radius * Math.sin(Math.toRadians(y))));
			}
		}
	}

	public double getRadius() {
		return radius;
	}

	public void setRadius(double radius) {
		this.radius = radius;
	}

	public float getYaw() {
		return yaw;
	}

	public void setYaw(float yaw) {
		this.yaw = yaw;
	}

	public Angle getTheta() {
		return theta;
	}

	public void setTheta(Angle theta) {
		this.theta = theta;
	}

	public Angle getInterval() {
		return interval;
	}

	public void setInterval(Angle interval) {
		this.interval = interval;
	}
}
