package com.projectkorra.core.util.gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;

public class PagedGui {

	private List<InventoryGui> pages;
	private int size;
	
	private final ClickableItem NEXT_PAGE = ClickableItem.create("Next Page", Material.LIGHT_BLUE_STAINED_GLASS_PANE, Arrays.asList("Click here for the next page!"), (player, action, gui) -> {
		pages.get(pages.indexOf(gui) + 1).open(player);
	});
	
	private final ClickableItem PREV_PAGE = ClickableItem.create("Previous Page", Material.BLUE_STAINED_GLASS_PANE, Arrays.asList("Click here for the previous page!"), (player, action, gui) -> {
		pages.get(pages.indexOf(gui) - 1).open(player);
	});
	
	/**
	 * Create a new PagedGui, effectively a collection of connected {@link InventoryGui}'s
	 * @param title The title for the inventories, which will be appended with the page number
	 * @param pages How many pages (or guis) to be connected
	 * @param rows Number of rows in the inventories (they will all match)
	 */
	public PagedGui(String title, int pages, int rows) {
		this.pages = new ArrayList<>(Math.max(2, pages));
		this.size = Math.min(6, Math.max(1, rows)) * 9;
		
		for (int i = 0; i < pages; ++i) {
			InventoryGui gui = InventoryGui.create(title + " &r[" + (i + 1) + "/" + pages + "]", rows);
			
			if (i >= 0 && i < pages - 1) {
				gui.put(size - 1, NEXT_PAGE);
			}
			
			if (i > 0 && i <= pages - 1) {
				gui.put(size - 9, PREV_PAGE);
			}
			
			this.pages.set(i, gui);
		}
	}
	
	/**
	 * Attempt to put the given {@link ClickableItem} into the given index.
	 * <br><br>The index of any slot is given as <code>(page + 1) * size + slot</code> where
	 * page is the page number, size is the inventory size, and slot is the inventory slot
	 * @param index Where to put the item
	 * @param item Clickable item to be inserted
	 * @return false if page / index are out of bounds, or the space is occupied by default items
	 */
	public boolean put(int page, int slot, ClickableItem item) {
		if (page < 0 || page >= pages.size() || slot < 0 || slot >= size) {
			return false;
		} else if (pages.get(page).get(slot) == NEXT_PAGE || pages.get(page).get(slot) == PREV_PAGE) {
			return false;
		}
		
		return pages.get(page).put(slot, item);
	}
	
	/**
	 * Opens the first page for the given player
	 * @param player Who to open the first page for
	 */
	public void open(Player player) {
		pages.get(0).open(player);
	}
	
	/**
	 * Opens the specified page for the given player.
	 * If the given page is less than zero or greater
	 * than the number of pages, this will fail silently.
	 * @param player Who to open for
	 * @param page Which page to open
	 */
	public void openPage(Player player, int page) {
		if (page < 0 || page >= pages.size()) {
			return;
		}
		pages.get(page).open(player);
	}
	
	/**
	 * Attempts to close any gui the player has open in
	 * association with this {@link PagedGui}
	 * @param player Who to close pages for
	 */
	public void close(Player player) {
		for (InventoryGui gui : pages) {
			gui.close(player);
		}
	}

	/**
	 * Gets the {@link ClickableItem} on the given page and in the given slot.
	 * @param page Which page the item is on
	 * @param slot Which slot the item is in
	 * @return null if page / slot are out of bounds
	 */
	public ClickableItem get(int page, int slot) {
		if (page < 0 || page >= pages.size()) {
			return null;
		}
		return pages.get(page).get(slot);
	}
	
	/**
	 * Creates a new {@link ClickableItem} for opening a specific page in this gui
	 * @param name Display name of the ItemStack 
	 * @param type Type of the ItemStack
	 * @param lore Lore for the ItemStack
	 * @param page Which page to be opened when clicked
	 * @return the new {@link ClickableItem}
	 */
	public ClickableItem pageOpener(String name, Material type, List<String> lore, int page) {
		if (page < 0 || page >= pages.size()) {
			return ClickableItem.EMPTY;
		}
		return ClickableItem.create(name, type, lore, (p, a, g) -> pages.get(page).open(p));
	}
}
