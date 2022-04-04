package com.projectkorra.core.game.firebending.flamethrower;

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

public class Flamethrower extends Ability implements Bindable {

    @Configure("damage") double damage = 0.5;
    @Configure("range") double range = 20;
    @Configure("cooldown") long cooldown = 6000;
    @Configure("radius") double radius = 1.5;
    @Configure("speed") double speed = 10;
    @Configure double staminaCost = 100;
    @Configure double staminaDrain = 50;

    public Flamethrower() {
        super("Flamethrower", "Create large streams of fire.", "ProjectKorra", "CORE", Skill.FIREBENDING);
    }

    @Override
    public void postProcessed() {}

    @Override
    protected AbilityInstance activate(AbilityUser user, Activation trigger, Event provider) {
        if (user.isOnCooldown(this)) {
            return null;
        }

        if (trigger == Activation.SNEAK_DOWN && user.getStamina().consume(staminaCost)) {
            return new FlamethrowerInstance(this, user);
        } else if (trigger == Activation.SNEAK_UP) {
            AbilityManager.getInstance(user, FlamethrowerInstance.class).ifPresent((inst) -> AbilityManager.remove(inst));
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
        return ImmutableSet.of(FlamethrowerInstance.class);
    }

	@Override
	public String getInstructions() {
		return "Hold sneak to create a stream of fire.";
	}
    
}
