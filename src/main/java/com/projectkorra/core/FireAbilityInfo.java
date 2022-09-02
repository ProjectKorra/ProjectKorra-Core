package com.projectkorra.core;


import com.projectkorra.core.ability.Ability;
import com.projectkorra.core.ability.AbilityInfo;
import com.projectkorra.core.ability.BendingUser;
import com.projectkorra.core.ability.activation.Activation;
import com.projectkorra.core.game.InputType;

public class FireAbilityInfo extends AbilityInfo {
    
    public FireAbilityInfo() {
        super("Vahagn", "1.0.0", "FireAbility", true);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Activation getActivation() {
        return new Activation().chain(0, b -> b.did(InputType.LEFT_CLICK)).chain(500L, b -> b.did(InputType.SHIFT_DOWN)).chain(500L, b -> b.did(InputType.SHIFT_UP));
    }

    @Override
    public void load() {
   
    }

    @Override
    public Ability createInstance(BendingUser user) {
        
        return new FireAbilityInstance(user, this.priority);
    }
}
