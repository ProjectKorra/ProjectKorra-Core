package com.projectkorra.core.system.entity;

import java.util.Collection;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.projectkorra.core.system.ability.AbilityBinds;
import com.projectkorra.core.system.skill.Skill;
import com.projectkorra.core.util.RegionUtil;

public class PlayerUser extends User<Player> {

	public PlayerUser(Player player, Collection<Skill> skills, Collection<Skill> toggled, AbilityBinds binds) {
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

	@Override
	public boolean checkDefaultProtections(Location loc) {
		return loc.getBlock().getType().isSolid() ? RegionUtil.canBreak(entity, loc) : RegionUtil.canBuild(entity, loc);
	}
}
