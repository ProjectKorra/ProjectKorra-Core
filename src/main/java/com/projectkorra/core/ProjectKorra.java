package com.projectkorra.core;

import java.io.File;

import org.bukkit.event.Event;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;

public class ProjectKorra extends JavaPlugin {
	
	private static ProjectKorra instance;

	@Override
	public void onEnable() {
		instance = this;
	}

	@Override
	public void onDisable() {}
	
	public ProjectKorra() {
		super();
	}

	protected ProjectKorra(JavaPluginLoader loader, PluginDescriptionFile description, File dataFolder, File file) {
		super(loader, description, dataFolder, file);
	}
	
	public static ProjectKorra plugin() {
		return instance;
	}
	
	public static <T extends Event> T callEvent(T event) {
		instance.getServer().getPluginManager().callEvent(event);
		return event;
	}
}
