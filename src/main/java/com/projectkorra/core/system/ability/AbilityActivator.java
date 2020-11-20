package com.projectkorra.core.system.ability;

public class AbilityActivator {
	
	public static final AbilityActivator SNEAK_PRESS = new AbilityActivator("SNEAK_PRESS");
	public static final AbilityActivator SNEAK_RELEASE = new AbilityActivator("SNEAK_RELEASE");
	public static final AbilityActivator LEFT_CLICK = new AbilityActivator("LEFT_CLICK");
	public static final AbilityActivator RIGHT_CLICK_BLOCK = new AbilityActivator("RIGHT_CLICK_BLOCK");
	public static final AbilityActivator RIGHT_CLICK_ENTITY = new AbilityActivator("RIGHT_CLICK_ENTITY");
	public static final AbilityActivator PASSIVE = new AbilityActivator("PASSIVE");
	public static final AbilityActivator OFFHAND_SWAP = new AbilityActivator("OFFHAND_SWAP");

	private String identifier;
	
	public AbilityActivator(String identifier) {
		this.identifier = identifier;
	}
	
	@Override
	public boolean equals(Object other) {
		return other instanceof AbilityActivator ? this.identifier.equals(((AbilityActivator) other).identifier) : false;
	}
	
	@Override
	public String toString() {
		return identifier;
	}
	
	@Override
	public int hashCode() {
		return identifier.hashCode();
	}
}
