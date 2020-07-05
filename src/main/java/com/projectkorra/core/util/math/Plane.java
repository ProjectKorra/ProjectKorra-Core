package com.projectkorra.core.util.math;

import org.apache.commons.lang.Validate;
import org.bukkit.Location;
import org.bukkit.util.Vector;

import com.projectkorra.core.util.VectorUtil;

public class Plane {
	
	public static final Plane XY = new Plane(UnitVector.POSITIVE_X.normal(), UnitVector.POSITIVE_Y.normal());
	public static final Plane ZY = new Plane(UnitVector.POSITIVE_Z.normal(), UnitVector.POSITIVE_Y.normal());
	public static final Plane XZ = new Plane(UnitVector.POSITIVE_X.normal(), UnitVector.POSITIVE_Z.normal());

	private Vector horizontal, vertical;

	private Plane(Vector horizontal, Vector vertical) {
		this.horizontal = horizontal.clone().normalize();
		this.vertical = vertical.clone().normalize();
	}

	public Vector getHorizontalAxis() {
		return horizontal.clone();
	}

	public Vector getVerticalAxis() {
		return vertical.clone();
	}

	public Location moveAlongAxes(Location position, double rightward, double upward) {
		return position.add(horizontal.clone().multiply(rightward)).add(vertical.clone().multiply(upward));
	}

	public static Plane fromPerpendicular(Vector perpendicular) throws IllegalArgumentException {
		Validate.isTrue(!perpendicular.equals(UnitVector.ZERO.normal()), "Perpendicular vector cannot be zero");

		Vector horizontal = VectorUtil.orthogonal(perpendicular, 1, Angle.radians(0));
		Vector vertical = VectorUtil.orthogonal(perpendicular, 1, Angle.degrees(90));

		return new Plane(horizontal, vertical);
	}
}
