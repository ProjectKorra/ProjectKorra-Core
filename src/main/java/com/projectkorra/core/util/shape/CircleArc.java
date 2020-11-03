package com.projectkorra.core.util.shape;

import java.util.function.Consumer;

import org.bukkit.Location;

import com.projectkorra.core.util.VectorUtil;
import com.projectkorra.core.util.math.Angle;
import com.projectkorra.core.util.math.Plane;
import com.projectkorra.core.util.math.Angle.AngleMode;

public class CircleArc extends Polygon {

	private double radius;
	private float yaw;
	private Angle theta, interval;
	
	/**
	 * Creates a hollow arc of a circle in the XZ plane
	 * @param center center of the circle (not the arc)
	 * @param radius distance from center to points on the arc
	 * @param yaw starting angle
	 * @param theta angular magnitude of the arc
	 * @param interval angle between each location
	 */
	public CircleArc(Location center, double radius, float yaw, Angle theta, Angle interval) {
		this(center, radius, yaw, theta, interval, Plane.fromPerpendicular(VectorUtil.direction(-90, -90)), true);
	}
	
	/**
	 * Creates a hollow arc of a circle
	 * @param center center of the circle (not the arc)
	 * @param radius distance from center to points on the arc
	 * @param yaw starting angle
	 * @param theta angular magnitude of the arc
	 * @param interval angle between each location
	 * @param reference plane the circle exists in
	 */
	public CircleArc(Location center, double radius, float yaw, Angle theta, Angle interval, Plane reference) {
		this(center, radius, yaw, theta, interval, reference, true);
	}
	
	/**
	 * Creates an arc of a circle
	 * @param center center of the circle (not the arc)
	 * @param radius distance from center to points on the arc
	 * @param yaw starting angle
	 * @param theta angular magnitude of the arc
	 * @param interval angle between each location
	 * @param reference plane the circle exists in
	 * @param hollow true to only draw outline
	 */
	public CircleArc(Location center, double radius, float yaw, Angle theta, Angle interval, Plane reference, boolean hollow) {
		super(center, reference, hollow);
		this.radius = Math.max(radius, 0.1);
		this.yaw = yaw;
		this.theta = theta;
		this.interval = interval;
	}

	@Override
	public void construct(Consumer<Location> func) {
		double lower = Math.toRadians(yaw - theta.getValue(AngleMode.DEGREES) / 2); // lower bound for the yaw
		double upper = Math.toRadians(yaw + theta.getValue(AngleMode.DEGREES) / 2); // upper bound for the yaw
		double angle = interval.getValue(AngleMode.RADIANS); // angle between each location of the arc
		double rinc = radius * angle / theta.getValue(AngleMode.RADIANS); // interval between each location along the radius
		
		if (!hollow) {
			// construct each location
			for (double y = lower; y <= upper; y += angle) {
				for (double r = 0.0; r <= radius; r += rinc) {
					func.accept(reference.moveAlongAxes(center.clone(), r * Math.cos(y), r * Math.sin(y)));
				}
			}
		} else {
			// construct the sides for the outline
			for (double r = 0.0; r < radius; r += rinc) {
				func.accept(reference.moveAlongAxes(center.clone(), r * Math.cos(lower), r * Math.sin(lower)));
				func.accept(reference.moveAlongAxes(center.clone(), r * Math.cos(upper), r * Math.sin(upper)));
			}
			
			// construct the curve for the outline
			for (double y = lower; y <= upper; y += angle) {
				func.accept(reference.moveAlongAxes(center.clone(), radius * Math.cos(y), radius * Math.sin(y)));
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
