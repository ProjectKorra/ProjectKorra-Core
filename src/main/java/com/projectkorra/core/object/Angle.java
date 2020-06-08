package com.projectkorra.core.object;

public class Angle {

	public static enum AngleMode {
		DEGREES (Math.PI / 180), 
		RADIANS (180 / Math.PI);
		
		private double conversion;
		private AngleMode(double conversion) {
			this.conversion = conversion;
		}
		
		public double getConversion() {
			return conversion;
		}
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
	
	public static Angle radians(double value) {
		return new Angle(AngleMode.RADIANS, value);
	}
	
	public static Angle degrees(double value) {
		return new Angle(AngleMode.DEGREES, value);
	}
}
