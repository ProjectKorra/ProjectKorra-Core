package com.projectkorra.core.util.gui;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

import com.projectkorra.core.ProjectKorra;

/**
 * Class for easily implementing an inventory-based GUI. GUI's are created in general 
 * and can be viewed by multiple players, at the same time. If a player-unique GUI is
 * desired, either only call {@link #open(Player)} on that player or use {@link PlayerGui}
 */
public class InventoryGui implements Listener {

	private ClickableItem[] items;
	private Inventory holder;
	
	private InventoryGui(String title, int size) {
		items = new ClickableItem[size];
		holder = Bukkit.createInventory(null, size, title);
		Bukkit.getServer().getPluginManager().registerEvents(this, JavaPlugin.getPlugin(ProjectKorra.class));
	}
	
	/**
	 * Attempts to put the given {@link ClickableItem} into the specified slot.
	 * @param slot Where to put the item in the inventory.
	 * @param item {@link ClickableItem} to put into the GUI.
	 * @return false if slot is filled or out of bounds.
	 */
	public boolean put(int slot, ClickableItem item) {
		if (slot < 0 || slot > items.length || items[slot] != null) {
			return false;
		}
		
		items[slot] = item;
		holder.setItem(slot, item.itemStack);
		return true;
	}
	
	/**
	 * Open this GUI for the given {@link Player}.
	 * @param player Who to open the GUI for.
	 */
	public void open(Player player) {
		player.openInventory(holder);
	}
	
	/**
	 * Close this GUI for the given {@link Player} if they have it open.
	 * @param player Who to close the GUI for.
	 */
	public void close(Player player) {
		if (holder.getViewers().contains(player)) {
			player.closeInventory();
		}
	}
	
	/**
	 * Create a new InventoryGui from the given title and number of rows
	 * @param title Title of the inventory
	 * @param rows Number of rows in the inventory, which will be clamped between 1 and 6 
	 * @return the created {@link InventoryGui}
	 */
	public static InventoryGui create(String title, int rows) {
		return new InventoryGui(title, Math.min(6, Math.max(1, rows)) * 9);
	}
	
	@EventHandler
	private void onClick(InventoryClickEvent event) {
		if (!event.getInventory().equals(holder) || event.getCurrentItem() == null || !(event.getWhoClicked() instanceof Player)) {
			return;
		}
		
		event.setCancelled(true);
		items[event.getSlot()].action.accept((Player) event.getWhoClicked(), event.getAction(), this);
	}
}
