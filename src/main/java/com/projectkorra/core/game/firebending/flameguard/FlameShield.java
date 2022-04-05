package com.projectkorra.core.game.firebending.flameguard;

import org.bukkit.event.Event;

import com.google.common.collect.ImmutableSet;
import com.projectkorra.core.ability.Ability;
import com.projectkorra.core.ability.AbilityInstance;
import com.projectkorra.core.ability.AbilityManager;
import com.projectkorra.core.ability.AbilityUser;
import com.projectkorra.core.ability.activation.Activation;
import com.projectkorra.core.ability.type.Bindable;
import com.projectkorra.core.skill.Skill;
import com.projectkorra.core.util.configuration.Configure;

public class FlameShield extends Ability implements Bindable {

    @Configure("radius") double radius = 1.7;
    @Configure("cooldown") long cooldown = 1000;
    @Configure("staminaDrain") double staminaDrain = 0.34;
    @Configure("collisionDamage") double collisionDamage = 0.25;

    public FlameShield() {
        super("FlameShield", "A small shield of flames to block basic abilities.", "ProjectKorra", "CORE", Skill.FIREBENDING);
    }

    @Override
    public void postProcessed() {}

    @Override
    public String getInstructions() {
        return "Hold sneak to create a small shield of flames for a short duration";
    }

    @Override
    protected AbilityInstance activate(AbilityUser user, Activation trigger, Event provider) {
        if (user.isOnCooldown(this)) {
            return null;
        }

        if (trigger == Activation.SNEAK_DOWN && user.getStamina().consume(0.1 * staminaDrain)) {
        	return new FlameShieldInstance(this, user);
        } else if (trigger == Activation.SNEAK_UP) {
        	AbilityManager.getInstance(user, FlameShieldInstance.class).ifPresent(AbilityManager::remove);
        }

        return null;
    }

    @Override
    protected void onRegister() {}

    @Override
    public boolean uses(Activation trigger) {
        return trigger == Activation.SNEAK_DOWN || trigger == Activation.SNEAK_UP;
    }

    @Override
    public ImmutableSet<Class<? extends AbilityInstance>> instanceClasses() {
        return ImmutableSet.of(FlameShieldInstance.class);
    }
    
}
