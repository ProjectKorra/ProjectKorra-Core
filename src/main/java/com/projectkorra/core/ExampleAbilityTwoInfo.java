package com.projectkorra.core;

import com.projectkorra.core.api.Ability;
import com.projectkorra.core.api.AbilityInfo;
import com.projectkorra.core.api.User;
import com.projectkorra.core.api.activation.Activation;
import com.projectkorra.core.api.game.Input;

public class ExampleAbilityTwoInfo extends AbilityInfo {

    public ExampleAbilityTwoInfo() {
        super("Vahagn", "1.0", "PlantBomb", true);
    }

    @Override
    public Activation getActivation() {
        return new Activation().check(0, Input.SHIFT_DOWN)
                .check(10000L, Input.SHIFT_UP)
                .check(10000L, Input.SHIFT_DOWN)
                .check(10000L, Input.SHIFT_UP)
                .excludeAll(false, Input.SPRINT_OFF, Input.SPRINT_ON);
    }

    @Override
    public void load() {
        // TODO Auto-generated method stub

    }

    @Override
    public Ability createInstance(User user) {
        // TODO Auto-generated method stub
        return new ExampleAbilityTwoInstance(user, this, this.priority);
    }

}
