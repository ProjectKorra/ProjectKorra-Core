package com.projectkorra.core.util.gui;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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

	private DisplayItem[] items;
	private Inventory display;
	
	private InventoryGui(String title, int size) {
		items = new DisplayItem[size];
		display = Bukkit.createInventory(null, size, title);
		Bukkit.getServer().getPluginManager().registerEvents(this, JavaPlugin.getPlugin(ProjectKorra.class));
	}
	
	/**
	 * Inserts the given {@link DisplayItem} into the specified slot.
	 * This method will replace the item currently occupying the slot if it is replaceable as
	 * defined by {@link DisplayItem#isReplaceable()}
	 * @param slot Where to put the item in the inventory.
	 * @param item {@link DisplayItem} to put into the inventory GUI.
	 * @return false if slot is out of bounds or current item is non-replaceable
	 */
	public boolean setItem(int slot, DisplayItem item) {
		if (slot < 0 || slot >= items.length) {
			return false;
		}
		
		return set(slot, item);
	}
	
	/**
	 * Inserts the given {@link DisplayItem} into the slot denoted by the given row and column.
	 * This method will replace the item currently occupying the slot if it is replaceable as
	 * defined by {@link DisplayItem#isReplaceable()}
	 * @param row Which row of the inventory to put the item in
	 * @param column Which column of the inventory to put the item in
	 * @param item What to put into the inventory GUI.
	 * @return false if row / column are out of bounds or current item is non-replaceable
	 */
	public boolean setItem(int row, int column, DisplayItem item) {
		if (row < 0 || row >= items.length / 9 || column < 0 || column >= 9) {
			return false;
		}
		
		return set(row * 9 + column, item);
	}
	
	private boolean set(int slot, DisplayItem item) {
		if (items[slot] != null && !items[slot].isReplaceable()) {
			return false;
		}
		
		items[slot] = item;
		display.setItem(slot, item.getItemStack());
		return true;
	}
	
	/**
	 * Gets the {@link DisplayItem} in the given slot
	 * @param slot Where to get the item from
	 * @return null if slot is out of bounds or isn't filled yet
	 */
	public DisplayItem getItem(int slot) {
		return (slot < 0 || slot >= items.length) ? null : items[slot];
	}
	
	/**
	 * Open this GUI for the given {@link Player}.
	 * @param player Who to open the GUI for.
	 */
	public void open(Player player) {
		player.openInventory(display);
	}
	
	/**
	 * Close this GUI for the given {@link Player} if they have it open.
	 * @param player Who to close the GUI for.
	 */
	public void close(Player player) {
		if (display.getViewers().contains(player)) {
			player.closeInventory();
		}
	}
	
	/**
	 * Get the size of the inventory for this gui
	 * @return inventory size
	 */
	public int size() {
		return items.length;
	}
	
	/**
	 * Create a new InventoryGui from the given title and number of rows
	 * @param title Title of the inventory
	 * @param rows Number of rows in the inventory, which will be clamped between 1 and 6 
	 * @return the created {@link InventoryGui}
	 */
	public static InventoryGui create(String title, int rows) {
		return new InventoryGui(ChatColor.translateAlternateColorCodes('&', title), Math.min(6, Math.max(1, rows)) * 9);
	}
	
	@EventHandler
	private void onClick(InventoryClickEvent event) {
		if (!event.getInventory().equals(display) || event.getCurrentItem() == null || !(event.getWhoClicked() instanceof Player)) {
			return;
		}
		
		event.setCancelled(true);
		items[event.getSlot()].getAction().ifPresent((a) -> a.accept((Player) event.getWhoClicked(), event.getAction(), this));
	}
}
