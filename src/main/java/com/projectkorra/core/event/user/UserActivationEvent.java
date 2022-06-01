package com.projectkorra.core.event.user;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.projectkorra.core.ability.AbilityUser;
import com.projectkorra.core.ability.activation.Activation;

public class UserActivationEvent extends Event implements Cancellable {

	private static final HandlerList HANDLERS = new HandlerList();

	private boolean cancelled = false;
	private AbilityUser user;
	private Activation trigger;
	private Event provider;

	public UserActivationEvent(AbilityUser user, Activation trigger, Event provider) {
		this.user = user;
		this.trigger = trigger;
		this.provider = provider;
	}

	public AbilityUser getUser() {
		return user;
	}

	public Activation getTrigger() {
		return trigger;
	}

	public Event getProvider() {
		return provider;
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

	public HandlerList getHandlerList() {
		return HANDLERS;
	}
}
