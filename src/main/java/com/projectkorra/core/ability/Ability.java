package com.projectkorra.core.ability;

public abstract class Ability {
	private boolean isStarted = false;
	private boolean isStopped = false;
	private int priority = 0;

	public Ability(int priority) {
		this.priority = priority;
	}

	public Ability start() {
		
		// add default start conditions; 
		isStarted = true;
		return this;
	}
	public Ability progress() {
		// add default stuff to check for early exit
		
		return this;
	}
	public Ability stop() {
		// add default removal stuff;
		
		isStopped = true;
		return this;
	}
	public boolean isStarted() {
		return isStarted;
	}
	public boolean isStopped() {
		return isStopped;
	}

	public int getPriority() {
		return priority;
	}
}
