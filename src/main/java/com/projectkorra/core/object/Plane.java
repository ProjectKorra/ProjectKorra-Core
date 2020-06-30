package com.projectkorra.core.object;

import org.apache.commons.lang.Validate;
import org.bukkit.Location;
import org.bukkit.util.Vector;

import com.projectkorra.core.enums.Dimension;
import com.projectkorra.core.util.VectorUtil;

public class Plane {

	public static final Plane XY = new Plane(Dimension.X.getAxis(), Dimension.Y.getAxis());
	public static final Plane ZY = new Plane(Dimension.Z.getAxis(), Dimension.Y.getAxis());
	public static final Plane XZ = new Plane(Dimension.X.getAxis(), Dimension.Z.getAxis());

	private Vector horizontal, vertical;

	private Plane(Vector horizontal, Vector vertical) {
		this.horizontal = horizontal;
		this.vertical = vertical;
	}

	public Vector getHorizontalAxis() {
		return horizontal.normalize().clone();
	}

	public Vector getVerticalAxis() {
		return vertical.normalize().clone();
	}
	
	public Location moveAlongAxes(Location position, double rightward, double upward) {
		return position.add(horizontal.normalize().multiply(rightward)).add(vertical.normalize().multiply(upward));
	}

	public static Plane fromPerpendicular(Vector perpendicular) {
		Validate.isTrue(!perpendicular.equals(VectorUtil.ZERO), "Perpendicular vector cannot be zero");

		Vector horizontal = VectorUtil.orthogonal(new Location(null, 0, 0, 0).setDirection(perpendicular), 1, Angle.radians(0));
		Vector vertical = VectorUtil.rotate(horizontal.clone(), perpendicular, Angle.radians(Math.PI / 2));

		return new Plane(horizontal, vertical);
	}
}
