package com.projectkorra.core.system.ability;

public abstract class AbilityInstance {
	
	private AbilityUser user;
	private int counter = -1;
	private long startTime = -1;
	
	public AbilityInstance(AbilityUser user) {
		this.user = user;
	}
	
	/**
	 * Gets the user associated with this instance
	 * @return user
	 */
	public final AbilityUser getUser() {
		return user;
	}
	
	/**
	 * Gets how many ticks have passed since this
	 * instance was started, will return {@code -1}
	 * if called before the instance has started
	 * @return ticks since starting
	 */
	public final int ticksLived() {
		return counter;
	}
	
	/**
	 * Gets the time in milliseconds when this instance
	 * was started, will return {@code -1} if called
	 * before the instance has started
	 * @return instance start time
	 */
	public final long startTime() {
		return startTime;
	}
	
	/**
	 * Gets the time in millis since this instance
	 * was started
	 * @return instance duration
	 */
	public final long timeLived() {
		return System.currentTimeMillis() - startTime;
	}
	
	/**
	 * Return whether or not this instance has been started
	 * @return has instance started
	 */
	public final boolean hasStarted() {
		return startTime >= 0;
	}
	
	final void start() {
		startTime = System.currentTimeMillis();
		onStart();
	}
	
	final boolean update() {
		++counter;
		return onUpdate();
	}
	
	final void stop() {
		onStop();
		startTime = -1;
		counter = -1;
		user = null;
	}
	
	/**
	 * Method called when instance is started
	 */
	public abstract void onStart();
	
	/**
	 * Method called when instance is updated
	 */
	public abstract boolean onUpdate();
	
	/**
	 * Method called when instance is stopped
	 */
	public abstract void onStop();
	
	/**
	 * How many instances of this can be active for a single player
	 * @return instance capacity per player
	 */
	public abstract int getCapacity();
}
