package com.projectkorra.core;

import org.bukkit.event.Event;
import org.bukkit.plugin.java.JavaPlugin;

public class ProjectKorra extends JavaPlugin {
	
	private static ProjectKorra instance;

	@Override
	public void onEnable() {
		instance = this;
	}

	@Override
	public void onDisable() {}
	
	public static ProjectKorra plugin() {
		return instance;
	}
	
	public static <T extends Event> T callEvent(T event) {
		instance.getServer().getPluginManager().callEvent(event);
		return event;
	}
}
