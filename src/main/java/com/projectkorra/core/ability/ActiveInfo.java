package com.projectkorra.core.ability;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.projectkorra.core.ability.activation.Activation;
import com.projectkorra.core.ability.type.ExpanderInstance;

public class ActiveInfo {

	private Map<Class<? extends AbilityInstance>, LinkedList<AbilityInstance>> instances;
	private List<ComboAgent> sequences;
	private AbilityBinds original;
	private ExpanderInstance expander;
	private AbilityUser user;

	ActiveInfo(AbilityUser user) {
		this.instances = new HashMap<>();
		this.sequences = new ArrayList<>();
		this.original = new AbilityBinds();
		this.expander = null;
		this.user = user;
	}

	Set<AbilityInstance> all() {
		Set<AbilityInstance> all = new HashSet<>();
		for (List<AbilityInstance> list : instances.values()) {
			all.addAll(list);
		}

		return all;
	}

	void clear() {
		instances.clear();
		sequences.clear();
		user.getBinds().copy(original);
		original = new AbilityBinds();
		expander = null;
	}

	LinkedList<AbilityInstance> getInstances(Class<? extends AbilityInstance> clazz) {
		return instances.get(clazz);
	}

	boolean hasInstance(Class<? extends AbilityInstance> clazz) {
		return instances.get(clazz) != null && instances.get(clazz).size() > 0;
	}

	boolean addInstance(AbilityInstance instance) {
		if (instance == null) {
			return false;
		}

		if (instance instanceof ExpanderInstance) {
			if (expander != null) {
				return false;
			}

			expander = (ExpanderInstance) instance;
			original.copy(instance.getUser().getBinds());
			instance.getUser().getBinds().copy(expander.getNewBinds());
		}

		return instances.computeIfAbsent(instance.getClass(), (c) -> new LinkedList<>()).add(instance);
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

		if (completed != null) {
			sequences.clear();
		}

		return completed;
	}
}
