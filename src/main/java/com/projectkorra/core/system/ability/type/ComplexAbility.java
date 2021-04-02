package com.projectkorra.core.system.ability.type;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

import org.bukkit.event.Event;

import com.projectkorra.core.system.ability.Ability;
import com.projectkorra.core.system.ability.AbilityInstance;
import com.projectkorra.core.system.ability.AbilityUser;
import com.projectkorra.core.system.ability.activation.Activation;

public abstract class ComplexAbility implements Ability {

	private final Map<Activation, BiFunction<AbilityUser, Event, AbilityInstance>> TRIGGERS = new HashMap<>();
	
	@Override
	public AbilityInstance activate(AbilityUser user, Activation trigger, Event event) {
		return TRIGGERS.getOrDefault(trigger.getClass(), (u, e) -> null).apply(user, event);
	}
	
	@Override
	public boolean uses(Activation trigger) {
		return TRIGGERS.containsKey(trigger);
	}
	
	protected final void setInstance(Activation trigger, BiFunction<AbilityUser, Event, AbilityInstance> func) {
		if (trigger == null || func == null) {
			return;
		}
		
		TRIGGERS.put(trigger, func);
	}
}
