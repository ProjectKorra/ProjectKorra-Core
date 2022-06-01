package com.projectkorra.core.util.math;

public abstract class AngleType {

	public static final AngleType RADIANS = new AngleType() {

		@Override
		public double toRadians(double angle) {
			return angle;
		}

		@Override
		public double fromRadians(double angle) {
			return angle;
		}

	};

	public static final AngleType DEGREES = new AngleType() {

		@Override
		public double toRadians(double angle) {
			return angle * Math.PI / 180;
		}

		@Override
		public double fromRadians(double angle) {
			return angle * 180 / Math.PI;
		}

	};

	/**
	 * Converts the given angle from the given angle type to radians
	 * 
	 * @param angle value of the angle to convert
	 * @return radian value of the angle
	 */
	public abstract double toRadians(double angle);

	/**
	 * Converts the given radians to the this angle type
	 * 
	 * @param angle value of the radians angle
	 * @return angle value of this type
	 */
	public abstract double fromRadians(double angle);

	/**
	 * Converts the given angle from this type to the given other type
	 * 
	 * @param other the other angle type to convert to
	 * @param angle value of the angle to convert
	 * @return angle of the other value type
	 */
	public final double convertTo(AngleType other, double angle) {
		return other.fromRadians(toRadians(angle));
	}
}
