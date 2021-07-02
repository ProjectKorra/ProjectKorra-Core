package com.projectkorra.core.event.ability;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.projectkorra.core.system.ability.AbilityInstance;

public class AbilityInstanceStopEvent extends Event {
	
	public static enum Reason {
		/**
		 * When an ability stops under its own logic
		 */
		NATURAL,
		
		/**
		 * When an ability stops from external logic
		 */
		FORCED;
	}

	private static final HandlerList HANDLERS = new HandlerList();

	private AbilityInstance instance;
	private Reason reason;
	
	public AbilityInstanceStopEvent(AbilityInstance instance, Reason reason) {
		this.instance = instance;
		this.reason = reason;
	}
	
	public AbilityInstance getInstance() {
		return instance;
	}

	public Reason getReason() {
		return reason;
	}
	
	@Override
	public HandlerList getHandlers() {
		return HANDLERS;
	}
	
	public static HandlerList getHandlerList() {
		return HANDLERS;
	}
}
