package com.projectkorra.core.game.earthbending.earthbolt;

import java.util.Optional;

import org.bukkit.entity.FallingBlock;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityChangeBlockEvent;

import com.projectkorra.core.ability.Ability;
import com.projectkorra.core.ability.AbilityInstance;
import com.projectkorra.core.ability.AbilityUser;
import com.projectkorra.core.ability.activation.Activation;
import com.projectkorra.core.ability.type.Bindable;
import com.projectkorra.core.game.AvatarSkills;
import com.projectkorra.core.util.configuration.Configure;

public class Earthbolt extends Ability implements Bindable {

	@Configure
	double damage = 4;
	@Configure
	double launchSpeed = 35;
	@Configure
	double selectRange = 7;
	@Configure
	long cooldown = 3000;
	@Configure
	double staminaCost = 0.15;

	public Earthbolt() {
		super("Earthbolt", "Raise and launch large pieces of rock at your enemies!", "ProjectKorra", "CORE", AvatarSkills.EARTHBENDING);
	}

	@Override
	public String getInstructions() {
		return "Hold sneak while looking at earth to pull it in front of you, then release sneak to throw it.";
	}

	@Override
	protected AbilityInstance activate(AbilityUser user, Activation trigger, Event provider) {
		if (user.hasCooldown(this)) {
			return null;
		}
		
		Optional<EarthboltInstance> bolt = user.getInstance(EarthboltInstance.class);

		if (trigger == Activation.SNEAK_DOWN && !bolt.isPresent()) {
			return new EarthboltInstance(this, user);
		}
		
		if (trigger == Activation.LEFT_CLICK) {
			bolt.ifPresent(EarthboltInstance::launch);
		}

		return null;
	}

	@EventHandler
	private void onPhysicsBlock(EntityChangeBlockEvent event) {
		if (!(event.getEntity() instanceof FallingBlock)) {
			return;
		}

		if (!event.getEntity().hasMetadata("earthbolt")) {
			return;
		}

		event.setCancelled(true);
		((EarthboltInstance) event.getEntity().getMetadata("earthbolt").get(0).value()).blockLanding(event.getBlock(), event.getBlockData());
	}
}
