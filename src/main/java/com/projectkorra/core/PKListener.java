package com.projectkorra.core;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.event.player.PlayerToggleSprintEvent;

import com.projectkorra.core.ability.BendingUserManager;
import com.projectkorra.core.game.InputType;

public class PKListener implements Listener {
	
	@EventHandler
	public void onPlayerLogin(PlayerLoginEvent event) {
		BendingUserManager.loadBendingUser(event.getPlayer());
	}
	
	@EventHandler
	public void onPlayerLogout(PlayerQuitEvent event) {
		BendingUserManager.saveBendingUser(event.getPlayer());
	}
	
	@EventHandler
	public void onPlayerSneak(PlayerToggleSneakEvent event) {
		Player p = event.getPlayer();
		if(p != null) {
			BendingUserManager.getBendingUser(p).does(p.isSneaking() ? InputType.SHIFT_DOWN : InputType.SHIFT_UP);
		}
	}
	@EventHandler
	public void onPlayerToggle(PlayerToggleSprintEvent event) {
		Player p = event.getPlayer();
		if(p != null) {
			BendingUserManager.getBendingUser(p).does(p.isSprinting() ? InputType.SPRINT_ON : InputType.SPRINT_OFF);
		}
	}
	@EventHandler
	public void onPlayerToggle(PlayerInteractEvent event) {
		Player p = event.getPlayer();
		if(p != null) {
			BendingUserManager.getBendingUser(p).does(p.isSprinting() ? InputType.SPRINT_ON : InputType.SPRINT_OFF);
		}
	}
}
