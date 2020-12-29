package com.projectkorra.core.system.ability;

import com.projectkorra.core.util.configuration.Config;

public abstract class AbilityInstance {
	
	protected AbilityActivator activator;
	protected Config config;
	
	public AbilityInstance(AbilityActivator activator, Config config) {
		this.activator = activator;
		this.config = config;
	}
	
	public AbilityActivator getActivator() {
		return activator;
	}
	
	public abstract void progress();
	public abstract Class<? extends Ability> getProvider();
}
