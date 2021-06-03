package com.projectkorra.core.util;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public final class RegionUtil {

	private RegionUtil() {}
	
	private static final ItemStack HAND_ITEM = new ItemStack(Material.BARRIER);
	
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
		return !EventUtil.call(new BlockPlaceEvent(loc.getBlock(), loc.getBlock().getState(), loc.getBlock().getRelative(BlockFace.DOWN), HAND_ITEM, player, true, EquipmentSlot.HAND)).isCancelled();
	}
	
	/**
	 * Calls a fake {@link BlockBreakEvent} to check if the player can destroy at the given location
	 * @param player Who to check
	 * @param loc Where to check
	 * @return true if the fake event isn't cancelled
	 */
	public static boolean canBreak(Player player, Location loc) {
		return !EventUtil.call(new BlockBreakEvent(loc.getBlock(), player)).isCancelled();
	}
	
	/**
	 * Checks to see if the given {@link BlockPlaceEvent} was called by this utility
	 * @param event The {@link BlockPlaceEvent} to check 
	 * @return true if the event was called by this utility
	 */
	public static boolean isFake(BlockPlaceEvent event) {
		return event.getItemInHand() == HAND_ITEM;
	}
}
