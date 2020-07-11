package com.projectkorra.core.util.shape;

import java.util.function.Consumer;

import org.bukkit.Location;
import org.bukkit.util.Vector;

import com.projectkorra.core.util.math.Plane;
import com.projectkorra.core.util.math.UnitVector;

public class Tetrahedron extends Polyhedron {

	private double length, height, width, interval;
	
	/**
	 * Creates hollow a tetrahedron (pyramid) using the y-axis for upwards direction
	 * @param center center location
	 * @param sides magnitude of the length, height, and width
	 * @param interval distance between each location
	 */
	public Tetrahedron(Location center, double sides, double interval) {
		this(center, sides, sides, sides, interval, UnitVector.POSITIVE_Y.normal(), true);
	}
	
	/**
	 * Creates hollow a tetrahedron (pyramid)
	 * @param center center location
	 * @param sides magnitude of the length, height, and width
	 * @param interval distance between each location
	 * @param upwards upwards direction for this shape, used to construct the perpendicular plane
	 */
	public Tetrahedron(Location center, double sides, double interval, Vector upwards) {
		this(center, sides, sides, sides, interval, upwards, true);
	}
	
	/**
	 * Creates a tetrahedron (pyramid)
	 * @param center center location
	 * @param sides magnitude of the length, height, and width
	 * @param interval distance between each location
	 * @param upwards upwards direction for this shape, used to construct the perpendicular plane
	 * @param hollow true to only draw the outline
	 */
	public Tetrahedron(Location center, double sides, double interval, Vector upwards, boolean hollow) {
		this(center, sides, sides, sides, interval, upwards, hollow);
	}
	
	/**
	 * Creates a hollow tetrahedron (pyramid) using the y-axis for upwards direction
	 * @param center center location
	 * @param length horizontal magnitude in the reference plane
	 * @param height magnitude of the upwards vector
	 * @param width vertical magnitude in the reference plane
	 * @param interval distance between each location
	 */
	public Tetrahedron(Location center, double length, double height, double width, double interval) {
		this(center, length, height, width, interval, UnitVector.POSITIVE_Y.normal(), true);
	}
	
	/**
	 * Creates a hollow tetrahedron (pyramid)
	 * @param center center location
	 * @param length horizontal magnitude in the reference plane
	 * @param height magnitude of the upwards vector
	 * @param width vertical magnitude in the reference plane
	 * @param interval distance between each location
	 * @param upwards upwards direction for this shape, used to construct the perpendicular plane
	 */
	public Tetrahedron(Location center, double length, double height, double width, double interval, Vector upwards) {
		this(center, length, height, width, interval, upwards, true);
	}
	
	/**
	 * Creates a tetrahedron (pyramid)
	 * @param center center location
	 * @param length horizontal magnitude in the reference plane
	 * @param height magnitude of the upwards vector
	 * @param width vertical magnitude in the reference plane
	 * @param interval distance between each location
	 * @param upwards upwards direction for this shape, used to construct the perpendicular plane
	 * @param hollow true to only draw the outline
	 */
	public Tetrahedron(Location center, double length, double height, double width, double interval, Vector upwards, boolean hollow) {
		super(center, upwards, hollow);
		this.length = Math.max(length, 0.1);
		this.height = Math.max(height, 0.1);
		this.width = Math.max(width, 0.1);
		this.interval = Math.max(interval, 0.01);
	}
	
	@Override
	public void construct(Consumer<Location> func) {
		Plane horizontal = Plane.fromPerpendicular(upwards.normalize()); // horizontal reference plane
		
		if (hollow) {
			// construct triangle base
			for (double x = -length / 2; x <= length / 2 + interval / 2; x += interval) {
				func.accept(horizontal.moveAlongAxes(center.clone(), x, -width / 2).add(upwards.clone().multiply(height / 2)));
				func.accept(horizontal.moveAlongAxes(center.clone(), x, (-width / 2) + (width) * (1 - Math.abs(x) / (length / 2))).add(upwards.clone().multiply(height / 2)));
			}
			
			// construct face outlines
			for (double y = -height / 2; y <= height / 2; y += interval) {
				double percent = (y + height / 2) / height;
				double xv = percent * length / 2;
				double zv = percent * width / 2;
				func.accept(horizontal.moveAlongAxes(center.clone(), 0, zv).add(upwards.clone().multiply(y)));
				func.accept(horizontal.moveAlongAxes(center.clone(), xv, -zv).add(upwards.clone().multiply(y)));
				func.accept(horizontal.moveAlongAxes(center.clone(), -xv, -zv).add(upwards.clone().multiply(y)));
			}
		} else {
			// construct filled triangles at each height interval
			for (double y = -height / 2; y <= height / 2; y += interval) {
				double percent = (y + height / 2) / height;
				double xv = percent * length;
				double zv = percent * width;
				
				// construct filled triangle for this height interval
				for (double x = -xv / 2; x <= xv / 2; x += interval) {
					double zBound = (-zv / 2) + zv * (1 - Math.abs(x) / (xv / 2));
					
					for (double z = -zv / 2; z <= zBound; z += interval) {
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
