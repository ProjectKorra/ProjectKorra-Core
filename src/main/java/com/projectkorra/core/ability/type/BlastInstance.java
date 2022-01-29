package com.projectkorra.core.ability.type;

import java.util.function.Predicate;

import com.projectkorra.core.ability.Ability;
import com.projectkorra.core.ability.AbilityInstance;
import com.projectkorra.core.ability.AbilityUser;

import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

public abstract class BlastInstance extends AbilityInstance {

	private Vector direction;
	private Predicate<Location> passable;
	protected Location location;
	protected double speed;
	
	public BlastInstance(Ability provider, AbilityUser user, Predicate<Location> passable) {
		super(provider, user);
		this.passable = passable;
	}
	
	@Override
	protected void onStart() {
		direction = getUser().getDirection();
		location = getUser().getEyeLocation();
	}

	@Override
	protected boolean onUpdate(double timeDelta) {
		location.add(direction.multiply(speed * timeDelta));
		return passable.test(location);
	}
	
	@Override
	protected void postUpdate() {
		direction.normalize();
	}
	
	/**
	 * Sets the speed of this blast
	 * @param speed blocks per second
	 */
	protected final void setSpeed(double speed) {
		this.speed = speed;
	}
	
	/**
	 * Sets the direction of this blast without changing the speed
	 * @param direction blast's new direction
	 */
	protected final void setDirection(Vector direction) {
		this.direction.copy(direction).normalize();
	}

	public final Vector getDirection() {
		return this.direction.clone().normalize();
	}
	
	/**
	 * Turns this blast towards the given direction without changing 
	 * the speed. The length of the given direction will determine how 
	 * much the blast turns toward it (longer = more turning)
	 * @param direction where to turn towards
	 */
	protected final void turnTowards(Vector direction) {
		this.direction.add(direction).normalize();
	}

	protected final RayTraceResult rayTrace(double distance, FluidCollisionMode fcm, boolean ignorePassables, double raySize) {
		return location.getWorld().rayTrace(location, direction, distance, fcm, ignorePassables, raySize, null);
	}

	protected final RayTraceResult rayTrace(double distance, FluidCollisionMode fcm, boolean ignorePassables, double raySize, Predicate<Entity> filter) {
		return location.getWorld().rayTrace(location, direction, distance, fcm, ignorePassables, raySize, filter);
	}
}
