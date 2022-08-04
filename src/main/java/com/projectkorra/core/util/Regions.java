package com.projectkorra.core.util;

import java.util.function.BiPredicate;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import com.projectkorra.core.ability.AbilityUser;

public final class Regions {

	private Regions() {
	}

	private static boolean defaultProtection = true; // make configurable
	private static final ItemStack PLACEHOLDER = new ItemStack(Material.AIR);
	private static BiPredicate<AbilityUser, Location> protections = null;

	/**
	 * Adds a region protection check to the current predicate used by
	 * {@link #isProtected(AbilityUser, Location)}
	 * 
	 * @param protection new region protection check, passing null will be silently
	 *                   ignored
	 */
	public static void addProtection(BiPredicate<AbilityUser, Location> protection) {
		if (protection == null) {
			return;
		}

		protections = (protections != null) ? protections.and(protection) : protection;
	}

	/**
	 * Checks the existing region protection checks to see if the given location is
	 * protected from the given user. If no region protection checks exist and
	 * default protection is enabled, this will pass the call along to
	 * {@link AbilityUser#checkDefaultProtections(Location)}.
	 * 
	 * @param user Who to check against protections
	 * @param loc  Where to check for protections
	 * @return true if the location is protected from the user
	 */
	public static boolean isProtected(AbilityUser user, Location loc) {
		if (protections == null) {
			if (!defaultProtection) {
				return false;
			}

			return user.checkDefaultProtections(loc);
		}

		return protections.test(user, loc);
	}

	/**
	 * Calls a fake {@link BlockPlaceEvent} to check if the player can build at the
	 * given location. If you want to ignore this fake event, check
	 * {@link Regions#isFake(BlockPlaceEvent)}
	 * 
	 * @param player Who to check
	 * @param loc    Where to check
	 * @return true if the fake event isn't cancelled
	 */
	public static boolean canBuild(Player player, Location loc) {
		return !Events.call(new UserCheckCanBuildEvent(player, loc)).isCancelled();
	}

	/**
	 * Calls a fake {@link BlockBreakEvent} to check if the player can destroy at
	 * the given location. If you want to ignore this fake event, check
	 * {@link Regions#isFake(BlockPlaceEvent)}
	 * 
	 * @param player Who to check
	 * @param loc    Where to check
	 * @return true if the fake event isn't cancelled
	 */
	public static boolean canBreak(Player player, Location loc) {
		return !Events.call(new UserCheckCanBreakEvent(player, loc)).isCancelled();
	}

	/**
	 * Checks to see if the given event was called by this utility
	 * 
	 * @param event The event to check
	 * @return true if the event was called by this utility
	 */
	public static boolean isFake(Event event) {
		return event instanceof UserCheckCanBuildEvent || event instanceof UserCheckCanBreakEvent;
	}

	public static class UserCheckCanBuildEvent extends BlockPlaceEvent {

		public UserCheckCanBuildEvent(Player player, Location loc) {
			super(loc.getBlock(), loc.getBlock().getState(), loc.getBlock().getRelative(BlockFace.DOWN), PLACEHOLDER, player, true, EquipmentSlot.HAND);
		}
	}

	public static class UserCheckCanBreakEvent extends BlockBreakEvent {

		public UserCheckCanBreakEvent(Player player, Location loc) {
			super(loc.getBlock(), player);
			this.setDropItems(false);
		}
	}
}
