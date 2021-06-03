package com.projectkorra.core.event.user;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.projectkorra.core.system.ability.AbilityInstance;
import com.projectkorra.core.system.ability.AbilityUser;

public class UserDamageEntityEvent extends Event implements Cancellable {

	private static final HandlerList HANDLERS = new HandlerList();
	
	private boolean cancelled = false;
	private AbilityUser user;
	private LivingEntity target;
	private double damage;
	private boolean ignoreArmor;
	private AbilityInstance source;
	
	public UserDamageEntityEvent(AbilityUser user, LivingEntity target, double damage, boolean ignoreArmor, AbilityInstance source) {
		this.user = user;
		this.target = target;
		this.damage = damage;
		this.ignoreArmor = ignoreArmor;
		this.source = source;
	}
	
	public AbilityUser getUser() {
		return user;
	}
	
	public LivingEntity getTarget() {
		return target;
	}
	
	public double getDamage() {
		return damage;
	}
	
	public boolean doesIgnoreArmor() {
		return ignoreArmor;
	}
	
	public AbilityInstance getSource() {
		return source;
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
