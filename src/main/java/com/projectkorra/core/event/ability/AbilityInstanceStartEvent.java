package com.projectkorra.core.event.ability;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.projectkorra.core.system.ability.AbilityInstance;

public class AbilityInstanceStartEvent extends Event implements Cancellable {

	private static final HandlerList HANDLERS = new HandlerList();
	
	private boolean cancelled = false;
	private AbilityInstance instance;
	
	public AbilityInstanceStartEvent(AbilityInstance instance) {
		this.instance = instance;
	}
	
	public AbilityInstance getInstance() {
		return instance;
	}
	
	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled(boolean cancel) {
		this.cancelled = cancel;
	}

	@Override
	public HandlerList getHandlers() {
		return HANDLERS;
	}

	public static HandlerList getHandlerList() {
		return HANDLERS;
	}
}
