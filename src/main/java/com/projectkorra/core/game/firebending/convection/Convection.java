package com.projectkorra.core.game.firebending.convection;

import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import com.projectkorra.core.UserManager;
import com.projectkorra.core.ability.Ability;
import com.projectkorra.core.ability.AbilityInstance;
import com.projectkorra.core.ability.AbilityManager;
import com.projectkorra.core.ability.AbilityUser;
import com.projectkorra.core.ability.activation.Activation;
import com.projectkorra.core.ability.type.Bindable;
import com.projectkorra.core.event.ability.InstanceDamageEntityEvent;
import com.projectkorra.core.game.firebending.flamingwall.FlamingWallInstance;
import com.projectkorra.core.skill.Skill;
import com.projectkorra.core.util.configuration.Configure;

public class Convection extends Ability implements Bindable {

	@Configure("melt.range")
	double meltRange = 5;
	@Configure("melt.staminaDrain")
	double meltStaminaDrain = 0.05;
	@Configure("melt.time")
	long meltTime = 400;
	@Configure("extinguish.range")
	double extinguishRange = 15;
	@Configure("extinguish.radius")
	double extinguishRadius = 3;
	@Configure("extinguish.staminaCost")
	double extinguishStaminaCost = 0.15;
	@Configure("extinguish.cooldown")
	long extinguishCooldown = 1500;

	public Convection() {
		super("Convection", "Transfer heat into or out of nearby objects! Useful against fire and lava", "ProjectKorra", "CORE", Skill.FIREBENDING);
	}

	@Override
	public void postProcessed() {
	}

	@Override
	public String getInstructions() {
		return "Hold sneak to melt the ice block you are looking at over time, click to extinguish fire and cool lava in small patches.";
	}

	@Override
	protected AbilityInstance activate(AbilityUser user, Activation trigger, Event provider) {
		if (user.isOnCooldown(this)) {
			return null;
		}

		if (trigger == Activation.SNEAK_DOWN) {
			return new MeltingInstance(this, user);
		} else if (trigger == Activation.SNEAK_UP) {
			AbilityManager.getInstance(user, MeltingInstance.class).ifPresent(AbilityManager::remove);
		} else if (trigger == Activation.LEFT_CLICK && !AbilityManager.hasInstance(user, MeltingInstance.class)) {
			return new ExtinguishInstance(this, user);
		}

		return null;
	}

	@Override
	protected void onRegister() {
	}

	@EventHandler
	private void onBurnDamage(EntityDamageEvent event) {
		if (event.getCause() != DamageCause.FIRE_TICK && event.getCause() != DamageCause.FIRE) {
			return;
		}

		AbilityUser user = UserManager.get(event.getEntity().getUniqueId());

		if (user == null) {
			return;
		} else if (!user.getBoundAbility().filter((a) -> a instanceof Convection).isPresent()) {
			return;
		}

		event.setCancelled(true);
		event.getEntity().setFireTicks(0);
	}

	@EventHandler
	private void onAbilityDamage(InstanceDamageEntityEvent event) {
		if (!(event.getInstance() instanceof FlamingWallInstance)) {
			return;
		}

		AbilityUser user = UserManager.get(event.getTarget().getUniqueId());

		if (user == null) {
			return;
		} else if (!user.getBoundAbility().filter((a) -> a instanceof Convection).isPresent()) {
			return;
		}

		event.setCancelled(true);
	}
}
