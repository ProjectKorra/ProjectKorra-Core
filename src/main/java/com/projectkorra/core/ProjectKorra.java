package com.projectkorra.core;

import java.io.File;

import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;

import com.projectkorra.core.ability.AbilityManager;
import com.projectkorra.core.ability.PKListener;

public class ProjectKorra extends JavaPlugin {

	@Override
	public void onEnable() {
		// get infos from classes
		new FireAbilityInfo();
		// get their activations

		// validate activations

		// register listeners
		
		this.getServer().getPluginManager().registerEvents(new PKListener(), this);
				
		this.getServer().getScheduler().runTaskTimer(this, () -> {
			AbilityManager.tick();
			// do other stuff;
		},  0, 1);
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
