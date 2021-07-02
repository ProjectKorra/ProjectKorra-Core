package com.projectkorra.core.system.ability.activation;

import java.util.HashMap;
import java.util.Map;

public class Activation {
	
	private static final Map<String, Activation> CACHE = new HashMap<>();
	
	public static final Activation LEFT_CLICK = of("left_click", "Left Click");
	public static final Activation RIGHT_CLICK_BLOCK = of("right_click_block", "Right Click Block");
	public static final Activation RIGHT_CLICK_ENTITY = of("right_click_entity", "Right Click Entity");
	public static final Activation SNEAK_DOWN = of("sneak_down", "Press Sneak");
	public static final Activation SNEAK_UP = of("sneak_up", "Release Sneak");
	public static final Activation SPRINT_ON = of("sprint_on", "Start Sprinting");
	public static final Activation SPRINT_OFF = of("sprint_off", "Stop Sprinting");
	public static final Activation DAMAGED = of("damaged", "Take damage");
	public static final Activation PASSIVE = of("passive", "Passive");

	private String id, display;
	
	private Activation(String id, String display) {
		this.id = id;
		this.display = display;
	}
	
	public String getDisplay() {
		return display;
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
	
	public static Activation of(String id, String display) {
		return CACHE.computeIfAbsent(id.toLowerCase(), (s) -> new Activation(s, display));
	}
}
