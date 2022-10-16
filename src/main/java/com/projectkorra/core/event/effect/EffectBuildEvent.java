package com.projectkorra.core.event.effect;

import java.util.Optional;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.projectkorra.core.ability.AbilityInstance;
import com.projectkorra.core.util.effect.Effect.EffectBuilder;

public class EffectBuildEvent extends Event {
	
	private static final HandlerList HANDLERS = new HandlerList();
	
	private EffectBuilder builder;
	private String tag;
	private Optional<AbilityInstance> provider;
	
	public EffectBuildEvent(EffectBuilder builder, String tag, Optional<AbilityInstance> provider) {
		this.builder = builder;
		this.tag = tag;
		this.provider = provider;
	}
	
	public EffectBuilder getBuilder() {
		return builder;
	}
	
	public String getTag() {
		return tag;
	}
	
	public Optional<AbilityInstance> getProvider() {
		return provider;
	}

	@Override
	public HandlerList getHandlers() {
		return HANDLERS;
	}

	public static HandlerList getHandlerList() {
		return HANDLERS;
	}
}
