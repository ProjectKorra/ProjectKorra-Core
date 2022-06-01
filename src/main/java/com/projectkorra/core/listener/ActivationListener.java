package com.projectkorra.core.listener;

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
import com.projectkorra.core.ability.AbilityManager;
import com.projectkorra.core.ability.AbilityUser;
import com.projectkorra.core.ability.activation.Activation;
import com.projectkorra.core.event.user.UserCreationEvent;
import com.projectkorra.core.event.user.UserSkillChangeEvent;

/**
 * Listens for events relating to ability activations
 */
public class ActivationListener implements Listener {

	@EventHandler(priority = EventPriority.HIGHEST)
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

	@EventHandler(priority = EventPriority.HIGHEST)
	private void onInteractEntity(PlayerInteractAtEntityEvent event) {
		AbilityUser user = UserManager.get(event.getPlayer().getUniqueId());

		if (user == null || event.getHand() != EquipmentSlot.HAND) {
			return;
		}

		AbilityManager.activate(user, Activation.RIGHT_CLICK_ENTITY, event);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	private void onSneak(PlayerToggleSneakEvent event) {
		AbilityUser user = UserManager.get(event.getPlayer().getUniqueId());

		if (user == null) {
			return;
		}

		AbilityManager.activate(user, event.isSneaking() ? Activation.SNEAK_DOWN : Activation.SNEAK_UP, event);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	private void onDamage(EntityDamageEvent event) {
		AbilityUser user = UserManager.get(event.getEntity().getUniqueId());

		if (user == null) {
			return;
		}

		AbilityManager.activate(user, Activation.DAMAGED, event);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	private void onSprint(PlayerToggleSprintEvent event) {
		AbilityUser user = UserManager.get(event.getPlayer().getUniqueId());

		if (user == null) {
			return;
		}

		AbilityManager.activate(user, event.isSprinting() ? Activation.SPRINT_ON : Activation.SPRINT_OFF, event);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	private void onUserCreate(UserCreationEvent event) {
		AbilityManager.refreshPassives(event.getUser());
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	private void onUserSkillsChange(UserSkillChangeEvent event) {
		if (event.getHolder() instanceof AbilityUser) {
			AbilityManager.refreshPassives((AbilityUser) event.getHolder());
		}
	}
}
