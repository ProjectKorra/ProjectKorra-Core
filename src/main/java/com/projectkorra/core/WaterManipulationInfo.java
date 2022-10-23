package com.projectkorra.core;

import java.util.List;

import com.projectkorra.core.api.AbilityInfo;
import com.projectkorra.core.api.AbilityInstance;
import com.projectkorra.core.api.ActionCriteria;
import com.projectkorra.core.api.ActivationCriteria;
import com.projectkorra.core.api.Sequence;
import com.projectkorra.core.api.User;
import com.projectkorra.core.api.game.Input;

public class WaterManipulationInfo extends AbilityInfo {

    public WaterManipulationInfo() {
        super("Vahagn", "1.0", "WaterManipulation", true);
    }

    @Override
    public Sequence<AbilityInstance> getActivationSequence() {
        return new ActivationCriteria();
    }

    @Override
    public Sequence<List<AbilityInstance>> getActionSequence() {

        Sequence<List<AbilityInstance>> c1 = new ActionCriteria((u, i) -> u.apply(i, a -> a.update()))
                .check(Input.LEFT_CLICK);

        return c1;
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
