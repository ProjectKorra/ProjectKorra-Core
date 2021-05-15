package com.projectkorra.core.event.entity;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.util.Vector;

import com.projectkorra.core.system.ability.AbilityInstance;
import com.projectkorra.core.system.ability.AbilityUser;

public class UserKnockbackEntityEvent extends Event implements Cancellable {

	private static final HandlerList HANDLERS = new HandlerList();
	
	private boolean cancelled = false;
	private AbilityUser user;
	private LivingEntity target;
	private Vector direction;
	private boolean resetFallDistance;
	private AbilityInstance source;
	
	public UserKnockbackEntityEvent(AbilityUser user, LivingEntity target, Vector direction, boolean resetFallDistance, AbilityInstance source) {
		this.user = user;
		this.target = target;
		this.direction = direction;
		this.resetFallDistance = resetFallDistance;
		this.source = source;
	}
	
	public AbilityUser getUser() {
		return user;
	}
	
	public AbilityInstance getSource() {
		return source;
	}
	
	public LivingEntity getTarget() {
		return target;
	}
	
	public Vector getDirection() {
		return direction;
	}
	
	public boolean doesResetFallDistance() {
		return resetFallDistance;
	}
	
	public void setResetFallDistance(boolean resetFallDistance) {
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
