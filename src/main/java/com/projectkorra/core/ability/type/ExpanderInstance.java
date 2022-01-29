package com.projectkorra.core.ability.type;

import com.projectkorra.core.ability.Ability;
import com.projectkorra.core.ability.AbilityBinds;
import com.projectkorra.core.ability.AbilityInstance;
import com.projectkorra.core.ability.AbilityUser;

public abstract class ExpanderInstance extends AbilityInstance {
	
	public ExpanderInstance(Ability provider, AbilityUser user) {
		super(provider, user);
	}

	public abstract AbilityBinds getNewBinds();
}
