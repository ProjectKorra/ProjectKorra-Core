package com.projectkorra.core.ability;

public abstract class Ability {
	private boolean started = false;
	private boolean stopped = false;
	private long startTime;
	protected BendingUser user;
	private int priority = 0;

	public Ability(BendingUser user, int priority) {
		this.user = user;
		this.priority = priority;
	}
	
	public final void start() {
		if(!started) {
			startTime = System.currentTimeMillis();
			started = true;
			this.onStart();
		}
	}

	public final void tick() {
		if(started && !stopped)
			this.progress();
	}

	public final void remove() {
		if(!stopped) {
			stopped = true;
			this.onRemove();
		}
	}

	public abstract void onStart();
	public abstract void progress();
	public abstract void onRemove();


	public boolean started() {
		return started;
	}
	public boolean stopped() {
		return stopped;
	}

	public int getPriority() {
		return priority;
	}

	public long getStartTime() {
		return startTime;
	}

	public BendingUser getUser() {
		return user;
	}
}
