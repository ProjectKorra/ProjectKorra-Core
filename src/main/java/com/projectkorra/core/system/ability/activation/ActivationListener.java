package com.projectkorra.core.system.ability.activation;

import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.event.player.PlayerToggleSprintEvent;
import org.bukkit.inventory.EquipmentSlot;

import com.projectkorra.core.UserManager;
import com.projectkorra.core.system.ability.AbilityManager;
import com.projectkorra.core.system.ability.AbilityUser;

public class ActivationListener implements Listener {

	@EventHandler(priority = EventPriority.LOWEST)
	private void onInteract(PlayerInteractEvent event) {
		AbilityUser user = UserManager.get(event.getPlayer().getUniqueId());
		
		if (user == null || event.getHand() != EquipmentSlot.HAND) {
			return;
		} else if (event.getAction() == Action.LEFT_CLICK_AIR || (event.getAction() == Action.LEFT_CLICK_BLOCK && event.useInteractedBlock() != Event.Result.DENY)) {
			AbilityManager.activate(user, Activation.LEFT_CLICK, event);
		} else if (event.getAction() == Action.RIGHT_CLICK_BLOCK && (event.useInteractedBlock() != Event.Result.DENY)) {
			AbilityManager.activate(user, Activation.RIGHT_CLICK_BLOCK, event);
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	private void onInteractEntity(PlayerInteractAtEntityEvent event) {
		AbilityUser user = UserManager.get(event.getPlayer().getUniqueId());
		
		if (user == null || event.getHand() != EquipmentSlot.HAND) {
			return;
		}
		
		AbilityManager.activate(user, Activation.RIGHT_CLICK_ENTITY, event);
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	private void onSneak(PlayerToggleSneakEvent event) {
		AbilityUser user = UserManager.get(event.getPlayer().getUniqueId());
		
		if (user == null) {
			return;
		}
		
		AbilityManager.activate(user, event.isSneaking() ? Activation.SNEAK_DOWN : Activation.SNEAK_UP, event);
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	private void onDamage(EntityDamageEvent event) {
		AbilityUser user = UserManager.get(event.getEntity().getUniqueId());
		
		if (user == null) {
			return;
		}
		
		AbilityManager.activate(user, Activation.DAMAGED, event);
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	private void onSprint(PlayerToggleSprintEvent event) {
		AbilityUser user = UserManager.get(event.getPlayer().getUniqueId());
		
		if (user == null) {
			return;
		}
		
		AbilityManager.activate(user, event.isSprinting() ? Activation.SPRINT_ON : Activation.SPRINT_OFF, event);
	}
}
