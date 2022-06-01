package com.projectkorra.core.game.firebending.passives.sunlight;

import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;

import com.projectkorra.core.ability.Ability;
import com.projectkorra.core.ability.AbilityInstance;
import com.projectkorra.core.ability.AbilityManager;
import com.projectkorra.core.ability.AbilityUser;
import com.projectkorra.core.ability.activation.Activation;
import com.projectkorra.core.ability.type.Passive;
import com.projectkorra.core.event.ability.InstanceStartEvent;
import com.projectkorra.core.skill.Skill;
import com.projectkorra.core.util.configuration.Configure;

public class SunlightPassive extends Ability implements Passive {

	@Configure("multiplier.damage")
	double modDamage = 1.05;
	@Configure("multiplier.speed")
	double modSpeed = 1.05;
	@Configure("multiplier.range")
	double modRange = 1.05;
	@Configure("multiplier.size")
	double modSize = 1.025;

	public SunlightPassive() {
		super("Sunlight", "Firebenders are stronger during the day!", "ProjectKorra", "CORE", Skill.FIREBENDING);
	}

	@Override
	public void postProcessed() {
	}

	@Override
	public Activation getTrigger() {
		return Activation.PASSIVE;
	}

	@Override
	protected AbilityInstance activate(AbilityUser user, Activation trigger, Event provider) {
		return null;
	}

	@Override
	protected void onRegister() {
	}

	@EventHandler
	public void onAbilityStart(InstanceStartEvent event) {
		if (event.getInstance().getProvider().getSkill() != Skill.FIREBENDING) {
			return;
		}

		AbilityManager.getInstance(event.getInstance().getUser(), SunlightInstance.class).ifPresent((sl) -> sl.applyBuffs(event.getInstance()));
	}
}
