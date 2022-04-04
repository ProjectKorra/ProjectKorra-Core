package com.projectkorra.core.game.firebending.blazingarc;

import com.google.common.collect.ImmutableSet;
import com.projectkorra.core.ability.Ability;
import com.projectkorra.core.ability.AbilityInstance;
import com.projectkorra.core.ability.AbilityUser;
import com.projectkorra.core.ability.activation.Activation;
import com.projectkorra.core.ability.type.Bindable;
import com.projectkorra.core.skill.Skill;
import com.projectkorra.core.util.configuration.Configure;

import org.bukkit.event.Event;

public class BlazingArcAbility extends Ability implements Bindable {

    @Configure("speed")
    double speed = 30;
    @Configure("range")
    double range = 15;
    @Configure("damage")
    double damage = 1;
    @Configure("cooldown")
    long cooldown = 3000;
    @Configure("knockback")
    double knockback = 0.7;
    @Configure(comment = "How long the user has to swipe their cursor to make the arc")
    long delay = 200;
    @Configure
    double staminaCost = 100;

    public BlazingArcAbility() {
        super("BlazingArc", "Create a arcing blaze in front of you", "ProjectKorra", "CORE", Skill.FIREBENDING);
    }

    @Override
    public void postProcessed() {}

    @Override
    public String getInstructions() {
        return "Left click";
    }

    @Override
    protected AbilityInstance activate(AbilityUser user, Activation trigger, Event provider) {
        if (user.isOnCooldown(this)) {
            return null;
        }

        if (trigger == Activation.LEFT_CLICK && user.getStamina().consume(staminaCost)) {
            return new BlazingArcInstance(this, user);
        }

        return null;
    }

    @Override
    protected void onRegister() {}

    @Override
    public boolean uses(Activation trigger) {
        return trigger == Activation.LEFT_CLICK;
    }

    @Override
    public ImmutableSet<Class<? extends AbilityInstance>> instanceClasses() {
        return ImmutableSet.of(BlazingArcInstance.class);
    }
    
    
}
