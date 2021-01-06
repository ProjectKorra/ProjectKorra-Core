package com.projectkorra.core.util.configuration;

import java.io.File;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class Config {

	private File file;
	private FileConfiguration config;
	
	public Config(File file) {
		this.file = file;
		this.config = YamlConfiguration.loadConfiguration(file);
		this.reload();
	}
	
	public void reload() {
		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdir();
		}
		
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public boolean save() {
		try {
			config.save(file);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public FileConfiguration get() {
		return config;
	}
}
