package com.projectkorra.core.util.configuration;

import java.io.File;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import com.projectkorra.core.ProjectKorra;
import com.projectkorra.core.util.reflection.ReflectionUtil;

public class Config {
	
	private static final Map<Configured, Config> CACHE = new HashMap<>();

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
	
	public static Config from(Configured object) {
		return CACHE.computeIfAbsent(object, (o) -> new Config(o.getFile()));
	}
	
	public static void configure(Configured object) {
		for (Field field : object.getClass().getDeclaredFields()) {
			if (field.isAnnotationPresent(Configurable.class)) {
				try {
					from(object).get().addDefault(field.getAnnotation(Configurable.class).value(), field.get(object));
					ReflectionUtil.setValueSafely(object, field, from(object).get().get(field.getAnnotation(Configurable.class).value()));
				} catch (Exception e) {
					e.printStackTrace();
					JavaPlugin.getPlugin(ProjectKorra.class).getLogger().warning("Unable to set config value of " + field.getName() + " for " + object.getFile().getName());
				}
			}
		}
	}
}
