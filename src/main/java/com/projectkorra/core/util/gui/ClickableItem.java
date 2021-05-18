package com.projectkorra.core.util.gui;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ClickableItem {
	
	public static interface Action {
		public void accept(Player player, InventoryAction action, InventoryGui gui);
		
		public default Action andThen(Action action) {
			return (p, a, g) -> {accept(p, a, g); action.accept(p, a, g);};
		}
	}
	
	public static final ClickableItem EMPTY = new ClickableItem(null, (a, b, c) -> {});

	private ItemStack itemStack;
	private Action action;
	
	/**
	 * Create a new {@link ClickableItem} for a GUI with the given ItemStack
	 * to display and action to do when the item is clicked.
	 * @param item GUI display item
	 * @param action What to do when the item is clicked
	 */
	public ClickableItem(ItemStack item, Action action) {
		this.itemStack = item;
		this.action = action;
	}
	
	public ItemStack getItemStack() {
		return itemStack;
	}
	
	public Action getAction() {
		return action;
	}
	
	/**
	 * Create a new {@link ClickableItem} with an ItemStack using the given name, type, and lore,
	 * and the given click action
	 * @param name Name for the ItemStack
	 * @param type Type of the ItemStack
	 * @param lore Lore for the ItemStack
	 * @param action What to do when the item is clicked
	 * @return the created {@link ClickableItem}
	 */
	public static ClickableItem create(String name, Material type, List<String> lore, Action action) {
		ItemStack item = new ItemStack(type);
		ItemMeta meta = item.getItemMeta();
		
		meta.setDisplayName(name);
		meta.setLore(lore);
		
		item.setItemMeta(meta);
		return new ClickableItem(item, action);
	}
}
