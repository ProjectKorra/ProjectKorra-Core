package com.projectkorra.core.system.ability;

import java.util.function.Predicate;

public interface Passive {

	public Predicate<AbilityActivator> getActivationCondition();
	
}
