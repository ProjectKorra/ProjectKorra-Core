package com.projectkorra.core.util.data;

import com.projectkorra.core.ability.AbilityInstance;

public interface RemovalPolicy {
	
	public static final RemovalPolicy EMPTY = (instance) -> false;
	public static final RemovalPolicy DEATH = (instance) -> instance.getUser().getEntity().isDead();
	public static final RemovalPolicy OFFLINE = (instance) -> !instance.getUser().isOnline();
	public static final RemovalPolicy OFF_BIND = (instance) -> !instance.getUser().getBoundAbility().filter((bind) -> bind.equals(instance.getProvider())).isPresent();
	
	/** Shortcut for using the DEATH and OFFLINE policies */
	public static final RemovalPolicy COMMON = DEATH.add(OFFLINE);

	public boolean test(AbilityInstance instance);
	
	public default RemovalPolicy add(RemovalPolicy other) {
		return (instance) -> this.test(instance) || other.test(instance);
	}
}
