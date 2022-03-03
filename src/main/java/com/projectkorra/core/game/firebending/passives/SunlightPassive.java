package com.projectkorra.core.game.firebending.passives;

import com.google.common.collect.ImmutableSet;
import com.projectkorra.core.ability.Ability;
import com.projectkorra.core.ability.AbilityInstance;
import com.projectkorra.core.ability.AbilityManager;
import com.projectkorra.core.ability.AbilityUser;
import com.projectkorra.core.ability.activation.Activation;
import com.projectkorra.core.ability.attribute.Attribute;
import com.projectkorra.core.ability.attribute.Modifier;
import com.projectkorra.core.ability.type.Passive;
import com.projectkorra.core.event.ability.InstanceStartEvent;
import com.projectkorra.core.skill.Skill;
import com.projectkorra.core.util.configuration.Configure;

import org.bukkit.World;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;

public class SunlightPassive extends Ability implements Passive {

    @Configure("multiplier.damage")
    public double modDamage = 1.5;
    @Configure("multiplier.speed")
    public double modSpeed = 1.2;
    @Configure("multiplier.range")
    public double modRange = 2.0;
    @Configure("divisor.chargeTime")
    public long divChargeTime = 2;
    @Configure("divisor.cooldown")
    public long divCooldown = 2;

    public SunlightPassive() {
        super("Sunlight", "Firebenders are stronger during the day!", "ProjectKorra", "CORE", Skill.FIREBENDING);
    }

    @Override
    public void postProcessed() {}

    @Override
    public Activation getTrigger() {
        return null;
    }

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
    
    @EventHandler
    public void onAbilityStart(InstanceStartEvent event) {
        if (event.getInstance().getProvider().getSkill() != Skill.FIREBENDING) {
            return;
        }

        World world = event.getInstance().getUser().getLocation().getWorld();

        if (world.getTime() > 12000) {
            return;
        }

        AbilityManager.addModifier(event.getInstance(), Attribute.DAMAGE, Modifier.multiply(modDamage));
        AbilityManager.addModifier(event.getInstance(), Attribute.RANGE, Modifier.multiply(modRange));
        AbilityManager.addModifier(event.getInstance(), Attribute.SPEED, Modifier.multiply(modSpeed));
        AbilityManager.addModifier(event.getInstance(), Attribute.CHARGE_DURATION, Modifier.divide(divChargeTime));
        AbilityManager.addModifier(event.getInstance(), Attribute.COOLDOWN, Modifier.divide(divCooldown));
    }
}
