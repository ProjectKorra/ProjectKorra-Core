package com.projectkorra.core;

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
}
