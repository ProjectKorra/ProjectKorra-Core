package com.projectkorra.core.game.lavabending.lavawave;

import org.bukkit.entity.FallingBlock;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityChangeBlockEvent;

import com.projectkorra.core.ability.Ability;
import com.projectkorra.core.ability.AbilityInstance;
import com.projectkorra.core.ability.AbilityManager;
import com.projectkorra.core.ability.AbilityUser;
import com.projectkorra.core.ability.activation.Activation;
import com.projectkorra.core.ability.type.Bindable;
import com.projectkorra.core.skill.Skill;
import com.projectkorra.core.util.configuration.Configure;

public class LavaWave extends Ability implements Bindable {
	
	@Configure double speed = 16;
	@Configure double range = 30;
	@Configure double width = 2.5;
	@Configure double damage = 5;
	@Configure double staminaDrain = 0.2;
	@Configure long coolTime = 4000;
	@Configure long cooldown = 3000;

	public LavaWave() {
		super("LavaWave", "Create a wave of lava ahead of you", "ProjectKorra", "CORE", Skill.of("lavabending"));
	}

	@Override
	public String getInstructions() {
		return "Hold sneak to charge the ability";
	}

	@Override
	protected AbilityInstance activate(AbilityUser user, Activation trigger, Event provider) {
		if (user.hasCooldown(this)) {
			return null;
		}
		
		if (trigger == Activation.SNEAK_DOWN) {
			return new LavaWaveInstance(this, user);
		} else if (trigger == Activation.SNEAK_UP) {
			user.getInstance(LavaWaveInstance.class).ifPresent(AbilityManager::remove);
		}
		
		return null;
	}

	@EventHandler
	private void onChange(EntityChangeBlockEvent event) {
		if (!(event.getEntity() instanceof FallingBlock)) {
			return;
		} else if (!event.getEntity().hasMetadata("lavawave")) {
			return;
		}
	
		event.setCancelled(true);
	}
}
