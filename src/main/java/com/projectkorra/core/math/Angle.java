package com.projectkorra.core.math;

public class Angle {

	private AngleMode mode;
	private double value;

	private Angle(AngleMode mode, double value) {
		this.mode = mode;
		this.value = value;
	}

	public AngleMode getMode() {
		return mode;
	}

	public double getRawValue() {
		return value;
	}

	public double getValue(AngleMode mode) {
		if (this.mode != mode) {
			return value * mode.getConversion();
		} else {
			return value;
		}
	}
	
	public void set(AngleMode mode, double value) {
		this.mode = mode;
		this.value = value;
	}
	
	public void setValue(double value) {
		this.value = value;
	}

	public static Angle radians(int value) {
		return new Angle(AngleMode.RADIANS, (double) value);
	}

	public static Angle radians(float value) {
		return new Angle(AngleMode.RADIANS, (double) value);
	}

	public static Angle radians(double value) {
		return new Angle(AngleMode.RADIANS, value);
	}

	public static Angle degrees(int value) {
		return new Angle(AngleMode.DEGREES, (double) value);
	}

	public static Angle degrees(float value) {
		return new Angle(AngleMode.DEGREES, (double) value);
	}

	public static Angle degrees(double value) {
		return new Angle(AngleMode.DEGREES, value);
	}

	public static enum AngleMode {
		DEGREES (180 / Math.PI), 
		RADIANS (Math.PI / 180);

		private double conversion;
		private AngleMode(double conversion) {
			this.conversion = conversion;
		}

		public double getConversion() {
			return conversion;
		}
	}
}
