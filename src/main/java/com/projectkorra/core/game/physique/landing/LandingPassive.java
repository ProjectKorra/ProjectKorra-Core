package com.projectkorra.core.game.physique.landing;

import com.google.common.collect.ImmutableSet;
import com.projectkorra.core.UserManager;
import com.projectkorra.core.ability.Ability;
import com.projectkorra.core.ability.AbilityInstance;
import com.projectkorra.core.ability.AbilityManager;
import com.projectkorra.core.ability.AbilityUser;
import com.projectkorra.core.ability.activation.Activation;
import com.projectkorra.core.ability.type.Passive;
import com.projectkorra.core.skill.Skill;
import com.projectkorra.core.util.configuration.Configure;

import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class LandingPassive extends Ability implements Passive {

    @Configure("damageReduction")
    private double reduction = 5;

    public LandingPassive() {
        super("Landing", "Break your fall so you don't take as much damage!", "ProjectKorra", "CORE", Skill.PHYSIQUE);
    }

    @Override
    public void postProcessed() {}

    @Override
    public Activation getTrigger() {
        return Activation.PASSIVE;
    }

    @Override
    protected AbilityInstance activate(AbilityUser user, Activation trigger, Event provider) {
        if (reduction < 0) {
            return null;
        }

        return new LandingInstance(this, user, reduction);
    }

    @Override
    protected void onRegister() {
        
    }

    @Override
    public boolean uses(Activation trigger) {
        return trigger == Activation.PASSIVE;
    }

    @Override
    public ImmutableSet<Class<? extends AbilityInstance>> instanceClasses() {
        return ImmutableSet.of();
    }
    
    @EventHandler
    public void onFallDamage(EntityDamageEvent event) {
        if (event.getCause() != DamageCause.FALL) {
            return;
        }

        AbilityUser user = UserManager.get(event.getEntity().getUniqueId());
        if (user == null) {
            return;
        }

        AbilityManager.getInstance(user, LandingInstance.class).ifPresent(a -> a.reduceDamage(event));
    }
}
