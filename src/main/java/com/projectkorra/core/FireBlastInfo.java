package com.projectkorra.core;

import com.projectkorra.core.api.Ability;
import com.projectkorra.core.api.AbilityInfo;
import com.projectkorra.core.api.User;
import com.projectkorra.core.api.activation.Activation;
import com.projectkorra.core.api.game.Input;

public class FireBlastInfo extends AbilityInfo {

    // add default config variables here

    public FireBlastInfo() {
        super("Vahagn", "1.0", "FireBlast", true);
    }

    @Override
    public Activation getActivation() {
        // TODO Auto-generated method stub
        return new Activation().check(Input.LEFT_CLICK);
    }

    @Override
    public void load() {
        // TODO Auto-generated method stub

    }

    @Override
    public Ability createInstance(User user) {
        // TODO Auto-generated method stub
        return new FireBlastInstance(user, this, priority);
    }

}
