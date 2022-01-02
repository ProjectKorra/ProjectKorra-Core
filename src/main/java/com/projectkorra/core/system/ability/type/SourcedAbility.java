package com.projectkorra.core.system.ability.type;

import com.projectkorra.core.system.ability.AbilityUser;
import com.projectkorra.core.system.ability.SourceInstance;
import com.projectkorra.core.system.ability.activation.Activation;

public interface SourcedAbility {
    
    public SourceInstance selectSource(AbilityUser user, Activation trigger);
}
