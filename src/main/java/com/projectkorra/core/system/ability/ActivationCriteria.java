package com.projectkorra.core.system.ability;

import java.util.function.BiPredicate;
import java.util.function.Function;

public class ActivationCriteria {

	public static final ActivationCriteria EMPTY = new ActivationCriteria((user) -> null);
	
	private Function<AbilityActivator, AbilityInstance> creation;
	private BiPredicate<Ability, AbilityActivator> prereq;
	
	public ActivationCriteria(Function<AbilityActivator, AbilityInstance> creation) {
		this.creation = creation;
		this.prereq = (a, b) -> true;
	}
	
	public ActivationCriteria(Function<AbilityActivator, AbilityInstance> creation, BiPredicate<Ability, AbilityActivator> prereq) {
		this.creation = creation;
		this.prereq = prereq;
	}
	
	public AbilityInstance apply(AbilityActivator activator, Ability ability) {
		if (prereq.test(ability, activator)) {
			return creation.apply(activator);
		}
		
		return null;
	}
}
