package com.projectkorra.core.system.ability.type;

import com.projectkorra.core.system.ability.AbilityBinds;
import com.projectkorra.core.system.ability.activation.Activation;

public interface Expander {
	
	public AbilityBinds getNewBinds();
	public boolean isExpansionTrigger(Activation trigger);
}
