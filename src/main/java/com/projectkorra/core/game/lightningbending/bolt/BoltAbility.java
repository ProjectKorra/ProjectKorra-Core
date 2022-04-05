package com.projectkorra.core.game.lightningbending.bolt;

import org.bukkit.event.Event;

import com.google.common.collect.ImmutableSet;
import com.projectkorra.core.ability.Ability;
import com.projectkorra.core.ability.AbilityInstance;
import com.projectkorra.core.ability.AbilityManager;
import com.projectkorra.core.ability.AbilityUser;
import com.projectkorra.core.ability.activation.Activation;
import com.projectkorra.core.ability.type.Bindable;
import com.projectkorra.core.game.lightningbending.redirection.RedirectionPassive;
import com.projectkorra.core.skill.Skill;
import com.projectkorra.core.util.configuration.Configure;

public class BoltAbility extends Ability implements Bindable {

    @Configure("minChargeTime")
    long minChargeTime = 2000;
    @Configure("maxChargeTime")
    long maxChargeTime = 5500;
    @Configure("damage")
    double damage = 9;
    @Configure("speed")
    double speed = 70;
    @Configure("range")
    double range = 40;
    @Configure("cooldown")
    long cooldown = 3500;
    @Configure("subarc.chance")
    double subarcChance = 0.75;
    @Configure("subarc.damage")
    double subarcDamage = 2;
    @Configure("subarc.speed")
    double subarcSpeed = 40;
    @Configure("subarc.range")
    double subarcRange = 7;
    @Configure
    double staminaCost = 0.4;

    public BoltAbility() {
        super("Bolt", "Shoot a lightning bolt!", "ProjectKorra", "CORE", Skill.LIGHTNINGBENDING);
    }

    @Override
    public void postProcessed() {}

    @Override
    public String getInstructions() {
        return "Hold sneak to charge a lightning bolt, and release it quick or it will backfire!";
    }

    @Override
    protected AbilityInstance activate(AbilityUser user, Activation trigger, Event provider) {
        if (trigger == RedirectionPassive.TRIGGER) {
            if (AbilityManager.hasInstance(user, BoltInstance.class)) {
                AbilityManager.getInstance(user, BoltInstance.class).ifPresent(BoltInstance::charge);
            } else {
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
        return trigger == Activation.SNEAK_DOWN || trigger == Activation.SNEAK_UP || trigger == Activation.LEFT_CLICK || trigger == RedirectionPassive.TRIGGER;
    }

    @Override
    public ImmutableSet<Class<? extends AbilityInstance>> instanceClasses() {
        return ImmutableSet.of(BoltInstance.class);
    }
}
