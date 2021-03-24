package com.projectkorra.core;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;
import org.bukkit.plugin.PluginDescriptionFile;

import java.io.File;

public class ProjectKorra extends JavaPlugin {
	
	private static ProjectKorra instance;
	
	private static File abilitiesFolder;

	@Override
	public void onEnable() {
		instance = this;
		abilitiesFolder = new File(getDataFolder(), "/abilities/");
		
		getServer().getScheduler().scheduleSyncRepeatingTask(this, () -> AbilityManager.tick(), 1, 1);
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
	
	public static File getAbilitiesFolder() {
		return abilitiesFolder;
	}
	
	public static void messageConsole(String msg) {
		instance.getLogger().info(msg);
	}
	
	public static void warnConsole(String msg) {
		instance.getLogger().warning(msg);
	}
}
