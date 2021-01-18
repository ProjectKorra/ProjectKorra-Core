package com.projectkorra.core.system.user;

import java.util.Collection;

import org.bukkit.entity.Player;

import com.projectkorra.core.system.ability.AbilityBinds;
import com.projectkorra.core.system.skill.Skill;

public class SkilledPlayer extends SkilledEntity<Player> {

	public SkilledPlayer(Player player, Collection<Skill> skills, Collection<Skill> toggled, AbilityBinds binds) {
		super(player, skills, toggled, binds);
	}

	@Override
	public int getCurrentSlot() {
		return entity.getInventory().getHeldItemSlot();
	}

	@Override
	public void sendMessage(String message) {
		entity.sendMessage(message);
	}

	@Override
	public boolean hasPermission(String perm) {
		return entity.hasPermission(perm);
	}
	
	@Override
	public boolean shouldRemove() {
		return !entity.isOnline();
	}
}
