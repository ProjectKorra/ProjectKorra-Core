package com.projectkorra.core.game.physique.wellbeing;

import com.google.common.collect.ImmutableSet;
import com.projectkorra.core.ability.Ability;
import com.projectkorra.core.ability.AbilityInstance;
import com.projectkorra.core.ability.AbilityUser;
import com.projectkorra.core.ability.activation.Activation;
import com.projectkorra.core.ability.type.Passive;
import com.projectkorra.core.skill.Skill;
import com.projectkorra.core.util.configuration.Configure;

import org.bukkit.event.Event;

public class WellbeingPassive extends Ability implements Passive {

    @Configure("health")
    private double health = 100;

    public WellbeingPassive() {
        super("Wellbeing", "description", "ProjectKorra", "CORE", Skill.PHYSIQUE);
    }

    @Override
    public void postProcessed() {}

    @Override
    public Activation getTrigger() {
        return Activation.PASSIVE;
    }

    @Override
    protected AbilityInstance activate(AbilityUser user, Activation trigger, Event provider) {
        if (health < 0) {
            return null;
        }

        return new WellbeingInstance(this, user, health);
    }

    @Override
    protected void onRegister() {}

    @Override
    public boolean uses(Activation trigger) {
        return trigger == Activation.PASSIVE;
    }

    @Override
    public ImmutableSet<Class<? extends AbilityInstance>> instanceClasses() {
        return ImmutableSet.of(WellbeingInstance.class);
    }
    
}
