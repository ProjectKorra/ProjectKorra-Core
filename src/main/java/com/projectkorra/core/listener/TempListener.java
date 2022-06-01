package com.projectkorra.core.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;

import com.projectkorra.core.temporary.TempBlock;

public class TempListener implements Listener {

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	private void onFlow(BlockFromToEvent event) {
		if (!TempBlock.exists(event.getBlock())) {
			return;
		}
		
		event.setCancelled(!TempBlock.from(event.getBlock()).currentlyHasPhysics());
	}
}
