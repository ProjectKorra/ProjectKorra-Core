package com.projectkorra.core.api;

import org.bukkit.entity.Player;

public class PlayerUser extends User {

	public PlayerUser(Player entity) {
		super(entity);
	}

	@Override
	public AbilityInfo getCurrentBind() {
		return null;
	}
}
