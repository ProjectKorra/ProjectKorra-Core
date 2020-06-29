package com.projectkorra.core.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;

public class PluginUtil {
	
	private PluginUtil() {}
	
	/**
	 * Useful method for creating and calling an event in a single line, such as:<br>
	 * {@code CustomEvent event = PluginUtil.callEvent(new CustomEvent());}
	 * @param <T>
	 * @param event
	 * @return
	 */
	public static <T extends Event> T callEvent(T event) {
		Bukkit.getServer().getPluginManager().callEvent(event);
		return event;
	}
	
	/**
	 * Returns the current date in the form {@code yyyy/MM/dd HH:mm:ss}
	 * @return formatted current date
	 */
	public static String getCurrentDate() {
		return new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date());
	}
}
