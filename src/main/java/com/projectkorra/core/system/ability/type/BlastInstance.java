package com.projectkorra.core.system.ability.type;

import java.util.function.Predicate;

import org.bukkit.Location;
import org.bukkit.util.Vector;

import com.projectkorra.core.system.ability.AbilityInstance;
import com.projectkorra.core.system.ability.AbilityUser;

public abstract class BlastInstance extends AbilityInstance {

	private Vector direction;
	private Predicate<Location> passable;
	protected Location location;
	
	public BlastInstance(AbilityUser user, Predicate<Location> passable) {
		super(user);
		this.passable = passable;
	}
	
	@Override
	public void onStart() {
		direction = getUser().getDirection();
		location = getUser().getLocation();
	}

	@Override
	public boolean onUpdate() {
		location.add(direction);
		return passable.test(location);
	}
	
	/**
	 * Sets the speed of this blast
	 * @param speed blocks per second
	 */
	protected final void setSpeed(double speed) {
		direction.normalize().multiply(speed / 20);
	}
	
	/**
	 * Sets the direction of this blast without changing the speed
	 * @param direction blast's new direction
	 */
	protected final void setDirection(Vector direction) {
		double speed = this.direction.length();
		this.direction.copy(direction).normalize().multiply(speed);
	}
	
	/**
	 * Turns this blast towards the given direction without changing 
	 * the speed. The length of the given direction will determine how 
	 * much the blast turns toward it (longer = more turning)
	 * @param direction where to turn towards
	 */
	protected final void turnTowards(Vector direction) {
		double speed = this.direction.length();
		this.direction.add(direction).normalize().multiply(speed);
	}
}
