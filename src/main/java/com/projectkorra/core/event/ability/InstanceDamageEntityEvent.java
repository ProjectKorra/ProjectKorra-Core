package com.projectkorra.core.event.ability;

import com.projectkorra.core.system.ability.AbilityInstance;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class InstanceDamageEntityEvent extends Event implements Cancellable {

	private static final HandlerList HANDLERS = new HandlerList();
	
	private boolean cancelled = false;
	private LivingEntity target;
	private double damage;
	private AbilityInstance source;
	private boolean ignoreArmor;
	
	public InstanceDamageEntityEvent(LivingEntity target, double damage, AbilityInstance source, boolean ignoreArmor) {
		this.target = target;
		this.damage = damage;
		this.source = source;
		this.ignoreArmor = ignoreArmor;
	}
	
	public LivingEntity getTarget() {
		return target;
	}
	
	public double getDamage() {
		return damage;
	}
	
	public AbilityInstance getInstance() {
		return source;
	}
	
	public boolean doesIgnoreArmor() {
		return ignoreArmor;
	}
	
	public void setDamage(double damage) {
		this.damage = damage;
	}
	
	public void setIgnoreArmor(boolean ignoreArmor) {
		this.ignoreArmor = ignoreArmor;
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
