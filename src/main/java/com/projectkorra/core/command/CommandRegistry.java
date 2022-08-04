package com.projectkorra.core.command;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.lang.Validate;
import org.bukkit.command.PluginCommand;

import com.projectkorra.core.ProjectKorra;
import com.projectkorra.core.util.reflection.DynamicLoader;

public final class CommandRegistry {

	private static final Map<String, PKSubCommand> CMDS = new HashMap<>();

	private static ProjectKorra plugin = null;

	public static void init(ProjectKorra pk) {
		if (plugin != null) {
			return;
		}

		plugin = pk;
		registerSpigot();
		registerCore();
	}

	public static void reload() {
		if (!CMDS.isEmpty()) {
			CMDS.clear();
		}

		registerCore();
	}

	public static void register(PKSubCommand cmd) {
		Validate.notNull(cmd, "Cannot register null command!");
		Validate.notEmpty(cmd.getName(), "Cannot register command with empty name!");
		Validate.isTrue(!CMDS.containsKey(cmd.getName().toLowerCase()), "Cannot register command with existing name!");

		CMDS.put(cmd.getName().toLowerCase(), cmd);
		List<String> aliases = cmd.getAliases();
		if (aliases == null) {
			aliases = Collections.emptyList();
		}

		for (String alias : aliases) {
			if (CMDS.containsKey(alias.toLowerCase())) {
				plugin.getLogger().warning("Command '" + cmd.getName() + "' attempted to register under existing alias '" + alias + "'!");
				continue;
			}

			CMDS.put(alias.toLowerCase(), cmd);
		}
	}

	public static Optional<PKSubCommand> from(String label) {
		return Optional.ofNullable(CMDS.get(label.toLowerCase()));
	}
	
	public static Set<String> registered() {
		return new HashSet<>(CMDS.keySet());
	}

	private static void registerSpigot() {
		PluginCommand spigot = plugin.getCommand("projectkorra");
		PKCommand pkcmd = new PKCommand();

		spigot.setExecutor(pkcmd);
		spigot.setTabCompleter(pkcmd);
	}

	private static void registerCore() {
		DynamicLoader.load(plugin, "com.projectkorra.core.command", PKSubCommand.class, CommandRegistry::register);
	}
}
