package com.projectkorra.core.collision;

import org.bukkit.Location;

public class Collision {

	private Collidable first, second;
	private CollisionOperator type;
	
	Collision(Collidable first, Collidable second, CollisionOperator type) {
		this.first = first;
		this.second = second;
		this.type = type;
	}
	
	public Collidable getFirst() {
		return first;
	}
	
	public Collidable getSecond() {
		return second;
	}
	
	public CollisionOperator getType() {
		return type;
	}
	
	public Location getCenterOfMass() {
		return first.getLocation().clone().add(second.getLocation()).multiply(0.5);
	}
}
