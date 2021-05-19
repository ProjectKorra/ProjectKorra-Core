package com.projectkorra.core.util.gui;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;

public class DisplayItem {
	
	@FunctionalInterface
	public static interface ClickAction {
		public void accept(Player player, InventoryAction action, InventoryGui gui);
		
		public default ClickAction andThen(ClickAction action) {
			return (p, a, g) -> {accept(p, a, g); action.accept(p, a, g);};
		}
	}
	
	/**
	 * A {@link DisplayItem} with no item or click action
	 */
	public static final DisplayItem EMPTY = new DisplayItem(null, null, true);
	
	/**
	 * A {@link DisplayItem} for closing the open GUI when clicked
	 */
	public static final DisplayItem EXIT_BUTTON = DisplayItem.functional(ChatColor.RED + "EXIT", Material.BARRIER, Arrays.asList(ChatColor.GRAY + "Click to exit this GUI"), (p, a, g) -> g.close(p), true);

	private ItemStack itemStack;
	private ClickAction action;
	private boolean replaceable;
	
	/**
	 * Create a new {@link DisplayItem} for a GUI with the given ItemStack
	 * to display and action to do when the item is clicked.
	 * @param item GUI display item
	 * @param action What to do when the item is clicked
	 * @param replaceable Whether or not this item can be replaced within a gui
	 */
	public DisplayItem(ItemStack item, ClickAction action, boolean replaceable) {
		this.itemStack = item;
		this.action = action;
		this.replaceable = replaceable;
	}
	
	/**
	 * Get the {@link ItemStack} that is used as display. Changes to the returned
	 * {@link ItemStack} are not guaranteed to happen to the one in the inventory.
	 * @return display {@link ItemStack}
	 */
	public ItemStack getItemStack() {
		return itemStack;
	}
	
	/**
	 * Get the {@link ClickAction} to do when this {@link DisplayItem} is interacted with
	 * @return click {@link ClickAction}
	 */
	public Optional<ClickAction> getAction() {
		return Optional.ofNullable(action);
	}
	
	/**
	 * Get whether or not this item can be replaced within a gui
	 * @return true if the item can be replaced
	 */
	public boolean isReplaceable() {
		return replaceable;
	}
	
	/**
	 * Create a new {@link DisplayItem} using the given name, type, and lore,
	 * and the given click action
	 * @param name Name for the ItemStack
	 * @param type Type for the ItemStack
	 * @param lore Lore for the ItemStack
	 * @param action What to do when the item is clicked
	 * @param replaceable Whether or not this can be replaced in a gui, usually false for functional elements
	 * @return a functional {@link DisplayItem}
	 */
	public static DisplayItem functional(String name, Material type, List<String> lore, ClickAction action, boolean replaceable) {
		ItemStack item = new ItemStack(type);
		ItemMeta meta = item.getItemMeta();
		
		meta.setDisplayName(name);
		meta.setLore(lore);
		
		item.setItemMeta(meta);
		return new DisplayItem(item, action, replaceable);
	}
	
	/**
	 * Create a new {@link DisplayItem} using the given name, type, and lore. This
	 * item is for aesthetic and will do nothing when clicked.
	 * @param name Name for the item
	 * @param type Type for the item
	 * @param lore Lore for the item
	 * @param replaceable Whether or not this can be replaced in a gui, usually true for visual elements
	 * @return an aesthetic {@link DisplayItem}
	 */
	public static DisplayItem aesthetic(String name, Material type, List<String> lore, boolean replaceable) {
		ItemStack item = new ItemStack(type);
		ItemMeta meta = item.getItemMeta();
		
		meta.setDisplayName(name);
		meta.setLore(lore);
		
		item.setItemMeta(meta);
		return new DisplayItem(item, null, replaceable);
	}
}
