package com.projectkorra.core.game.physique.landing;

import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import com.projectkorra.core.UserManager;
import com.projectkorra.core.ability.Ability;
import com.projectkorra.core.ability.AbilityInstance;
import com.projectkorra.core.ability.AbilityUser;
import com.projectkorra.core.ability.activation.Activation;
import com.projectkorra.core.skill.Skill;
import com.projectkorra.core.util.configuration.Configure;

public class LandingPassive extends Ability {

	@Configure("damageReduction")
	private double reduction = 5;

	public LandingPassive() {
		super("Landing", "Break your fall so you don't take as much damage!", "ProjectKorra", "CORE", Skill.of("physique"));
	}

	@Override
	public void postProcessed() {
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
	public boolean hasPassive() {
		return true;
	}

	@EventHandler
	public void onFallDamage(EntityDamageEvent event) {
		if (event.getCause() != DamageCause.FALL) {
			return;
		}

		AbilityUser user = UserManager.from(event.getEntity().getUniqueId()).get();
		if (user == null) {
			return;
		}

		user.getInstance(LandingInstance.class).ifPresent(a -> a.reduceDamage(event));
	}
}
