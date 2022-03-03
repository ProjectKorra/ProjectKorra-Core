package com.projectkorra.core.game.firebending.firejet;

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

public class FireJetAbility extends Ability implements Bindable {

    @Configure("jet.maxSpeed")
    double speed = 20;
    @Configure("jet.duration")
    long duration = 3000;
    @Configure("jet.cooldown")
    long cooldown = 6000;
    @Configure("jet.acceleration")
    double acceleration = 4;
    @Configure("dash.speed")
    double dashSpeed = 0.9;
    @Configure("dash.cooldown")
    long dashCooldown = 4000;
    @Configure("jump.speed")
    double jumpSpeed = 1.1;
    @Configure("jump.cooldown")
    long jumpCooldown = 7000;

    public FireJetAbility() {
        super("FireJet", "Use firebending to create jet propulsion from your hands and feet.", "ProjectKorra", "CORE", Skill.FIREBENDING);
    }

    @Override
    public void postProcessed() {}

    @Override
    public String getInstructions() {
        return "Click while running ";
    }

    @Override
    protected AbilityInstance activate(AbilityUser user, Activation trigger, Event provider) {
        if (trigger == Activation.LEFT_CLICK && !user.isOnCooldown("JetExtra")) {
            return new JetDashInstance(this, user);
        }

        if (trigger == Activation.SNEAK_DOWN) {
            if (user.getEntity().isOnGround() && !user.isOnCooldown("JetExtra")) {
                return new JetJumpInstance(this, user);
            } else if (!user.isOnCooldown(this)) {
                return new FireJetInstance(this, user);
            }
        } 
        
        if (trigger == Activation.SNEAK_UP) {
            AbilityManager.getInstance(user, FireJetInstance.class).ifPresent(AbilityManager::remove);
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
        return ImmutableSet.of(FireJetInstance.class, JetDashInstance.class, JetJumpInstance.class);
    }
    
}
