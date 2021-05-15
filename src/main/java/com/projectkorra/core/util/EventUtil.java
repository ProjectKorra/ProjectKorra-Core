package com.projectkorra.core.util;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;

public final class EventUtil {
	
	private EventUtil() {}
	
	public static <T extends Event> T call(T event) {
		Bukkit.getServer().getPluginManager().callEvent(event);
		return event;
	}
}
