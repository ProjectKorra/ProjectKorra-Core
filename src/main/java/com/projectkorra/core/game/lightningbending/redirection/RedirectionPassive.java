package com.projectkorra.core.game.lightningbending.redirection;

import com.google.common.collect.ImmutableSet;
import com.projectkorra.core.UserManager;
import com.projectkorra.core.ability.Ability;
import com.projectkorra.core.ability.AbilityInstance;
import com.projectkorra.core.ability.AbilityManager;
import com.projectkorra.core.ability.AbilityUser;
import com.projectkorra.core.ability.activation.Activation;
import com.projectkorra.core.ability.type.Passive;
import com.projectkorra.core.entity.PlayerUser;
import com.projectkorra.core.event.ability.InstanceDamageEntityEvent;
import com.projectkorra.core.game.lightningbending.bolt.BoltAbility;
import com.projectkorra.core.game.lightningbending.bolt.BoltInstance;
import com.projectkorra.core.skill.Skill;

import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class RedirectionPassive extends Ability implements Passive {

    public RedirectionPassive() {
        super("Redirection", "Lightningbenders can take in lightning and release it again!", "ProjectKorra", "CORE", Skill.LIGHTNINGBENDING);
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
        return ImmutableSet.of();
    }

    @Override
    public Activation getTrigger() {
        return null;
    }

    @EventHandler
    public void onBoltDamage(InstanceDamageEntityEvent event) {
        if (!(event.getInstance() instanceof BoltInstance)) {
            return;
        }

        PlayerUser user = UserManager.getAs(event.getTarget().getUniqueId(), PlayerUser.class);
        if (user == null) {
            return;
        }

        if (!user.getEntity().isSneaking() || !user.getBoundAbility().filter((a) -> a instanceof BoltAbility).isPresent()) {
            return;
        }

        user.removeCooldown(AbilityManager.getAbility(BoltAbility.class).get());
        AbilityManager.activate(user, Activation.DAMAGED, event);
    }
    
    @EventHandler
    public void onLightningDamage(EntityDamageByEntityEvent event) {
        if (event.getCause() != DamageCause.LIGHTNING) {
            return;
        }

        PlayerUser user = UserManager.getAs(event.getEntity().getUniqueId(), PlayerUser.class);
        if (user == null) {
            return;
        }

        if (!user.getEntity().isSneaking() || !user.getBoundAbility().filter((a) -> a instanceof BoltAbility).isPresent()) {
            return;
        } else if (AbilityManager.hasInstance(user, BoltInstance.class) && !AbilityManager.getInstance(user, BoltInstance.class).get().canRedirect()) {
            return;
        }

        event.setCancelled(true);
        user.removeCooldown(AbilityManager.getAbility(BoltAbility.class).get());
        AbilityManager.activate(user, Activation.DAMAGED, event);
    }
}
