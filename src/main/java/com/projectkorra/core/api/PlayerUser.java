package com.projectkorra.core.api;

import org.bukkit.entity.Player;

public class PlayerUser extends User {
	private int slot;

	public PlayerUser(Player entity) {
		super(entity);
	}

	@Override
	public AbilityInfo getCurrentBind() {
		return null;
	}

	public void setSlot(int i) {
		this.slot = i;
	}

	public int getSlot() {
		return this.slot;
	}
}
