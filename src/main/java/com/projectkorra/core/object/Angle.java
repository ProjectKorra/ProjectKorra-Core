package com.projectkorra.core.object;

public class Angle {

	public static enum AngleMode {
		DEGREES, RADIANS;
	}
	
	private AngleMode mode;
	private double value;
	
	private Angle(AngleMode mode, double value) {
		this.mode = mode;
		this.value = value;
	}
	
	public AngleMode getMode() {
		return mode;
	}
	
	public double getValue() {
		return value;
	}
	
	public double getValueInRadians() {
		if (mode == AngleMode.DEGREES) {
			return Math.toRadians(value);
		} else {
			return value;
		}
	}
	
	public double getValueInDegrees() {
		if (mode == AngleMode.RADIANS) {
			return Math.toDegrees(value);
		} else {
			return value;
		}
	}
	
	public static Angle radians(double value) {
		return new Angle(AngleMode.RADIANS, value);
	}
	
	public static Angle degrees(double value) {
		return new Angle(AngleMode.DEGREES, value);
	}
}
