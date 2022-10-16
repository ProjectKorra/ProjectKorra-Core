package com.projectkorra.core.game.lightningbending.redirection;

import java.util.Optional;

import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.util.Vector;

import com.projectkorra.core.UserManager;
import com.projectkorra.core.ability.Ability;
import com.projectkorra.core.ability.AbilityInstance;
import com.projectkorra.core.ability.AbilityManager;
import com.projectkorra.core.ability.AbilityUser;
import com.projectkorra.core.ability.activation.Activation;
import com.projectkorra.core.entity.PlayerUser;
import com.projectkorra.core.event.ability.InstanceDamageEntityEvent;
import com.projectkorra.core.game.AvatarSkills;
import com.projectkorra.core.game.lightningbending.bolt.BoltAbility;
import com.projectkorra.core.game.lightningbending.bolt.BoltInstance;
import com.projectkorra.core.util.configuration.Configure;

public class RedirectionPassive extends Ability {

	public static final Activation TRIGGER = Activation.of("redirection_trigger", "Redirection", false);

	@Configure
	double staminaCost = 0.05;

	public RedirectionPassive() {
		super("Redirection", "Lightningbenders can take in lightning and release it again!", "ProjectKorra", "CORE", AvatarSkills.LIGHTNINGBENDING);
	}

	@Override
	public void postProcessed() {}

	@Override
	protected AbilityInstance activate(AbilityUser user, Activation trigger, Event provider) {
		return null;
	}

	@EventHandler
	public void onBoltDamage(InstanceDamageEntityEvent event) {
		if (!(event.getInstance() instanceof BoltInstance)) {
			return;
		}

		PlayerUser user = UserManager.from(event.getTarget().getUniqueId()).getAs(PlayerUser.class);
		Optional<BoltInstance> bolt = user.getInstance(BoltInstance.class);
		
		if (!user.getEntity().isSneaking() || !user.getBoundAbility().filter((a) -> a instanceof BoltAbility).isPresent()) {
			return;
		} else if (bolt.isPresent() && !bolt.get().canRedirect()) {
			return;
		} else if (!user.getStamina().consume(staminaCost)) {
			return;
		}

		event.setCancelled(true);
		user.getEntity().setVelocity(new Vector(0, 0, 0));
		user.removeCooldown(AbilityManager.getAbility(BoltAbility.class).get());
		AbilityManager.activate(user, TRIGGER, event);
	}

	@EventHandler
	public void onLightningDamage(EntityDamageByEntityEvent event) {
		if (event.getCause() != DamageCause.LIGHTNING) {
			return;
		}

		PlayerUser user = UserManager.from(event.getEntity().getUniqueId()).getAs(PlayerUser.class);
		Optional<BoltInstance> bolt = user.getInstance(BoltInstance.class);
		
		if (!user.getEntity().isSneaking() || !user.getBoundAbility().filter((a) -> a instanceof BoltAbility).isPresent()) {
			return;
		} else if (bolt.isPresent() && !bolt.get().canRedirect()) {
			return;
		} else if (!user.getStamina().consume(staminaCost)) {
			return;
		}

		event.setCancelled(true);
		user.removeCooldown(AbilityManager.getAbility(BoltAbility.class).get());
		AbilityManager.activate(user, TRIGGER, event);
	}
}
