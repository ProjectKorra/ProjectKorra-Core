package com.projectkorra.core.api;

import org.bukkit.Location;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerStatisticIncrementEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.event.player.PlayerToggleSprintEvent;
import org.bukkit.event.player.PlayerVelocityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.util.Vector;

import com.projectkorra.core.api.game.InputType;

public class PKListener implements Listener {

	public long timeStart;

	@EventHandler
	public void onPlayerLogin(PlayerLoginEvent event) {
		UserManager.loadUser(event.getPlayer());
	}

	@EventHandler
	public void onPlayerLogout(PlayerQuitEvent event) {
		UserManager.saveUser(event.getPlayer());
	}

	@EventHandler
	public void onPlayerSneak(PlayerToggleSneakEvent event) {
		Player p = event.getPlayer();
		if (p != null) {
			User user = UserManager.getUser(p);
			user.does(!p.isSneaking() ? InputType.SHIFT_DOWN : InputType.SHIFT_UP, event);

			AbilityManager.activate(user);
		}
	}

	@EventHandler
	public void onPlayerSprint(PlayerToggleSprintEvent event) {
		Player p = event.getPlayer();
		if (p != null) {
			User user = UserManager.getUser(p);
			user.does(!p.isSprinting() ? InputType.SPRINT_ON : InputType.SPRINT_OFF, event);

			AbilityManager.activate(user);
		}
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		Player p = event.getPlayer();
		if (p != null) {
			User user = UserManager.getUser(p);
			if (event.getHand() == EquipmentSlot.HAND) {

				switch (event.getAction()) {
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
					default:
						return;
				}

				AbilityManager.activate(user);
			}
		}
	}

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		Player p = event.getPlayer();
		if (p != null) {
			User user = UserManager.getUser(p);

			Location from = event.getFrom();
			Location to = event.getTo();

			Vector direction = to.toVector().subtract(from.toVector()).clone().normalize();
			Vector forward = p.getLocation().getDirection().clone().setY(0).normalize();
			Vector left = p.getLocation().getPitch() % 90 == 0 ? forward.rotateAroundY(90)
					: forward.clone().setY(0).normalize();
			Vector up = new Vector(0, 1, 0);

			double dL = direction.dot(left);
			double dU = direction.dot(up);
			double dF = direction.dot(forward);

			if (Math.abs(dL) > 0) {
				user.does(dL > 0 ? InputType.LEFT : InputType.RIGHT, event);
			}
			if (Math.abs(dU) > 0) {
				user.does(dU > 0 ? InputType.UP : InputType.DOWN, event);
			}
			if (Math.abs(dF) > 0) {
				user.does(dF > 0 ? InputType.FORWARD : InputType.BACKWARD, event);
			}

			AbilityManager.activateMovement(user);
		}
	}

	@EventHandler
	public void onPlayerToggleFlight(PlayerToggleFlightEvent event) {
		Player p = event.getPlayer();
		if (p != null) {
			User user = UserManager.getUser(p);
			user.does(!p.isFlying() ? InputType.FLIGHT_ON : InputType.FLIGHT_OFF, event);

			AbilityManager.activate(user);
		}
	}

	@EventHandler
	public void onPlayerSlotChange(PlayerItemHeldEvent event) {
		Player p = event.getPlayer();
		if (p != null) {
			PlayerUser user = (PlayerUser) UserManager.getUser(p);

			user.setSlot(event.getNewSlot());

			user.does(InputType.SLOT_CHANGE, event);

			AbilityManager.activate(user);
		}
	}

	@EventHandler
	public void onPlayerJump(PlayerStatisticIncrementEvent event) {
		Player p = event.getPlayer();
		if (p != null) {
			PlayerUser user = (PlayerUser) UserManager.getUser(p);
			if (event.getStatistic() == Statistic.JUMP) {
				user.does(InputType.JUMP, event);
				AbilityManager.activate(user);
			}
		}
	}

	@EventHandler
	public void onPlayerVelocity(PlayerVelocityEvent event) {
		Player p = event.getPlayer();
		if (p != null) {
			PlayerUser user = (PlayerUser) UserManager.getUser(p);
			if (event.getVelocity().equals(new Vector())) {
				user.does(InputType.KNOCK_BACK, event);
			}
		}
	}

	@EventHandler
	public void onPlayerSwapHandsC(PlayerSwapHandItemsEvent event) {
		Player p = event.getPlayer();
		if (p != null) {
			PlayerUser user = (PlayerUser) UserManager.getUser(p);

			user.does(InputType.SWAP_HANDS, event);

			AbilityManager.activate(user);
		}
	}

	@EventHandler
	public void onPlayerDrop(PlayerDropItemEvent event) {
		Player p = event.getPlayer();
		if (p != null) {
			PlayerUser user = (PlayerUser) UserManager.getUser(p);

			user.does(InputType.THROW_ITEM, event);

			AbilityManager.activate(user);
		}
	}
}
