package com.projectkorra.core;

import com.projectkorra.core.api.Ability;
import com.projectkorra.core.api.AbilityInfo;
import com.projectkorra.core.api.User;
import com.projectkorra.core.api.activation.Activation;
import com.projectkorra.core.api.game.InputType;

public class ExampleAbilityInfo extends AbilityInfo {

    public ExampleAbilityInfo() {
        super("Vahagn", "1.0.0", "FireAbility", true);
    }

    @Override
    public Activation getActivation() {
        return new Activation().check(0, InputType.LEFT_CLICK)
                .check(500L, InputType.SHIFT_DOWN)
                .check(500L, InputType.SHIFT_UP)
                .excludeAll(false, InputType.SPRINT_ON, InputType.SPRINT_OFF);
    }

    @Override
    public void load() {

    }

    @Override
    public Ability createInstance(User user) {

        return new ExampleAbilityInstance(user, this.priority);
    }
}
