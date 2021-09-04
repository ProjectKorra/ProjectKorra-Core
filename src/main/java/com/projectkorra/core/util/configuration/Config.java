package com.projectkorra.core.util.configuration;

import java.io.File;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

import com.projectkorra.core.ProjectKorra;
import com.projectkorra.core.util.reflection.ReflectionUtil;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class Config {
	
	private static final Map<Configurable, Config> CACHE = new HashMap<>();

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

	public boolean load() {
		try {
			config.load(file);
			return true;
		} catch (Exception e) {
			return false;
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

	public void addDefault(String path, Object value) {
		config.addDefault(path, value);
	}

	/**
	 * Uses the given {@link BiFunction} to get a value from the FileConfiguration. An
	 * example use of this is:
	 * <pre> {@code 
	 * double speed = config.getValue(FileConfiguration::getDouble, "Speed")
	 * } </pre>
	 * @param <T> Return type of the getter function
	 * @param getter Function to get a value from a FileConfiguration
	 * @param path Path argument for a configuration path
	 * @return a value from the config
	 */
	public <T> T getValue(BiFunction<FileConfiguration, String, T> getter, String path) {
		return getter.apply(config, path);
	}

	public <T> T getValue(Function<FileConfiguration, T> getter) {
		return getter.apply(config);
	}

	public Object getValue(String path) {
		return config.get(path);
	}

	public FileConfiguration get() {
		return config;
	}
	
	/**
	 * Get the config from the configurable object
	 * @param object the configurable object
	 * @return config of the configurable object
	 */
	public static Config from(Configurable object) {
		return CACHE.computeIfAbsent(object, (o) -> new Config(o.getFile()));
	}
	
	/**
	 * Take the given configurable object and modify any fields with the {@link Configure} annotation
	 * to match the value in the object's config, or making the value of the field the default if one
	 * doesn't already exist.
	 * @param <T> Object type that extends {@link Configurable}
	 * @param object the object to be configured
	 * @return the configured object
	 */
	public static <T extends Configurable> T process(T object) {
		for (Field field : object.getClass().getDeclaredFields()) {
			if (field.isAnnotationPresent(Configure.class)) {
				try {
					from(object).addDefault(field.getAnnotation(Configure.class).value(), field.get(object));
					ReflectionUtil.setValueSafely(object, field, from(object).getValue(field.getAnnotation(Configure.class).value()));
				} catch (Exception e) {
					e.printStackTrace();
					JavaPlugin.getPlugin(ProjectKorra.class).getLogger().warning("Unable to set config value of " + field.getName() + " for " + object.getFile().getName());
				}
			}
		}

		return object;
	}
}
