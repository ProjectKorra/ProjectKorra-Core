package com.projectkorra.core.system.user;

import java.util.Collection;

import org.bukkit.entity.Mob;

import com.projectkorra.core.ProjectKorra;
import com.projectkorra.core.system.ability.AbilityBinds;
import com.projectkorra.core.system.skill.Skill;

public class SkilledMob extends SkilledEntity<Mob> {
	
	private MobSlotAI ai;

	public SkilledMob(Mob entity, Collection<Skill> skills, Collection<Skill> toggled, AbilityBinds binds, MobSlotAI ai) {
		super(entity, skills, toggled, binds);
		this.ai = ai;
	}

	@Override
	public boolean shouldRemove() {
		return entity.isDead();
	}

	@Override
	public int getCurrentSlot() {
		return ai.slot();
	}

	@Override
	public void sendMessage(String message) {
		ProjectKorra.messageConsole(message);
	}

	@Override
	public boolean hasPermission(String perm) {
		return entity.hasPermission(perm);
	}

}
