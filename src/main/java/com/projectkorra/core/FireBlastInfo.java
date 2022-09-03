package com.projectkorra.core;

import com.projectkorra.core.ability.Ability;
import com.projectkorra.core.ability.AbilityInfo;
import com.projectkorra.core.ability.User;
import com.projectkorra.core.ability.activation.Activation;
import com.projectkorra.core.game.InputType;

public class FireBlastInfo extends AbilityInfo {

    // add default config variables here

    public FireBlastInfo() {
        super("Vahagn", "1.0", "FireBlast", true);
    }

    @Override
    public Activation getActivation() {
        // TODO Auto-generated method stub
        return new Activation().check(InputType.LEFT_CLICK);
    }

    @Override
    public void load() {
        // TODO Auto-generated method stub

    }

    @Override
    public Ability createInstance(User user) {
        // TODO Auto-generated method stub
        return new FireBlastInstance(user, priority);
    }

}
