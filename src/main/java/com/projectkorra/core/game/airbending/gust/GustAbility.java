package com.projectkorra.core.game.airbending.gust;

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

public class GustAbility extends Ability implements Bindable {

    @Configure("blast.range")
    double blastRange = 10.0;
    @Configure("blast.knockback")
    double blastKnockback = 0.8;
    @Configure("blast.speed")
    double blastSpeed = 20.0;
    @Configure("blast.cooldown")
    long blastCooldown = 2000;
    @Configure("stream.range")
    double streamRange = 4.0;
    @Configure("stream.knockback")
    double streamKnockback = 1.4;
    @Configure("stream.speed")
    double streamSpeed = 20.0;
    @Configure("stream.cooldown")
    long streamCooldown = 3000;
    @Configure("stream.duration")
    long streamDuration = 0;
    @Configure("stream.dynamicFactor")
    double streamDynamicFactor = 0.5;

    public GustAbility() {
        super("Gust", "Create a blast of air to knockback opponents, or condense the blast into a stream of air with less range but more knockback!", "ProjectKorra", "CORE", Skill.AIRBENDING);
    }

    @Override
    public void postProcessed() {}

    @Override
    protected AbilityInstance activate(AbilityUser user, Activation trigger, Event provider) {
        if (user.isOnCooldown(this)) {
            return null;
        } else if (trigger == Activation.LEFT_CLICK && !AbilityManager.hasInstance(user, GustStreamInstance.class)) {
            return new GustBlastInstance(this, user);
        } else if (trigger == Activation.SNEAK_DOWN) {
            return new GustStreamInstance(this, user);
        } else if (trigger == Activation.SNEAK_UP && AbilityManager.hasInstance(user, GustStreamInstance.class)) {
            AbilityManager.getInstance(user, GustStreamInstance.class).ifPresent(AbilityManager::remove);
        }

        return null;
    }

    @Override
    protected void onRegister() {}

    @Override
    public boolean uses(Activation trigger) {
        return trigger == Activation.LEFT_CLICK || trigger == Activation.SNEAK_DOWN || trigger == Activation.SNEAK_UP;
    }

    @Override
    public ImmutableSet<Class<? extends AbilityInstance>> instanceClasses() {
        return ImmutableSet.of(GustBlastInstance.class, GustStreamInstance.class);
    }

    @Override
    public String getInstructions() {
        return "Left Click for blast, Hold Sneak for stream";
    }
    
}
