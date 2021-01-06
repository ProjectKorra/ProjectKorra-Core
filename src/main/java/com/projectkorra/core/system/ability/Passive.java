package com.projectkorra.core.system.ability;

public interface Passive {

	default public boolean canActivate(AbilityActivator activator) {
		return true;
	}
	
	default public Activation getTrigger() {
		return Activation.PASSIVE;
	}
}
