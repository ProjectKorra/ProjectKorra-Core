package com.projectkorra.core;

import com.projectkorra.core.api.AbilityInfo;
import com.projectkorra.core.api.AbilityInstance;
import com.projectkorra.core.api.ActionCriteria;
import com.projectkorra.core.api.ActivationCriteria;
import com.projectkorra.core.api.User;
import com.projectkorra.core.api.game.Input;

public class WaterManipulationInfo extends AbilityInfo {

    public WaterManipulationInfo() {
        super("Vahagn", "1.0", "WaterManipulation", true);
        this.registerAction(new ActionCriteria(
                (u, i) -> (u.apply(i, (e) -> ((WaterManipulationInstance) e).freeze())))
                .check(Input.SHIFT_DOWN));
        this.registerActivation(new ActivationCriteria());
    }

    @Override
    public void load() {
        // TODO Auto-generated method stub

    }

    @Override
    public AbilityInstance createInstance(User user) {
        return new WaterManipulationInstance(user, this, 15);
    }

}
