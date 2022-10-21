package com.projectkorra.core;

import com.projectkorra.core.api.AbilityInfo;
import com.projectkorra.core.api.AbilityInstance;
import com.projectkorra.core.api.User;

public class WaterManipulationInstance extends AbilityInstance {

    public WaterManipulationInstance(User user, AbilityInfo info, int priority) {
        super(user, info, priority);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onStart() {
        // TODO Auto-generated method stub

    }

    @Override
    public void progress() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onRemove() {
        // TODO Auto-generated method stub

    }

    public void freeze() {
    }

    @Override
    public void update() {
        freeze();
    }

}
