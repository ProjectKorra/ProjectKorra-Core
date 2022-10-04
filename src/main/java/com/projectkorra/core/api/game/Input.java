package com.projectkorra.core.api.game;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Input {

	public static final Map<String, Input> INPUTS = new HashMap<>(30);
	public static Collection<Input> values = INPUTS.values();

	public static final Input SHIFT_DOWN = new Input("SHIFT_DOWN");
	public static final Input SHIFT_UP = new Input("SHIFT_UP");
	public static final Input SPRINT_ON = new Input("SPRINT_ON");
	public static final Input SPRINT_OFF = new Input("SPRINT_OFF");

	public static final Input LEFT_CLICK = new Input("LEFT_CLICK");
	public static final Input LEFT_CLICK_BLOCK = new Input("LEFT_CLICK_BLOCK");
	public static final Input RIGHT_CLICK_AIR = new Input("RIGHT_CLICK_AIR");
	public static final Input RIGHT_CLICK_BLOCK = new Input("RIGHT_CLICK_BLOCK");
	public static final Input RIGHT_CLICK_ENTITY = new Input("RIGHT_CLICK_ENTITY");
	public static final Input LEFT_CLICK_OFF_HAND = new Input("LEFT_CLICK_OFF_HAND");
	public static final Input LEFT_CLICK_BLOCK_OFF_HAND = new Input("LEFT_CLICK_BLOCK_OFF_HAND");
	public static final Input RIGHT_CLICK_AIR_OFF_HAND = new Input("RIGHT_CLICK_AIR_OFF_HAND");
	public static final Input RIGHT_CLICK_BLOCK_OFF_HAND = new Input("RIGHT_CLICK_BLOCK_OFF_HAND");

	public static final Input LEFT = new Input("LEFT");
	public static final Input RIGHT = new Input("RIGHT");
	public static final Input FORWARD = new Input("FORWARD");
	public static final Input BACKWARD = new Input("BACKWARD");
	public static final Input UP = new Input("UP");
	public static final Input DOWN = new Input("DOWN");
	public static final Input FLIGHT_ON = new Input("FLIGHT_ON");
	public static final Input FLIGHT_OFF = new Input("FLIGHT_OFF");
	public static final Input JUMP = new Input("JUMP");
	public static final Input KNOCK_BACK = new Input("KNOCKBACK");

	public static final Input SLOT_CHANGE = new Input("SLOT_CHANGE");
	public static final Input SWAP_HANDS = new Input("SWAP_HANDS");
	public static final Input THROW_ITEM = new Input("THROW_ITEM");

	private String name;

	private Input(String name) {
		this.name = name;
		INPUTS.put(name, this);
	}

	public static Input registerInput(String name) {
		Input val = INPUTS.get(name);

		return val == null ? new Input(name) : val;
	}

	public static Input getInput(String name) {
		return INPUTS.get(name);
	}

	public String getName() {
		return name;
	}

	@Override
	public boolean equals(Object o) {
		return (this == o) || ((o instanceof Input) && this.name.equals(((Input) o).name));
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}

	@Override
	public String toString() {
		return this.name;
	}

}
