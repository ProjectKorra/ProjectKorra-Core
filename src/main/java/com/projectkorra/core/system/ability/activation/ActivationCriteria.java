package com.projectkorra.core.system.ability.activation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import com.projectkorra.core.system.ability.Ability;
import com.projectkorra.core.system.ability.AbilityInstance;
import com.projectkorra.core.system.ability.AbilityUser;
import com.projectkorra.core.util.function.TriPredicate;

public class ActivationCriteria {

	public static final ActivationCriteria EMPTY = new ActivationCriteria((user) -> null);
	
	private Function<AbilityUser, AbilityInstance> creator;
	private List<TriPredicate<AbilityUser, Ability, Activation>> nodes;
	private int current = 0;
	
	ActivationCriteria(Function<AbilityUser, AbilityInstance> creator) {
		this.creator = creator;
		this.nodes = Arrays.asList((a, b, c) -> true);
	}
	
	ActivationCriteria(Function<AbilityUser, AbilityInstance> creator, List<TriPredicate<AbilityUser, Ability, Activation>> nodes) {
		this.creator = creator;
		this.nodes = nodes;
	}
	
	public int getProgress() {
		return current;
	}
	
	public AbilityInstance update(AbilityUser activator, Ability ability, Activation trigger) {
		if (nodes.get(current).test(activator, ability, trigger)) {
			if (++current == nodes.size()) {
				return creator.apply(activator);
			}
		} else {
			current = 0;
		}
		
		return null;
	}
	
	public static class Builder {
		
		private List<TriPredicate<AbilityUser, Ability, Activation>> nodes;
		
		public Builder() {
			this.nodes = new ArrayList<>();
		}
		
		public Builder add(TriPredicate<AbilityUser, Ability, Activation> condition) {
			Objects.requireNonNull(condition, "Activation condition cannot be null!");
			this.nodes.add(condition);
			return this;
		}
		
		public ActivationCriteria build(Function<AbilityUser, AbilityInstance> creator) {
			return new ActivationCriteria(creator, nodes);
		}
	}
}
