package com.projectkorra.core.system.user;

import java.util.concurrent.ThreadLocalRandom;

@FunctionalInterface
public interface MobSlotAI {

	/**
	 * Slot selection AI that picks a random slot
	 */
	public static MobSlotAI random() {
		return () -> ThreadLocalRandom.current().nextInt(9);
	}
	
	/**
	 * Slot selection AI that goes through slots
	 * sequentially, 0 to 8 (left to right)
	 */
	public static MobSlotAI sequential() {
		return new MobSlotAI() {
			
			int current = 0;
			
			public int slot() {
				return current++ % 9;
			}
		};
	}
	
	public int slot();
}
