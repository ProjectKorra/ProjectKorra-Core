package com.projectkorra.core.ability.type;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import com.projectkorra.core.ability.Ability;
import com.projectkorra.core.ability.AbilityManager;
import com.projectkorra.core.event.user.UserActivationEvent;
import com.projectkorra.core.skill.Skill;

public abstract class ActivatedPassive extends Ability {

	public ActivatedPassive(String name, String description, String author, String version, Skill skill) {
		super(name, description, author, version, skill);
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public final void onActivation(UserActivationEvent event) {
		if (!this.canActivate(event.getUser(), event.getTrigger())) {
			return;
		}
		
		AbilityManager.start(this.activate(event.getUser(), event.getTrigger(), event.getProvider()));
	}
}
