package com.projectkorra.core.ability;

import org.bukkit.entity.Player;



public class BendingPlayer extends BendingUser {
	
	public BendingPlayer(Player entity) {
		super(entity);
	}
	
	@Override
	public AbilityInfo getCurrentBind() {
		return null;
	}
}
