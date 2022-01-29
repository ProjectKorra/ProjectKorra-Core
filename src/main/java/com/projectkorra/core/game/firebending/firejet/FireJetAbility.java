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
    private double speed = 20;
    @Configure("jet.duration")
    private long duration = 3000;
    @Configure("jet.cooldown")
    private long cooldown = 6000;
    @Configure("jet.acceleration")
    private double acceleration = 4;
    @Configure("dash.speed")
    private double dashSpeed = 0.9;
    @Configure("dash.cooldown")
    private long dashCooldown = 4000;
    @Configure("jump.speed")
    private double jumpSpeed = 1.1;
    @Configure("jump.cooldown")
    private long jumpCooldown = 7000;

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
            return new JetDashInstance(this, user, dashSpeed, dashCooldown);
        }

        if (trigger == Activation.SNEAK_DOWN) {
            if (user.getEntity().isOnGround() && !user.isOnCooldown("JetExtra")) {
                return new JetJumpInstance(this, user, jumpSpeed, jumpCooldown);
            } else if (!user.isOnCooldown(this)) {
                return new FireJetInstance(this, user, speed, duration, cooldown, acceleration);
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
