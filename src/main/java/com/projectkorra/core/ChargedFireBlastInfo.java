package com.projectkorra.core;

import com.projectkorra.core.api.Ability;
import com.projectkorra.core.api.AbilityInfo;
import com.projectkorra.core.api.User;
import com.projectkorra.core.api.activation.Activation;
import com.projectkorra.core.game.InputType;

public class ChargedFireBlastInfo extends AbilityInfo {

    // add default config variables here

    public ChargedFireBlastInfo() {
        super("Vahagn", "1.0", "ChargedFireBlast", false);
    }

    @Override
    public Activation getActivation() {

        return new Activation().check(InputType.SHIFT_DOWN);
    }

    @Override
    public Ability createInstance(User user) {
        return new FireBlastInstance(user, priority).new ChargedFireBlastInstance(user, priority);
    }

    @Override
    public void load() {
        // TODO Auto-generated method stub

    }
}
