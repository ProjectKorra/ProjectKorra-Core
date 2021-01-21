package com.projectkorra.core;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;
import org.bukkit.plugin.PluginDescriptionFile;

import java.io.File;

public class ProjectKorra extends JavaPlugin {
	
	@Override
	public void onEnable() {}

	@Override
	public void onDisable() {}
	
	public ProjectKorra() {
		super();
	}

	protected ProjectKorra(JavaPluginLoader loader, PluginDescriptionFile description, File dataFolder, File file) {
		super(loader, description, dataFolder, file);
	}
}
