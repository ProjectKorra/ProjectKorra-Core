package com.projectkorra.core.game.firebending.fireball;

import java.util.Arrays;
import java.util.List;

import com.google.common.collect.ImmutableSet;
import com.projectkorra.core.ability.Ability;
import com.projectkorra.core.ability.AbilityInstance;
import com.projectkorra.core.ability.AbilityUser;
import com.projectkorra.core.ability.activation.Activation;
import com.projectkorra.core.ability.activation.SequenceInfo;
import com.projectkorra.core.ability.type.Bindable;
import com.projectkorra.core.ability.type.Combo;
import com.projectkorra.core.ability.type.Passive;
import com.projectkorra.core.skill.Skill;
import com.projectkorra.core.util.configuration.Configure;

import org.bukkit.event.Event;

public class FireballAbility extends Ability implements Bindable, Combo, Passive {

    @Configure("blast.range")
    double blastRange = 15;
    @Configure("blast.speed")
    double blastSpeed = 25;
    @Configure("blast.damage")
    double blastDamage = 2;
    @Configure("blast.cooldown")
    long blastCooldown = 2000;
    @Configure
    double staminaCost = 100;

    public FireballAbility() {
        super("Fireball", "Throw fireballs!", "ProjectKorra", "CORE", Skill.FIREBENDING);
    }

    @Override
    public void postProcessed() {}

    @Override
    protected AbilityInstance activate(AbilityUser user, Activation trigger, Event provider) {
        if (!user.isOnCooldown(this) && trigger == Activation.LEFT_CLICK && user.getStamina().consume(staminaCost)) {
            return new FireballBlast(this, user);
        }
        
        if (!user.isOnCooldown("FireballCombo") && trigger == Activation.COMBO && user.getStamina().consume(staminaCost)) {
            user.addCooldown("FireballCombo", blastCooldown);
            return new FireballBlast(this, user);
        }

        return null;
    }

    @Override
    protected void onRegister() {}

    @Override
    public boolean uses(Activation trigger) {
        return trigger == Activation.LEFT_CLICK || trigger == Activation.SNEAK_DOWN || trigger == Activation.SNEAK_UP || trigger == Activation.COMBO || trigger == Activation.PASSIVE;
    }

    @Override
    public ImmutableSet<Class<? extends AbilityInstance>> instanceClasses() {
        return ImmutableSet.of(FireballBlast.class);
    }

    @Override
    public String getInstructions() {
        return "Left click for blast and the power of the fireball will be increased by the charge";
    }

    @Override
    public List<SequenceInfo> getSequence() {
        return Arrays.asList(SequenceInfo.of(this, Activation.LEFT_CLICK), SequenceInfo.of(this, Activation.SNEAK_DOWN), SequenceInfo.of(this, Activation.LEFT_CLICK));
    }

    @Override
    public Activation getTrigger() {
        return Activation.PASSIVE;
    }

}
