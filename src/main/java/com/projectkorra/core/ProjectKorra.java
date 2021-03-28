package com.projectkorra.core;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;

import com.projectkorra.core.system.ability.AbilityManager;

import org.bukkit.plugin.PluginDescriptionFile;

import java.io.File;

public class ProjectKorra extends JavaPlugin {
	
	private static ProjectKorra instance;
	
	private static File abilitiesFolder;

	@Override
	public void onEnable() {
		instance = this;
		abilitiesFolder = new File(getDataFolder(), "/abilities/");
		
		AbilityManager.init(this);
	}

	@Override
	public void onDisable() {}

	public ProjectKorra() {
		super();
	}

	protected ProjectKorra(JavaPluginLoader loader, PluginDescriptionFile description, File dataFolder, File file) {
		super(loader, description, dataFolder, file);
	}
}
