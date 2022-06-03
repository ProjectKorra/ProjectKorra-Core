package com.projectkorra.core.entity;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.MainHand;

import com.projectkorra.core.util.Regions;

public class PlayerUser extends User<Player> {

	public PlayerUser(Player player) {
		super(player);
	}

	@Override
	public int getCurrentSlot() {
		return typed.getInventory().getHeldItemSlot();
	}

	@Override
	public void sendMessage(String message) {
		typed.sendMessage(message);
	}

	@Override
	public boolean hasPermission(String perm) {
		return typed.hasPermission(perm);
	}

	@Override
	public boolean shouldRemove() {
		return !typed.isOnline();
	}

	@Override
	public boolean checkDefaultProtections(Location loc) {
		return loc.getBlock().getType().isSolid() ? Regions.canBreak(typed, loc) : Regions.canBuild(typed, loc);
	}

	@Override
	public MainHand getMainHand() {
		return typed.getMainHand();
	}
	
	@Override
	public boolean isOnline() {
		return typed.isOnline();
	}
}
