package com.projectkorra.core.system.ability;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.projectkorra.core.system.ability.activation.Activation;
import com.projectkorra.core.system.ability.type.ExpanderInstance;

public class ActiveInfo {

	private Map<Class<? extends AbilityInstance>, AbilityInstances> instances;
	private List<ComboAgent> sequences;
	private AbilityBinds original;
	private ExpanderInstance expander;
	
	ActiveInfo() {
		instances = new HashMap<>();
		sequences = new ArrayList<>();
		original = new AbilityBinds();
		expander = null;
	}
	
	boolean addInstance(AbilityInstance instance) {
		return instances.computeIfAbsent(instance.getClass(), (c) -> new AbilityInstances(instance.getCapacity())).add(instance);
	}
	
	void removeInstance(AbilityInstance instance) {
		if (instance == null) {
			return;
		}
		
		instances.get(instance.getClass()).remove(instance);
		if (instance == expander) {
			instance.getUser().getBinds().copy(original);
			expander = null;
		}
	}
	
	ComboAgent updateCombos(Ability ability, Activation trigger) {
		ComboAgent completed = null;
		sequences.add(new ComboAgent());
		Iterator<ComboAgent> iter = sequences.iterator();
		while (iter.hasNext()) {
			ComboAgent agent = iter.next();
			
			switch (agent.update(ability, trigger)) {
			case COMPLETE:
				completed = agent;
			case FAILED:
				iter.remove();
			case INCOMPLETE:
				break;
			}
		}
		
		return completed;
	}
	
	boolean expand(ExpanderInstance instance) {
		if (expander == null || !instances.computeIfAbsent(instance.getClass(), (c) -> new AbilityInstances(instance.getCapacity())).add(instance)) {
			return false;
		}
		
		original.copy(instance.getUser().getBinds());
		instance.getUser().getBinds().copy(instance.getNewBinds());
		expander = instance;
		return true;
	}
}
