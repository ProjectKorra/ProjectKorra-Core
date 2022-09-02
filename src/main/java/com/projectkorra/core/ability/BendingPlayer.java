package com.projectkorra.core.ability;

import org.bukkit.entity.Player;

import com.projectkorra.core.skills.Skill;


public class BendingPlayer extends BendingUser {
	
	public BendingPlayer(Player entity) {
		super(entity);
	}

	
	@Override
	public boolean canBend(AbilityInfo info) {
		for(Skill s : info.skills) {
			if(this.getSkills().contains(s))
				return true;
		}
		return false;
	}
	
	@Override
	public AbilityInfo getCurrentBind() {
		return this.getBinds()[((Player) this.getEntity()).getInventory().getHeldItemSlot()];
	}

	@Override
	public boolean onCooldown(AbilityInfo info) {
		return false;
	}

}
