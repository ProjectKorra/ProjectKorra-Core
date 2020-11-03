package com.projectkorra.core.util.shape;

import java.util.function.Consumer;

import org.bukkit.Location;

import com.projectkorra.core.util.math.Plane;

public class Rectangle extends Polygon {

	private double length, height, interval;
	
	/**
	 * Creates a hollow rectangle in the XZ plane
	 * @param center center location
	 * @param sides magnitude of length and height
	 * @param interval distance between each location
	 */
	public Rectangle(Location center, double sides, double interval) {
		this(center, sides, sides, interval, Plane.XZ, true);
	}
	
	/**
	 * Creates a hollow rectangle in the XZ plane
	 * @param center center location
	 * @param length horizontal magnitude in the reference plane
	 * @param height vertical magnitude in the reference plane
	 * @param interval distance between each location
	 */
	public Rectangle(Location center, double length, double height, double interval) {
		this(center, length, height, interval, Plane.XZ, true);
	}
	
	/**
	 * Creates a hollow rectangle
	 * @param center center location
	 * @param sides magnitude of length and height
	 * @param interval distance between each location
	 * @param reference plane the rectangle exists in
	 */
	public Rectangle(Location center, double sides, double interval, Plane reference) {
		this(center, sides, sides, interval, reference, true);
	}
	
	/**
	 * Creates a rectangle
	 * @param center center location
	 * @param sides magnitude of length and height
	 * @param interval distance between each location
	 * @param reference plane the rectangle exists in
	 * @param hollow true to only draw outline
	 */
	public Rectangle(Location center, double sides, double interval, Plane reference, boolean hollow) {
		this(center, sides, sides, interval, reference, hollow);
	}
	
	/**
	 * Creates a hollow rectangle
	 * @param center center location
	 * @param length horizontal magnitude in the reference plane
	 * @param height vertical magnitude in the reference plane
	 * @param interval distance between each location
	 * @param reference plane the rectangle exists in
	 */
	public Rectangle(Location center, double length, double height, double interval, Plane reference) {
		this(center, length, height, interval, reference, true);
	}
	
	/**
	 * Creates a rectangle
	 * @param center center location
	 * @param length horizontal magnitude in the reference plane
	 * @param height vertical magnitude in the reference plane
	 * @param interval distance between each location
	 * @param reference plane the rectangle exists in
	 * @param hollow true to only draw outline
	 */
	public Rectangle(Location center, double length, double height, double interval, Plane reference, boolean hollow) {
		super(center, reference, hollow);
		this.length = Math.max(length, 0.1);
		this.height = Math.max(height, 0.1);
		this.interval = Math.max(interval, 0.01);
	}
	
	@Override
	public void construct(Consumer<Location> func) {
		if (hollow) {
			// construct sides along reference plane horizontal axis
			for (double x = -length / 2; x <= length / 2; x += interval) {
				func.accept(reference.moveAlongAxes(center.clone(), x, height / 2));
				func.accept(reference.moveAlongAxes(center.clone(), x, -height / 2));
			}
			
			// construct sides along reference plane vertical axis
			for (double z = -length / 2; z <= length / 2; z += interval) {
				func.accept(reference.moveAlongAxes(center.clone(), length / 2, z));
				func.accept(reference.moveAlongAxes(center.clone(), -length / 2, z));
			}
		} else {
			// construct all locations within reference plane
			for (double i = -length / 2; i <= length / 2; i += interval) {
				for (double j = -height / 2; j <= height / 2; j += interval) {
					func.accept(reference.moveAlongAxes(center.clone(), i, j));
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

	public double getInterval() {
		return interval;
	}

	public void setInterval(double interval) {
		this.interval = interval;
	}
}
