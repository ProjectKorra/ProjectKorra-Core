package com.projectkorra.core.listener;

import com.projectkorra.core.ProjectKorra;
import com.projectkorra.core.UserManager;
import com.projectkorra.core.ability.AbilityBoard;
import com.projectkorra.core.ability.AbilityManager;
import com.projectkorra.core.ability.AbilityUser;
import com.projectkorra.core.entity.PlayerUser;
import com.projectkorra.core.event.user.UserBindChangeEvent;
import com.projectkorra.core.event.user.UserCooldownEndEvent;
import com.projectkorra.core.event.user.UserCooldownStartEvent;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Listens for events relating to players
 */
public class PlayerListener implements Listener {

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		PlayerUser user = (PlayerUser) UserManager.load(event.getPlayer());
		new BukkitRunnable() {

			@Override
			public void run() {
				AbilityBoard.from(user).ifPresent(AbilityBoard::show);
			}

		}.runTaskLater(JavaPlugin.getPlugin(ProjectKorra.class), 2);
	}

	private void playerLeave(Player player) {
		AbilityUser user = UserManager.from(player);
		if (user == null) {
			return;
		}

		AbilityManager.removeAll(user);
		UserManager.save(player);
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		playerLeave(event.getPlayer());
	}

	@EventHandler
	public void onPlayerKick(PlayerKickEvent event) {
		playerLeave(event.getPlayer());
	}

	@EventHandler
	public void onSlotChange(PlayerItemHeldEvent event) {
		AbilityBoard.from(event.getPlayer()).ifPresent(b -> b.switchSlot(event.getNewSlot()));
	}

	@EventHandler
	public void onCooldownStart(UserCooldownStartEvent event) {
		if (!(event.getUser() instanceof PlayerUser)) {
			return;
		}

		AbilityBoard.from((PlayerUser) event.getUser()).ifPresent(b -> b.cooldown(event.getTag(), true));
	}

	@EventHandler
	public void onCooldownEnd(UserCooldownEndEvent event) {
		if (!(event.getUser() instanceof PlayerUser)) {
			return;
		}

		AbilityBoard.from((PlayerUser) event.getUser()).ifPresent(b -> b.cooldown(event.getCooldown().getTag(), false));
	}

	@EventHandler
	public void onBindChange(UserBindChangeEvent event) {
		if (!(event.getUser() instanceof PlayerUser)) {
			return;
		}

		AbilityBoard.from((PlayerUser) event.getUser()).ifPresent(b -> b.updateBind(event.getSlot(), event.getResult()));
	}
}
