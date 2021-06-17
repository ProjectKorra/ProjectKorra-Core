package com.projectkorra.core.system.entity;

import java.util.concurrent.ThreadLocalRandom;

import com.projectkorra.core.util.data.Holder;

@FunctionalInterface
public interface MobSlotAI {

	Holder<Integer> current = Holder.of(0);
	
	/**
	 * Slot selection AI that picks a random slot
	 */
	public static MobSlotAI random() {
		return () -> {
			current.setHeld(ThreadLocalRandom.current().nextInt(9));
			return current.getHeld();
		};
	}
	
	/**
	 * Slot selection AI that goes through slots
	 * incrementally, 0 to 8 (left to right)
	 */
	public static MobSlotAI incremental() {
		return () -> {
			current.setHeld((current.getHeld() + 1) % 9);
			return current.getHeld();
		};
	}
	
	/**
	 * Slot selection AI that goes through slots
	 * decrementally, 8 to 0 (right to left)
	 */
	public static MobSlotAI decremental() {
		return new MobSlotAI() {

			@Override
			public int slot() {
				return 8 - current.getHeld();
			}
			
			@Override
			public int next() {
				current.setHeld((current.getHeld() + 1) % 9);
				return slot();
			}
			
		};
	}
	
	public default int slot() {
		return current.getHeld();
	}
	
	public int next();
}
