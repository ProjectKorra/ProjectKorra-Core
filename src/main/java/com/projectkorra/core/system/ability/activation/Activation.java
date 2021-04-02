package com.projectkorra.core.system.ability.activation;

import java.util.HashMap;
import java.util.Map;

public class Activation {
	
	private static final Map<String, Activation> CACHE = new HashMap<>();
	
	public static final Activation LEFT_CLICK = of("left_click");
	public static final Activation RIGHT_CLICK_BLOCK = of("right_click_block");
	public static final Activation RIGHT_CLICK_ENTITY = of("right_click_entity");
	public static final Activation SNEAK_DOWN = of("sneak_down");
	public static final Activation SNEAK_UP = of("sneak_up");
	public static final Activation SPRINT_ON = of("sprint_on");
	public static final Activation SPRINT_OFF = of("sprint_off");
	public static final Activation DAMAGED = of("damaged");
	public static final Activation PASSIVE = of("passive");

	private String id;
	
	private Activation(String id) {
		this.id = id;
	}
	
	@Override
	public String toString() {
		return id;
	}
	
	@Override
	public boolean equals(Object other) {
		return other instanceof Activation && other.toString().equals(id);
	}
	
	@Override
	public int hashCode() {
		return id.hashCode();
	}
	
	public static Activation of(String id) {
		return CACHE.computeIfAbsent(id.toLowerCase(), (s) -> new Activation(s));
	}
}
