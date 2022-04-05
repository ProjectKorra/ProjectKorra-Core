package com.projectkorra.core.game.firebending.flamingwall;

import com.google.common.collect.ImmutableSet;
import com.projectkorra.core.ability.Ability;
import com.projectkorra.core.ability.AbilityInstance;
import com.projectkorra.core.ability.AbilityManager;
import com.projectkorra.core.ability.AbilityUser;
import com.projectkorra.core.ability.activation.Activation;
import com.projectkorra.core.ability.type.Bindable;
import com.projectkorra.core.skill.Skill;
import com.projectkorra.core.util.Blocks;
import com.projectkorra.core.util.Vectors;
import com.projectkorra.core.util.configuration.Configure;

import org.bukkit.event.Event;

public class FlamingWall extends Ability implements Bindable {

    @Configure("raise.radius")
    double raiseRadius = 4;
    @Configure("shove.range")
    double shoveRange = 15;
    @Configure("width")
    double width = 6;
    @Configure("height")
    double height = 5;
    @Configure("shove.knockback") 
    double shoveKnockback = 0.8;
    @Configure("damage") 
    double damage = 0.5;
    @Configure("shove.speed")
    double shoveSpeed = 10;
    @Configure("cooldown")
    long cooldown = 3000;
    @Configure("duration")
    long duration = 7000;
    @Configure("raise.speed")
    double raiseSpeed = 8;
    @Configure
    double staminaCost = 0.01;
    @Configure
    double staminaDrain = 0.01;

    public FlamingWall() {
        super("FlamingWall", "Create a blazing wall that prevents enemies from passing through", "ProjectKorra", "CORE", Skill.FIREBENDING);
    }

    @Override
    public void postProcessed() {}

    @Override
    protected AbilityInstance activate(AbilityUser user, Activation trigger, Event provider) {
        if (user.isOnCooldown(this)) {
            return null;
        }

        if (!AbilityManager.hasInstance(user, FlamingWallInstance.class) && trigger == Activation.LEFT_CLICK && user.getStamina().consume(staminaCost) && !Blocks.getTop(user.getLocation().add(Vectors.direction(user.getLocation().getYaw(), 0)), height).isEmpty()) {
            return new FlamingWallInstance(this, user);
        } else if (trigger == Activation.SNEAK_DOWN) {
            AbilityManager.getInstance(user, FlamingWallInstance.class).ifPresent(FlamingWallInstance::shove);
        } else if (trigger == Activation.SNEAK_UP) {
            AbilityManager.getInstance(user, FlamingWallInstance.class).ifPresent(FlamingWallInstance::unshove);
        }

        return null;
    }

    @Override
    protected void onRegister() {}

    @Override
    public boolean uses(Activation trigger) {
        return trigger == Activation.SNEAK_DOWN || trigger == Activation.SNEAK_UP || trigger == Activation.LEFT_CLICK;
    }

    @Override
    public ImmutableSet<Class<? extends AbilityInstance>> instanceClasses() {
        return ImmutableSet.of(FlamingWallInstance.class);
    }

    @Override
    public String getInstructions() {
        return "Hold sneak to raise a wall of flames in front of you, or click to raise it parallel to you";
    }
    
}
