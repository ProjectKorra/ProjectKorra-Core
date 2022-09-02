package com.projectkorra.core.ability;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import com.projectkorra.core.util.Pair;


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
		if(getBendingUser(player) == null) {
			// check if player is in database, if not create new object
			BendingUser user = new BendingPlayer(player);
			
			users.add(user);
			
			updateActivations(user);
		}
	}

	public static void saveBendingUser(Player player) {
		if(getBendingUser(player) != null) {
			// save information
		}
	}

	private static void updateActivations(BendingUser user) {
		
		List<AbilityInfo> infos = AbilityManager.infos.stream().filter(i -> user.canBend(i)).toList();

		for(AbilityInfo i : infos) {
			user.getActivations().add(new Pair<>(i, i.getActivation()));
		}
	} 

}
