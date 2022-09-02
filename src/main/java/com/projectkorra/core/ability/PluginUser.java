package com.projectkorra.core.ability;

public class PluginUser extends BendingUser {
	
	public PluginUser() {
		super(null);
	}
	
	@Override
	public boolean canBend(AbilityInfo info) {
		return true;
	}

	@Override
	public boolean onCooldown(AbilityInfo info) {
		return false;
	}

	@Override
	public AbilityInfo getCurrentBind() {
		return null;
	}

}
