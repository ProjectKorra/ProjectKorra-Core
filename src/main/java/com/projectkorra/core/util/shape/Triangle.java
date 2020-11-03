package com.projectkorra.core.util.shape;

import java.util.function.Consumer;

import org.bukkit.Location;

import com.projectkorra.core.util.math.Plane;

public class Triangle extends Polygon {

	private double height, length, interval;
	
	/**
	 * Creates a hollow triangle in the XZ plane	
	 * @param center center location
	 * @param height vertical magnitude in the reference plane
	 * @param length horizontal magnitude in the reference plane
	 * @param interval distance between each location in the triangle
	 */
	public Triangle(Location center, double height, double length, double interval) {
		this(center, height, length, interval, Plane.XZ, true);
	}
	
	/**
	 * Creates a hollow triangle
	 * @param center center location
	 * @param height vertical magnitude in the reference plane
	 * @param length horizontal magnitude in the reference plane
	 * @param interval distance between each location in the triangle
	 * @param reference plane the triangle exists within
	 */
	public Triangle(Location center, double height, double length, double interval, Plane reference) {
		this(center, height, length, interval, reference, true);
	}
	
	/**
	 * Creates a triangle
	 * @param center center location
	 * @param height vertical magnitude in the reference plane
	 * @param length horizontal magnitude in the reference plane
	 * @param interval distance between each location in the triangle
	 * @param reference plane the triangle exists within
	 * @param hollow true if only drawing the outline
	 */
	public Triangle(Location center, double height, double length, double interval, Plane reference, boolean hollow) {
		super(center, reference, hollow);
		this.height = Math.max(height, 0.1);
		this.length = Math.max(length, 0.1);
		this.interval = Math.max(interval, 0.01);
	}
	
	@Override
	public void construct(Consumer<Location> func) {
		if (hollow) {
			// construct outline locations
			for (double x = -length / 2; x <= length / 2; x += interval) {
				func.accept(reference.moveAlongAxes(center.clone(), x, -height / 2));
				func.accept(reference.moveAlongAxes(center.clone(), x, (-height / 2) + (height) * (1 - Math.abs(x) / (length / 2))));
			}
		} else {
			// construct all locations
			for (double x = -length / 2; x <= length / 2; x += interval) {
				double yBound = (-height / 2) + (height) * (1 - Math.abs(x) / (length / 2));
				for (double y = -height / 2; y <= yBound; y += interval) {
					func.accept(reference.moveAlongAxes(center.clone(), x, y));
				}
			}
		}
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	public double getLength() {
		return length;
	}

	public void setLength(double length) {
		this.length = length;
	}

	public double getInterval() {
		return interval;
	}

	public void setInterval(double interval) {
		this.interval = interval;
	}
}
