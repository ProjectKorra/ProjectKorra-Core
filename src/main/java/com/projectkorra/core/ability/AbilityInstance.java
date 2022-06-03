package com.projectkorra.core.ability;

import com.projectkorra.core.ability.attribute.Attribute;
import com.projectkorra.core.util.data.RemovalPolicy;

public abstract class AbilityInstance {

	//QoL copies of common attributes
	protected static final String SPEED = Attribute.SPEED;
	protected static final String RANGE = Attribute.RANGE;
	protected static final String SELECT_RANGE = Attribute.SELECT_RANGE;
	protected static final String DAMAGE = Attribute.DAMAGE;
	protected static final String COOLDOWN = Attribute.COOLDOWN;
	protected static final String DURATION = Attribute.DURATION;
	protected static final String RADIUS = Attribute.RADIUS;
	protected static final String CHARGE_TIME = Attribute.CHARGE_TIME;
	protected static final String WIDTH = Attribute.WIDTH;
	protected static final String HEIGHT = Attribute.HEIGHT;
	protected static final String KNOCKBACK = Attribute.KNOCKBACK;
	protected static final String KNOCKUP = Attribute.KNOCKUP;
	protected static final String FIRE_TICK = Attribute.FIRE_TICK;

	protected final AbilityUser user;
	protected final Ability provider;
	protected final RemovalPolicy removal = RemovalPolicy.EMPTY;
	private int counter = -1;
	private long startTime = -1;

	public AbilityInstance(Ability provider, AbilityUser user) {
		this.user = user;
		this.provider = provider;
	}

	public final Ability getProvider() {
		return provider;
	}

	/**
	 * Gets the user associated with this instance
	 * 
	 * @return user
	 */
	public final AbilityUser getUser() {
		return user;
	}

	/**
	 * Gets how many ticks have passed since this instance was started, will return
	 * {@code -1} if called before the instance has started
	 * 
	 * @return ticks since starting
	 */
	public final int ticksLived() {
		return counter;
	}

	/**
	 * Gets the time in milliseconds when this instance was started, will return
	 * {@code -1} if called before the instance has started
	 * 
	 * @return instance start time
	 */
	public final long startTime() {
		return startTime;
	}

	/**
	 * Gets the time in millis since this instance was started
	 * 
	 * @return instance duration
	 */
	public final long timeLived() {
		return System.currentTimeMillis() - startTime;
	}

	/**
	 * Return whether or not this instance has been started
	 * 
	 * @return has instance started
	 */
	public final boolean hasStarted() {
		return startTime >= 0;
	}

	public boolean hasUpdate() {
		return true;
	}

	public int interpolationInterval() {
		return 3;
	}

	final boolean start() {
		startTime = System.currentTimeMillis();
		return onStart();
	}

	final boolean update(double timeDelta) {
		++counter;
		
		if (removal.test(this)) {
			return false;
		}
		
		return onUpdate(timeDelta);
	}

	final void stop() {
		onStop();
		startTime = -1;
		counter = -1;
	}

	/**
	 * Method called when the instance is started
	 * @return true if ability can successfully start
	 */
	protected abstract boolean onStart();

	/**
	 * Method called to update the instance
	 * 
	 * @param timeDelta the time difference between update calls, in seconds
	 * @return false to stop updating
	 */
	protected abstract boolean onUpdate(double timeDelta);

	protected abstract void preUpdate();

	/**
	 * Method called after instance has been successfully updated
	 */
	protected abstract void postUpdate();

	/**
	 * Method called when instance is stopped
	 */
	protected abstract void onStop();

	/**
	 * Gets the name associated with this instance
	 * 
	 * @return name of the instance
	 */
	public abstract String getName();
}
