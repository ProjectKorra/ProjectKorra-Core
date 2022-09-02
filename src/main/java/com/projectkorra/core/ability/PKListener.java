package com.projectkorra.core.ability;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.event.player.PlayerToggleSprintEvent;
import org.bukkit.inventory.EquipmentSlot;

import com.projectkorra.core.game.InputType;

public class PKListener implements Listener {
	
	public long timeStart;

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
			BendingUser user = BendingUserManager.getBendingUser(p);
			user.does(!p.isSneaking() ? InputType.SHIFT_DOWN : InputType.SHIFT_UP, event);

			AbilityManager.activate(user);
		}
	}
	@EventHandler
	public void onPlayerToggle(PlayerToggleSprintEvent event) {
		Player p = event.getPlayer();
		if(p != null) {
			BendingUser user = BendingUserManager.getBendingUser(p);
			user.does(p.isSprinting() ? InputType.SPRINT_ON : InputType.SPRINT_OFF, event);

			AbilityManager.activate(user);
		}
	}
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		Player p = event.getPlayer();
		if(p != null) {
			BendingUser user = BendingUserManager.getBendingUser(p);
			if(event.getHand() == EquipmentSlot.HAND) {
				
				switch(event.getAction()) {
					case LEFT_CLICK_AIR:
						user.does(InputType.LEFT_CLICK, event);
						break;
					case LEFT_CLICK_BLOCK:
						user.does(InputType.LEFT_CLICK_BLOCK, event);
						break;
					case RIGHT_CLICK_AIR:
						user.does(InputType.RIGHT_CLICK_AIR, event);
						break;
					case RIGHT_CLICK_BLOCK:
						user.does(InputType.RIGHT_CLICK_BLOCK, event);
						break;
					case PHYSICAL:
					default:
						user.does(InputType.NONE, event);
						break;
				}

				AbilityManager.activate(user);
			} 
		}
	}

	

}
