package com.projectkorra.core;

import com.projectkorra.core.ability.Ability;
import com.projectkorra.core.ability.AbilityInfo;
import com.projectkorra.core.ability.User;
import com.projectkorra.core.ability.activation.Activation;
import com.projectkorra.core.game.InputType;

public class ExampleAbilityTwoInfo extends AbilityInfo {

    public ExampleAbilityTwoInfo() {
        super("Vahagn", "1.0", "PlantBomb", true);
    }

    @Override
    public Activation getActivation() {
        return new Activation().check(0, InputType.SHIFT_DOWN)
                .check(10000L, InputType.SHIFT_UP)
                .check(10000L, InputType.SHIFT_DOWN)
                .check(10000L, InputType.SHIFT_UP)
                .excludeAll(false, InputType.SPRINT_OFF, InputType.SPRINT_ON);
    }

    @Override
    public void load() {
        // TODO Auto-generated method stub

    }

    @Override
    public Ability createInstance(User user) {
        // TODO Auto-generated method stub
        return new ExampleAbilityTwoInstance(user, this.priority);
    }

}
