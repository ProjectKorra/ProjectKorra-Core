package com.projectkorra.core.system.ability;

public class AbilityActivationManager {

	private AbilityActivator activator;
	
	public AbilityActivationManager(AbilityActivator activator) {
		this.activator = activator;
	}
	
	// precedence will probably be combos -> bound ability -> passives to check for activation
	public AbilityInstance trigger(Activation trigger) {
		if (activator.getCurrentAbility() != null) {
			
			
		}
		
		return null;
	}
}
