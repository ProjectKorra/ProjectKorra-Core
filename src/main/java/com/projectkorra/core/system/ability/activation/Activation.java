package com.projectkorra.core.system.ability.activation;

import java.util.HashMap;
import java.util.Map;

public class Activation {

	private static final Map<String, Activation> CACHE = new HashMap<>();
	
	public static final Activation LEFT_CLICK = Activation.of("leftclick");
	public static final Activation SNEAK_PRESS = Activation.of("sneakpress");
	public static final Activation SNEAK_RELEASE = Activation.of("sneakrelease");
	public static final Activation OFFHAND_SWAP = Activation.of("offhandswap");
	public static final Activation PASSIVE = Activation.of("passive");
	public static final Activation SEQUENCED = Activation.of("sequenced");
	
	private String id;
	
	Activation(String id) {
		this.id = id;
	}
	
	@Override
	public String toString() {
		return id;
	}
	
	@Override
	public int hashCode() {
		return id.hashCode();
	}
	
	@Override
	public boolean equals(Object other) {
		if (other instanceof Activation) {
			return this.id.equals(other.toString());
		}
		
		return false;
	}
	
	/**
	 * Attempts to get a cached Activation from the given id,
	 * or creates and caches an Activation from the id if not present
	 * @param id String identifier for the Activation
	 * @return an Activation from the given id
	 */
	public static Activation of(String id) {
		id = id.toLowerCase();
		Activation val = CACHE.get(id);
		
		if (val == null) {
			val = new Activation(id);
			CACHE.put(id, val);
		}
		
		return val;
	}
}
