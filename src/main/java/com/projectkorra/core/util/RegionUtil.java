package com.projectkorra.core.util;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public final class RegionUtil {

	private RegionUtil() {}
	
	private static final ItemStack PLACEHOLDER = new ItemStack(Material.AIR);
	
	/**
	 * Checks to see if the location is protected in such a way that the player either cannot
	 * build upon or cannot break the existing block, if one exists.
	 * @param player Who to check against protections
	 * @param loc Where to check for protections
	 * @return true if the location is protected from the player
	 */
	public static boolean isProtected(Player player, Location loc) {
		return loc.getBlock().getType().isSolid() ? canBreak(player, loc) : canBuild(player, loc);
	}
	
	/**
	 * Calls a fake {@link BlockPlaceEvent} to check if the player can build at the given location.
	 * If you want to ignore this fake event, check {@link RegionUtil#isFake(BlockPlaceEvent)}
	 * @param player Who to check
	 * @param loc Where to check
	 * @return true if the fake event isn't cancelled
	 */
	public static boolean canBuild(Player player, Location loc) {
		return !EventUtil.call(new UserCheckCanBuildEvent(player, loc)).isCancelled();
	}
	
	/**
	 * Calls a fake {@link BlockBreakEvent} to check if the player can destroy at the given location.
	 * If you want to ignore this fake event, check {@link RegionUtil#isFake(BlockPlaceEvent)}
	 * @param player Who to check
	 * @param loc Where to check
	 * @return true if the fake event isn't cancelled
	 */
	public static boolean canBreak(Player player, Location loc) {
		return !EventUtil.call(new UserCheckCanBreakEvent(player, loc)).isCancelled();
	}
	
	/**
	 * Checks to see if the given event was called by this utility
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
