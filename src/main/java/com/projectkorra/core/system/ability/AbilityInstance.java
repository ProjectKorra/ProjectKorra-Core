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
	 * Gets the time in millis when this instance
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
	
	public boolean hasStarted() {
		return counter >= 0;
	}
	
	final void start() {
		startTime = System.currentTimeMillis();
		counter = 0;
		onStart();
	}
	
	final void update() {
		onUpdate();
		counter++;
	}
	
	final void stop() {
		onStop();
		startTime = -1;
		counter = -1;
		user = null;
	}
	
	public abstract void onStart();
	public abstract void onUpdate();
	public abstract void onStop();
	public abstract boolean shouldRemove();
	public abstract int getCapacity();
	public abstract Class<? extends Ability> getProvider();
}
