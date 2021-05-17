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
	 * Attempt to put the given {@link ClickableItem} into the slot of the given page. If that slot
	 * is filled by the next or previous page items, this method will return false.
	 * @param page Desired page of the item
	 * @param index Desired slot of the item
	 * @param item Clickable item to be inserted
	 * @return false if page / index are out of bounds, or the space is occupied by default items
	 */
	public boolean put(int page, int index, ClickableItem item) {
		if (page < 0 || page >= pages.size() || index < 0 || index >= size) {
			return false;
		} else if (pages.get(page).getItem(index).get() == NEXT_PAGE || pages.get(page).getItem(index).get() == PREV_PAGE) {
			return false;
		}
		
		return pages.get(page).put(index, item);
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
}
