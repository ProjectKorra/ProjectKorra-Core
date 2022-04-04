package com.projectkorra.core.game.lightningbending.bolt;

import com.google.common.collect.ImmutableSet;
import com.projectkorra.core.ability.Ability;
import com.projectkorra.core.ability.AbilityInstance;
import com.projectkorra.core.ability.AbilityManager;
import com.projectkorra.core.ability.AbilityUser;
import com.projectkorra.core.ability.activation.Activation;
import com.projectkorra.core.ability.type.Bindable;
import com.projectkorra.core.skill.Skill;
import com.projectkorra.core.util.configuration.Configure;

import org.bukkit.event.Event;

public class BoltAbility extends Ability implements Bindable {

    @Configure("minChargeTime")
    long minChargeTime = 2000;
    @Configure("maxChargeTime")
    long maxChargeTime = 5500;
    @Configure("damage")
    double damage = 6;
    @Configure("speed")
    double speed = 70;
    @Configure("range")
    double range = 40;
    @Configure("cooldown")
    long cooldown = 8000;
    @Configure("subarc.chance")
    double subarcChance = 0.3;
    @Configure("subarc.damage")
    double subarcDamage = 1;
    @Configure("subarc.speed")
    double subarcSpeed = 50;
    @Configure("subarc.range")
    double subarcRange = 10;
    @Configure
    double staminaCost = 500;

    public BoltAbility() {
        super("Bolt", "Shoot a lightning bolt!", "ProjectKorra", "CORE", Skill.LIGHTNINGBENDING);
    }

    @Override
    public void postProcessed() {}

    @Override
    public String getInstructions() {
        return "Hold sneak to charge a lightning bolt, and release it quick or deal damage to yourself!";
    }

    @Override
    protected AbilityInstance activate(AbilityUser user, Activation trigger, Event provider) {
        if (trigger == Activation.DAMAGED) {
            user.sendMessage("a");
            if (AbilityManager.hasInstance(user, BoltInstance.class)) {
                user.sendMessage("b");
                AbilityManager.getInstance(user, BoltInstance.class).ifPresent(BoltInstance::charge);
            } else {
                user.sendMessage("c");
                BoltInstance bolt = new BoltInstance(this, user, false);
                bolt.charge();
                return bolt;
            }
        }
        
        if (user.isOnCooldown(this)) {
            return null;
        }

        if (trigger == Activation.SNEAK_DOWN) {
            return new BoltInstance(this, user, false);
        }

        if (trigger == Activation.SNEAK_UP) {
            AbilityManager.getInstance(user, BoltInstance.class).ifPresent(BoltInstance::releaseSneak);
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
        return ImmutableSet.of(BoltInstance.class);
    }
    
    
}
