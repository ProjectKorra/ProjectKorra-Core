package com.projectkorra.core.system.user;

import java.util.Set;

import org.bukkit.entity.Player;

import com.projectkorra.core.system.ability.Ability;
import com.projectkorra.core.system.skill.Skill;

public class SkilledPlayer extends SkilledEntity {

	SkilledPlayer(Player player, Set<Skill> skills, Set<Skill> toggled, Ability[] binds) {
		super(player, skills, toggled, binds);
	}

	@Override
	public int getCurrentSlot() {
		return ((Player) getEntity()).getInventory().getHeldItemSlot();
	}

	@Override
	public void sendMessage(String message) {
		((Player) getEntity()).sendMessage(message);
	}

	@Override
	public boolean hasPermission(String perm) {
		return ((Player) getEntity()).hasPermission(perm);
	}
}
