package com.projectkorra.core.system.ability;

public interface AbilityInstance {
	public void progress();
	public Class<? extends Ability> getProvider();
}
