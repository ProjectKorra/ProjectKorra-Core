package com.projectkorra.core.util.shape;

import java.util.function.Consumer;

import org.bukkit.Location;

import com.projectkorra.core.util.math.Plane;

public class Rectangle extends Polygon {

	private double length, height, interval;
	
	public Rectangle(Location center, double sides, double interval) {
		this(center, sides, sides, interval, Plane.XZ, true);
	}
	
	public Rectangle(Location center, double length, double height, double interval) {
		this(center, length, height, interval, Plane.XZ, true);
	}
	
	public Rectangle(Location center, double sides, double interval, Plane reference) {
		this(center, sides, sides, interval, reference, true);
	}
	
	public Rectangle(Location center, double sides, double interval, Plane reference, boolean hollow) {
		this(center, sides, sides, interval, reference, hollow);
	}
	
	public Rectangle(Location center, double length, double height, double interval, Plane reference) {
		this(center, length, height, interval, reference, true);
	}
	
	public Rectangle(Location center, double length, double height, double interval, Plane reference, boolean hollow) {
		super(center, reference, hollow);
		this.length = Math.max(length, 0.1);
		this.height = Math.max(height, 0.1);
		this.interval = Math.max(interval, 0.01);
	}
	
	@Override
	public void construct(Consumer<Location> func) {
		if (hollow) {
			for (double x = -length / 2; x <= length / 2; x += interval) {
				func.accept(reference.moveAlongAxes(center.clone(), x, height / 2));
				func.accept(reference.moveAlongAxes(center.clone(), x, -height / 2));
			}
			
			for (double z = -length / 2; z <= length / 2; z += interval) {
				func.accept(reference.moveAlongAxes(center.clone(), length / 2, z));
				func.accept(reference.moveAlongAxes(center.clone(), -length / 2, z));
			}
		} else {
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
