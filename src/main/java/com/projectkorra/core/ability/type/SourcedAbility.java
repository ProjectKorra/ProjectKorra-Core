package com.projectkorra.core.ability.type;

import com.projectkorra.core.ability.AbilityUser;
import com.projectkorra.core.ability.SourceInstance;
import com.projectkorra.core.ability.activation.Activation;

public interface SourcedAbility {
    
    public SourceInstance selectSource(AbilityUser user, Activation trigger);
}
