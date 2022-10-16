package com.projectkorra.core.ability.activation;

import java.util.HashMap;
import java.util.Map;

import com.projectkorra.core.ProjectKorra;

public class Activation {

	private static final Map<String, Activation> CACHE = new HashMap<>();

	public static final Activation LEFT_CLICK = of("left_click", "Left Click", true);
	public static final Activation RIGHT_CLICK_BLOCK = of("right_click_block", "Right Click Block", true);
	public static final Activation RIGHT_CLICK_ENTITY = of("right_click_entity", "Right Click Entity", true);
	public static final Activation SNEAK_DOWN = of("sneak_down", "Press Sneak", true);
	public static final Activation SNEAK_UP = of("sneak_up", "Release Sneak", true);
	public static final Activation SPRINT_ON = of("sprint_on", "Start Sprinting", false);
	public static final Activation SPRINT_OFF = of("sprint_off", "Stop Sprinting", false);
	public static final Activation DAMAGED = of("damaged", "Take damage", false);
	public static final Activation PASSIVE = of("passive", "Passive", false);
	public static final Activation COMBO = of("combo", "Combo sequence", false);

	private String id, display;
	private boolean comboable;

	private Activation(String id, String display, boolean comboable) {
		this.id = id;
		this.display = display;
		this.comboable = comboable;
	}

	public String getDisplay() {
		return display;
	}

	public boolean canCombo() {
		return comboable;
	}

	public boolean matchAny(Activation...triggers) {
		for (Activation a : triggers) {
			if (this == a) return true;
		}
		
		return false;
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

	public static Activation of(String id, String display, boolean comboable) {
		if (id == null) {
			ProjectKorra.warnConsole("Attempted register of activation with null id");
			return null;
		}

		if (CACHE.size() >= 64) {
			ProjectKorra.warnConsole("Activation of " + id + " unable to register, cache full");
			return null;
		}

		return CACHE.computeIfAbsent(id.toLowerCase(), (s) -> new Activation(s, display, comboable));
	}
}
