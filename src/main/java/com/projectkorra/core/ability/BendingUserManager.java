package com.projectkorra.core.ability;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;


public class BendingUserManager {

	protected static final List<BendingUser> users = new ArrayList<>(20);
	public static final BendingUser plugin = new PluginUser();

	public static BendingUser getBendingUser(Player player) {
		for(BendingUser user : users) {
			if(user.getEntity() == player) {
				return user;
			}
		}
		return null;
	}

	public static void loadBendingUser(Player player) {
		// TODO database stuff
		if(getBendingUser(player) == null) {
			// check if player is in database, if not create new object
			users.add(new BendingPlayer(player));
		}
	}

	public static void saveBendingUser(Player player) {
		// TODO database stuff
		if(getBendingUser(player) != null) {
			// save information
		}
	}

}
