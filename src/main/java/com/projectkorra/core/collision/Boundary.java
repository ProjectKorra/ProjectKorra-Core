package com.projectkorra.core.collision;

import org.bukkit.Location;

public class Boundary {

	private Location center;
	private double length, height, width;
	
	public Boundary(Location center, double length, double height, double width) {
		this.center = center;
		this.length = length;
		this.height = height;
		this.width = width;
	}
	
	public Location getCenter() {
		return center;
	}
	
	public double getLength() {
		return length;
	}
	
	public double getHeight() {
		return height;
	}
	
	public double getWidth() {
		return width;
	}
	
	public boolean contains(Location loc) {
		return loc.getX() >= center.getX() - length / 2 && loc.getX() <= center.getX() + length / 2
			&& loc.getY() >= center.getY() - height / 2 && loc.getY() <= center.getY() + height / 2
			&& loc.getZ() >= center.getZ() - width / 2 && loc.getZ() <= center.getZ() + width / 2;
	}
	
	public boolean intersects(Boundary other) {
		return !(center.getX() - length / 2 > other.center.getX() + other.length / 2 || 
				center.getX() + length / 2 < other.center.getX() - other.length / 2 || 
				center.getY() - height / 2 > other.center.getY() + other.height / 2 ||
				center.getY() + height / 2 < other.center.getY() - other.height / 2 ||
				center.getZ() - width / 2 > other.center.getZ() + other.width / 2 || 
				center.getZ() + width / 2 < other.center.getZ() - other.width / 2);
	}
}
