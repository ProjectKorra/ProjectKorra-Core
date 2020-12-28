package com.projectkorra.core.system.ability;

import java.util.Set;

import com.projectkorra.core.system.user.SkilledEntity;
import com.projectkorra.core.util.configuration.Config;

public abstract class AbilityInstance {
	
	protected Set<SkilledEntity> entities;
	protected Config config;
	
	public AbilityInstance(Set<SkilledEntity> entities, Config config) {
		this.entities = entities;
		this.config = config;
	}
	
	public Set<SkilledEntity> getEntities() {
		return entities;
	}
	
	public abstract void progress();
	public abstract Class<? extends Ability> getProvider();
}
