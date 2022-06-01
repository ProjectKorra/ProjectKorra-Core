package com.projectkorra.core.util.reflection;

import java.io.File;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.bukkit.plugin.java.JavaPlugin;

import com.projectkorra.core.ProjectKorra;

public class DynamicLoader {

	public static <T> Set<T> load(JavaPlugin plugin, String path, Class<T> parentClass) {
		if (plugin == null || path == null) {
			ProjectKorra.warnConsole("Cannot find abilities from null values");
			return null;
		}

		Set<T> found = new HashSet<>();
		ClassLoader loader = plugin.getClass().getClassLoader();

		try {
			Enumeration<URL> resources = loader.getResources(path.replace('.', '/'));
			String jarLoc = resources.nextElement().getPath();
			JarFile jar = new JarFile(new File(URLDecoder.decode(jarLoc.substring(5, jarLoc.length() - path.length() - 2), "UTF-8")));
			Enumeration<JarEntry> entries = jar.entries();

			while (entries.hasMoreElements()) {
				JarEntry entry = entries.nextElement();
				if (!entry.getName().endsWith(".class") || entry.getName().contains("$")) {
					continue;
				}

				String className = entry.getName().replace('/', '.').substring(0, entry.getName().length() - 6);
				if (!className.startsWith(path)) {
					continue;
				}

				Class<?> clazz = Class.forName(className, true, loader);
				if (clazz.isInterface() || java.lang.reflect.Modifier.isAbstract(clazz.getModifiers())) {
					continue;
				}
				
				if (parentClass.isAssignableFrom(clazz)) {
					found.add(parentClass.cast(clazz.getDeclaredConstructor().newInstance()));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
		return found;
	}
	
	public static <T> void load(JavaPlugin plugin, String path, Class<T> parentClass, Consumer<T> consumer) {
		if (plugin == null || path == null) {
			ProjectKorra.warnConsole("Cannot find abilities from null values");
			return;
		}

		ClassLoader loader = plugin.getClass().getClassLoader();

		try {
			Enumeration<URL> resources = loader.getResources(path.replace('.', '/'));
			String jarLoc = resources.nextElement().getPath();
			JarFile jar = new JarFile(new File(URLDecoder.decode(jarLoc.substring(5, jarLoc.length() - path.length() - 2), "UTF-8")));
			Enumeration<JarEntry> entries = jar.entries();

			while (entries.hasMoreElements()) {
				JarEntry entry = entries.nextElement();
				if (!entry.getName().endsWith(".class") || entry.getName().contains("$")) {
					continue;
				}

				String className = entry.getName().replace('/', '.').substring(0, entry.getName().length() - 6);
				if (!className.startsWith(path)) {
					continue;
				}

				Class<?> clazz = Class.forName(className, true, loader);
				if (clazz.isInterface() || java.lang.reflect.Modifier.isAbstract(clazz.getModifiers())) {
					continue;
				}
				
				if (parentClass.isAssignableFrom(clazz)) {
					consumer.accept(parentClass.cast(clazz.getDeclaredConstructor().newInstance()));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void load(JavaPlugin plugin, String path, Predicate<Class<?>> filter, Consumer<Class<?>> consumer) {
		if (plugin == null || path == null) {
			ProjectKorra.warnConsole("Cannot find abilities from null values");
			return;
		}

		ClassLoader loader = plugin.getClass().getClassLoader();

		try {
			Enumeration<URL> resources = loader.getResources(path.replace('.', '/'));
			String jarLoc = resources.nextElement().getPath();
			JarFile jar = new JarFile(new File(URLDecoder.decode(jarLoc.substring(5, jarLoc.length() - path.length() - 2), "UTF-8")));
			Enumeration<JarEntry> entries = jar.entries();

			while (entries.hasMoreElements()) {
				JarEntry entry = entries.nextElement();
				if (!entry.getName().endsWith(".class") || entry.getName().contains("$")) {
					continue;
				}

				String className = entry.getName().replace('/', '.').substring(0, entry.getName().length() - 6);
				if (!className.startsWith(path)) {
					continue;
				}

				Class<?> clazz = Class.forName(className, true, loader);
				if (clazz.isInterface() || java.lang.reflect.Modifier.isAbstract(clazz.getModifiers())) {
					continue;
				}
				
				if (filter.test(clazz)) {
					consumer.accept(clazz);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
