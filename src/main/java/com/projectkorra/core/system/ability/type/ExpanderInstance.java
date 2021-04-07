package com.projectkorra.core.system.ability.type;

import com.projectkorra.core.system.ability.AbilityBinds;
import com.projectkorra.core.system.ability.AbilityInstance;
import com.projectkorra.core.system.ability.AbilityUser;

public abstract class ExpanderInstance extends AbilityInstance {
	
	public ExpanderInstance(AbilityUser user) {
		super(user);
	}
	
	@Override
	public int getCapacity() {
		return 1;
	}

	public abstract AbilityBinds getNewBinds();
}
