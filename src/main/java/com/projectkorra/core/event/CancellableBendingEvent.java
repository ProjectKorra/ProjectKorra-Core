package com.projectkorra.core.event;

import org.bukkit.event.Cancellable;

public class CancellableBendingEvent extends BendingEvent implements Cancellable {

	protected boolean cancelled = false;

	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled(boolean cancel) {
		this.cancelled = cancel;
	}
}
