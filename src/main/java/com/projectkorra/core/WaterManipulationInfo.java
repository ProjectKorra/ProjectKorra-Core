package com.projectkorra.core;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

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
    public List<Sequence<List<AbilityInstance>>> getActionSequences() {
        BiFunction<User, AbilityInfo, List<AbilityInstance>> action = (u, i) -> {
            return u.apply(i, a -> a.update());
        };

        Sequence<List<AbilityInstance>> c1 = new ActionCriteria(action).check(Input.BACKWARD);

        List<Sequence<List<AbilityInstance>>> list = new ArrayList<>();

        list.add(c1);

        return list;
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
