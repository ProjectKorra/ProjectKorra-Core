package com.projectkorra.core.util.shape;

import java.util.function.Consumer;

import org.bukkit.Location;
import org.bukkit.util.Vector;

import com.projectkorra.core.util.math.Plane;
import com.projectkorra.core.util.math.UnitVector;

public class Cuboid extends Polyhedron {

	private double length, height, width, interval;
	
	/**
	 * Creates a hollow cuboid pointed up along the y-axis
	 * @param center center location
	 * @param sides magnitude of length, height, and width
	 * @param interval distance between each location
	 */
	public Cuboid(Location center, double sides, double interval) {
		this(center, sides, sides, sides, interval, UnitVector.POSITIVE_Y.normal(), true);
	}
	
	/**
	 * Creates a hollow cuboid
	 * @param center center location
	 * @param sides magnitude of length, height, and width
	 * @param interval distance between each location
	 * @param upwards upwards direction
	 */
	public Cuboid(Location center, double sides, double interval, Vector upwards) {
		this(center, sides, sides, sides, interval, upwards, true);
	}
	
	/**
	 * Creates a cuboid
	 * @param center center location
	 * @param sides magnitude of length, height, and width
	 * @param interval distance between each location
	 * @param upwards upwards direction
	 * @param hollow true to only draw outline
	 */
	public Cuboid(Location center, double sides, double interval, Vector upwards, boolean hollow) {
		this(center, sides, sides, sides, interval, upwards, hollow);
	}
	
	/**
	 * Creates a hollow cuboid pointed up along the y-axis
	 * @param center center location
	 * @param length horizontal magnitude of reference plane
	 * @param height magnitude of upwards vector
	 * @param width vertical magnitude of reference plane
	 * @param interval distance between each location
	 */
	public Cuboid(Location center, double length, double height, double width, double interval) {
		this(center, length, height, width, interval, UnitVector.POSITIVE_Y.normal(), true);
	}
	
	/**
	 * Creates a hollow cuboid
	 * @param center center location
	 * @param length horizontal magnitude of reference plane
	 * @param height magnitude of upwards vector
	 * @param width vertical magnitude of reference plane
	 * @param interval distance between each location
	 * @param upwards upwards direction
	 */
	public Cuboid(Location center, double length, double height, double width, double interval, Vector upwards) {
		this(center, length, height, width, interval, upwards, true);
	}
	
	/**
	 * Creates a cuboid
	 * @param center center location
	 * @param length horizontal magnitude of reference plane
	 * @param height magnitude of upwards vector
	 * @param width vertical magnitude of reference plane
	 * @param interval distance between each location
	 * @param upwards upwards direction
	 * @param hollow true to only draw outline
	 */
	public Cuboid(Location center, double length, double height, double width, double interval, Vector upwards, boolean hollow) {
		super(center, upwards, hollow);
		this.length = Math.max(length, 0.1);
		this.height = Math.max(height, 0.1);
		this.width = Math.max(width, 0.1);
		this.interval = Math.max(interval, 0.01);
	}
	
	@Override
	public void construct(Consumer<Location> func) {
		Plane horizontal = Plane.fromPerpendicular(upwards.normalize());
		
		if (hollow) {
			// construct lines along the horizontal plane horizontal axis
			for (double x = -length / 2; x <= length / 2; x += interval) {
				func.accept(horizontal.moveAlongAxes(center.clone(), x, -width / 2).add(upwards.clone().multiply(-height / 2)));
				func.accept(horizontal.moveAlongAxes(center.clone(), x, -width / 2).add(upwards.clone().multiply(height / 2)));
				func.accept(horizontal.moveAlongAxes(center.clone(), x, width / 2).add(upwards.clone().multiply(-height / 2)));
				func.accept(horizontal.moveAlongAxes(center.clone(), x, width / 2).add(upwards.clone().multiply(height / 2)));
			}
			
			// construct lines along the upwards vector
			for (double y = -height / 2; y <= height / 2; y += interval) {
				func.accept(horizontal.moveAlongAxes(center.clone(), -length / 2, -width / 2).add(upwards.clone().multiply(y)));
				func.accept(horizontal.moveAlongAxes(center.clone(), length / 2, -width / 2).add(upwards.clone().multiply(y)));
				func.accept(horizontal.moveAlongAxes(center.clone(), -length / 2, width / 2).add(upwards.clone().multiply(y)));
				func.accept(horizontal.moveAlongAxes(center.clone(), length / 2, width / 2).add(upwards.clone().multiply(y)));
			}
			
			// construct lines along the horizontal plane vertical axis
			for (double z = -width / 2; z <= width / 2; z += interval) {
				func.accept(horizontal.moveAlongAxes(center.clone(), -length / 2, z).add(upwards.clone().multiply(-height / 2)));
				func.accept(horizontal.moveAlongAxes(center.clone(), -length / 2, z).add(upwards.clone().multiply(height / 2)));
				func.accept(horizontal.moveAlongAxes(center.clone(), length / 2, z).add(upwards.clone().multiply(-height / 2)));
				func.accept(horizontal.moveAlongAxes(center.clone(), length / 2, z).add(upwards.clone().multiply(height / 2)));
			}
		} else {
			// construct whole cuboid
			for (double x = -length / 2; x <= length / 2; x += interval) {
				for (double y = -height / 2; y <= height / 2; y += interval) {
					for (double z = -width / 2; z <= width / 2; z += interval) {
						func.accept(horizontal.moveAlongAxes(center.clone(), x, z).add(upwards.clone().multiply(y)));
					}
				}
			}
		}
	}

	public double getLength() {
		return length;
	}

	public void setLength(double length) {
		this.length = length;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	public double getWidth() {
		return width;
	}

	public void setWidth(double width) {
		this.width = width;
	}

	public double getInterval() {
		return interval;
	}

	public void setInterval(double interval) {
		this.interval = interval;
	}
}
