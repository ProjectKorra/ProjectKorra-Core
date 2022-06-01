package com.projectkorra.core.event.ability;

import com.projectkorra.core.ability.AbilityInstance;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.util.Vector;

public class InstanceMoveEntityEvent extends Event implements Cancellable {

	private static final HandlerList HANDLERS = new HandlerList();

	private boolean cancelled = false;
	private LivingEntity target;
	private Vector direction;
	private boolean knockback, resetFallDistance;
	private AbilityInstance source;

	public InstanceMoveEntityEvent(LivingEntity target, Vector direction, AbilityInstance source, boolean knockback, boolean resetFallDistance) {
		this.target = target;
		this.direction = direction;
		this.source = source;
		this.knockback = knockback;
		this.resetFallDistance = resetFallDistance;
	}

	public AbilityInstance getInstance() {
		return source;
	}

	public LivingEntity getTarget() {
		return target;
	}

	public Vector getDirection() {
		return direction;
	}

	public boolean isKnockback() {
		return knockback;
	}

	public boolean doesResetFallDistance() {
		return resetFallDistance;
	}

	public void doesResetFallDistance(boolean resetFallDistance) {
		this.resetFallDistance = resetFallDistance;
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
