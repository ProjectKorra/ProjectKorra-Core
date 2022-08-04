package com.projectkorra.core.event.ability;

import com.projectkorra.core.ability.AbilityInstance;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class InstanceIgniteEntityEvent extends Event implements Cancellable {

	private static final HandlerList HANDLERS = new HandlerList();

	private boolean cancelled = false;
	private LivingEntity target;
	private int fireTicks;
	private AbilityInstance source;

	public InstanceIgniteEntityEvent(LivingEntity target, int fireTicks, AbilityInstance source) {
		this.target = target;
		this.fireTicks = fireTicks;
		this.source = source;
	}

	public LivingEntity getTarget() {
		return target;
	}

	public int getFireTicks() {
		return fireTicks;
	}

	public void setFireTicks(int fireTicks) {
		this.fireTicks = fireTicks;
	}

	public AbilityInstance getInstance() {
		return source;
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
