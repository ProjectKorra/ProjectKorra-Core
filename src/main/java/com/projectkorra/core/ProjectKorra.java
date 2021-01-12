package com.projectkorra.core;

import java.io.File;

import org.bukkit.plugin.java.JavaPlugin;

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
	
	public static ProjectKorra plugin() {
		return instance;
	}
	
	public static File getAbilitiesFolder() {
		return abilitiesFolder;
	}
}
