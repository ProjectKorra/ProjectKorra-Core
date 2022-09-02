package com.projectkorra.core;

import com.projectkorra.core.ability.Ability;
import com.projectkorra.core.ability.AbilityInfo;
import com.projectkorra.core.ability.BendingUser;
import com.projectkorra.core.ability.activation.Activation;

public class FireAbilityInfo extends AbilityInfo {
    
    public FireAbilityInfo() {
        super("Vahagn", "1.0.0", "FireAbility", true);
    }

    @Override
    public Activation getActivation() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void load() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public Ability createInstance(BendingUser user) {
        // TODO Auto-generated method stub
        return null;
    }
}
