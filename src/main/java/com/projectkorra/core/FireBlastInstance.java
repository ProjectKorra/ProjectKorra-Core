package com.projectkorra.core;

import com.projectkorra.core.api.Ability;
import com.projectkorra.core.api.User;

public class FireBlastInstance extends Ability {

    public FireBlastInstance(User user, int priority) {
        super(user, priority);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onStart() {
        // TODO Auto-generated method stub

    }

    @Override
    public void progress() {
        // TODO Auto-generated method stub

        // do normal fireblast function
    }

    @Override
    public void onRemove() {
        // TODO Auto-generated method stub

    }

    public class ChargedFireBlastInstance extends FireBlastInstance {

        public ChargedFireBlastInstance(User user, int priority) {
            super(user, priority);
            // TODO Auto-generated constructor stub
        }

        @Override
        public void progress() {
            super.progress();

            // add code for explosions and additional particles here;
        }

    }
}
