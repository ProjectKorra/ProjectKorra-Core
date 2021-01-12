package com.projectkorra.core.system.ability.type;

import com.projectkorra.core.system.ability.AbilityUser;
import com.projectkorra.core.system.ability.activation.Activation;

public interface Passive {

	default public boolean canActivate(AbilityUser activator) {
		return true;
	}
	
	default public Activation getTrigger() {
		return Activation.PASSIVE;
	}
}
