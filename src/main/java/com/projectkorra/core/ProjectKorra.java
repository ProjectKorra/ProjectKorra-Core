package com.projectkorra.core;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;

import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;

import com.projectkorra.core.api.AbilityInfo;
import com.projectkorra.core.api.AbilityManager;
import com.projectkorra.core.api.PKListener;

public class ProjectKorra extends JavaPlugin {

	public static ProjectKorra plugin;

	@Override
	public void onEnable() {
		plugin = this;
		// get infos from classes
		registerAbilities();
		// get their activations

		// validate activations

		// register listeners

		this.getServer().getPluginManager().registerEvents(new PKListener(), this);

		this.getServer().getScheduler().runTaskTimer(this, () -> {
			AbilityManager.tick();
			// do other stuff;
		}, 0, 1);
	}

	@Override
	public void onDisable() {
	}

	public ProjectKorra() {
		super();
	}

	protected ProjectKorra(JavaPluginLoader loader, PluginDescriptionFile description, File dataFolder, File file) {
		super(loader, description, dataFolder, file);
	}

	private void registerAbilities() {
		File abilFolder = new File(this.getDataFolder().getPath() + "\\abilities");
		if (!abilFolder.exists()) {
			abilFolder.mkdir();
		}

		File[] files = abilFolder.listFiles();
		for (File f : files) {
			try {
				JarFile j = new JarFile(f);
				Enumeration<JarEntry> entries = j.entries();

				while (entries.hasMoreElements()) {
					JarEntry entry = entries.nextElement();
					if (!entry.getName().endsWith(".class") || entry.getName().contains("$")) {
						continue;
					}

					String className = entry.getName().replace('/', '.').substring(0, entry.getName().length() - 6);

					try {
						Class<?> clazz = Class.forName(className);

						if (clazz.getSuperclass() == AbilityInfo.class) {
							clazz.getDeclaredConstructors()[0].newInstance();
						}
					} catch (Exception e) {
						ProjectKorra.plugin.getLogger().log(Level.SEVERE,
								"Loading abilities from ability folder gone bad.");
						e.printStackTrace();
					}

				}
				j.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
