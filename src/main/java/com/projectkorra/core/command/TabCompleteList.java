package com.projectkorra.core.command;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;

import com.projectkorra.core.ability.AbilityManager;
import com.projectkorra.core.ability.AbilityUser;
import com.projectkorra.core.skill.Skill;

public class TabCompleteList {

	public static List<String> filter(List<String> list, String input) {
		if (list == null) {
			return null;
		}

		list.removeIf((s) -> !s.toLowerCase().contains(input.toLowerCase()));
		return list;
	}

	public static List<String> bindables(AbilityUser user) {
		return AbilityManager.getAbilitiesUserCanBind(user).stream().map((a) -> a.getName()).collect(Collectors.toList());
	}

	public static List<String> slots() {
		return Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9");
	}

	public static List<String> onlinePlayers() {
		return Bukkit.getOnlinePlayers().stream().map((p) -> p.getName()).collect(Collectors.toList());
	}

	public static List<String> skills(boolean parentOnly) {
		return Skill.values().stream().filter((s) -> !parentOnly || s.getChildren().isEmpty()).map((s) -> s.getInternalName()).collect(Collectors.toList());
	}
}
