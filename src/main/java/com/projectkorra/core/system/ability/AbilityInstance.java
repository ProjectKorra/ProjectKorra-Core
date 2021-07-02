package com.projectkorra.core.system.ability;

public abstract class AbilityInstance {
	
	protected final AbilityUser user;
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
	
	final boolean update(double timeDelta) {
		++counter;
		return onUpdate(timeDelta);
	}
	
	final void stop() {
		onStop();
		startTime = -1;
		counter = -1;
	}
	
	/**
	 * Method called when instance is started
	 */
	protected abstract void onStart();
	
	/**
	 * Method called to update the instance
	 * @param timeDelta the time difference between update calls, in seconds
	 * @return false to stop updating
	 */
	protected abstract boolean onUpdate(double timeDelta);
	
	/**
	 * Method called after instance has been successfully updated
	 */
	protected abstract void postUpdate();
	
	/**
	 * Method called when instance is stopped
	 */
	protected abstract void onStop();
	
	/**
	 * How many instances of this can be active for a single player
	 * @return instance capacity per player
	 */
	public abstract int getCapacity();
}
