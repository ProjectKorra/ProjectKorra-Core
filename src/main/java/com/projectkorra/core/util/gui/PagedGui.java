package com.projectkorra.core.util.gui;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;

public class PagedGui {

	private List<InventoryGui> pages;
	private int size;
	
	private final DisplayItem NEXT_PAGE = DisplayItem.functional("Next Page", Material.LIGHT_BLUE_STAINED_GLASS_PANE, null, (player, action, gui) -> {
		gui.close(player);
		pages.get(pages.indexOf(gui) + 1).open(player);
	}, false);
	
	private final DisplayItem PREV_PAGE = DisplayItem.functional("Previous Page", Material.BLUE_STAINED_GLASS_PANE, null, (player, action, gui) -> {
		gui.close(player);
		pages.get(pages.indexOf(gui) - 1).open(player);
	}, false);
	
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
				gui.setItem(size - 1, NEXT_PAGE);
			}
			
			if (i > 0 && i <= pages - 1) {
				gui.setItem(size - 9, PREV_PAGE);
			}
			
			this.pages.add(gui);
		}
	}
	
	public InventoryGui page(int index) {
		if (index < 0 || index >= pages.size()) {
			return null;
		}
		
		return pages.get(index);
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
	 * Get the size of the Inventories for this gui
	 * @return inventory size
	 */
	public int size() {
		return size;
	}
	
	/**
	 * Get the number of pages in this gui
	 * @return how many pages this has
	 */
	public int pages() {
		return pages.size();
	}
	
	/**
	 * Creates a new {@link DisplayItem} for opening a specific page in this gui
	 * @param name Display name of the ItemStack 
	 * @param type Type of the ItemStack
	 * @param lore Lore for the ItemStack
	 * @param page Which page to be opened when clicked
	 * @param replaceable Whether or not the page opener should be replaceable
	 * @return the new {@link DisplayItem}
	 */
	public DisplayItem pageOpener(String name, Material type, List<String> lore, int page, boolean replaceable) {
		if (page < 0 || page >= pages.size()) {
			return DisplayItem.EMPTY;
		}
		return DisplayItem.functional(name, type, lore, (p, a, g) -> pages.get(page).open(p), replaceable);
	}
}
