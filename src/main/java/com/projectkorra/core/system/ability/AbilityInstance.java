package com.projectkorra.core.system.ability;

public abstract class AbilityInstance {
	
	protected AbilityUser activator;
	
	public AbilityInstance(AbilityUser activator) {
		this.activator = activator;
	}
	
	public AbilityUser getActivator() {
		return activator;
	}
	
	public abstract void onStart();
	public abstract void onUpdate();
	public abstract void onStop();
	public abstract Class<? extends Ability> getProvider();
}
