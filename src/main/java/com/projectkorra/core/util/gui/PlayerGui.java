package com.projectkorra.core.util.gui;

import org.bukkit.entity.Player;

/**
 * Class for creating an {@link InventoryGui} unique to a {@link Player}
 */
public class PlayerGui {

	private Player player;
	private InventoryGui gui;
	
	private PlayerGui(Player player, InventoryGui gui) {
		this.player = player;
		this.gui = gui;
	}
	
	/**
	 * Attempts to put the given {@link ClickableItem} into the specified slot.
	 * <br><br>Since this GUI is unique to the player, the item can be tailored
	 * to them more specifically
	 * @param slot Where to put the item in the inventory.
	 * @param item {@link ClickableItem} to put into the GUI.
	 * @return false if slot is filled or out of bounds.
	 */
	public boolean put(int slot, ClickableItem item) {
		return gui.put(slot, item);
	}
	
	/**
	 * Open this GUI for the associated Player
	 */
	public void open() {
		gui.open(player);
	}
	
	/**
	 * Close this GUI for the associated Player
	 */
	public void close() {
		gui.close(player);
	}
	
	/**
	 * Create a new player-specific inventory-based GUI
	 * @param player Who to create it for
	 * @param title Title for the inventory
	 * @param rows Number of rows in the inventory, clamped between 1 and 6
	 * @return The created player-specific GUI
	 */
	public static PlayerGui create(Player player, String title, int rows) {
		return new PlayerGui(player, InventoryGui.create(title, rows));
	}
}
