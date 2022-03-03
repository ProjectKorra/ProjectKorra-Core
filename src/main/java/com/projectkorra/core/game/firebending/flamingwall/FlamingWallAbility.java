package com.projectkorra.core.game.firebending.flamingwall;

import com.google.common.collect.ImmutableSet;
import com.projectkorra.core.ability.Ability;
import com.projectkorra.core.ability.AbilityInstance;
import com.projectkorra.core.ability.AbilityUser;
import com.projectkorra.core.ability.activation.Activation;
import com.projectkorra.core.skill.Skill;
import com.projectkorra.core.util.configuration.Configure;

import org.bukkit.event.Event;

public class FlamingWallAbility extends Ability {

    @Configure("raiseRadius")
    private double raiseRadius = 3;
    @Configure("shove.range")
    private double shoveRange = 6;
    @Configure("width")
    private double width = 6;
    @Configure("height")
    private double height = 4;
    @Configure("shove.knockback") 
    private double shoveKnockback = 0.8;
    @Configure("damage") 
    private double damage = 0.5;
    @Configure("shove.speed")
    private double shoveSpeed = 1.1;
    @Configure("cooldown")
    private long cooldown;

    public FlamingWallAbility() {
        super("FlamingWall", "Create a blazing wall that prevents enemies from passing through", "ProjectKorra", "CORE", Skill.FIREBENDING);
    }

    @Override
    public void postProcessed() {}

    @Override
    protected AbilityInstance activate(AbilityUser user, Activation trigger, Event provider) {
        return null;
    }

    @Override
    protected void onRegister() {}

    @Override
    public boolean uses(Activation trigger) {
        return false;
    }

    @Override
    public ImmutableSet<Class<? extends AbilityInstance>> instanceClasses() {
        return ImmutableSet.of(FlamingWall.class);
    }
    
}
