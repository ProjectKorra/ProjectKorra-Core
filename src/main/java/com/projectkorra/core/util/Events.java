package com.projectkorra.core.util;

import com.projectkorra.core.ProjectKorra;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class Events {
	
	private Events() {}
	
	public static <T extends Event> T call(T event) {
		Bukkit.getServer().getPluginManager().callEvent(event);
		return event;
	}

	public static <T extends Listener> T register(T listener) {
		Bukkit.getServer().getPluginManager().registerEvents(listener, JavaPlugin.getPlugin(ProjectKorra.class));
		return listener;
	}
}
