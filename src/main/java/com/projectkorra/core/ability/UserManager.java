package com.projectkorra.core.ability;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import com.projectkorra.core.util.Pair;

public class UserManager {

	protected static final List<User> users = new ArrayList<>(20);
	public static final User plugin = new PluginUser();

	public static User getUser(Player player) {
		for (User u : users) {
			if (u.getEntity() == player) {
				return u;
			}
		}
		return null;
	}

	public static void loadUser(Player player) {
		if (getUser(player) == null) {
			// check if player is in database, if not create new object
			User user = new PlayerUser(player);

			users.add(user);

			updateActivations(user);
		}
	}

	public static void saveUser(Player player) {
		if (getUser(player) != null) {
			// save information
		}
	}

	private static void updateActivations(User user) {

		List<AbilityInfo> infos = AbilityManager.infos.stream().filter(i -> user.canBend(i)).toList();

		for (AbilityInfo i : infos) {
			user.getActivations().add(new Pair<>(i, i.getActivation()));
		}
	}

}
