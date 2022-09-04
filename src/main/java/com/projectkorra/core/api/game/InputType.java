package com.projectkorra.core.api.game;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class InputType {

	public static final InputType SHIFT_DOWN = new InputType("SHIFT_DOWN");
	public static final InputType SHIFT_UP = new InputType("SHIFT_UP");
	public static final InputType SPRINT_ON = new InputType("SPRINT_ON");
	public static final InputType SPRINT_OFF = new InputType("SPRINT_OFF");

	public static final InputType LEFT_CLICK = new InputType("LEFT_CLICK");
	public static final InputType LEFT_CLICK_BLOCK = new InputType("LEFT_CLICK_BLOCK");
	public static final InputType RIGHT_CLICK_AIR = new InputType("RIGHT_CLICK_AIR");
	public static final InputType RIGHT_CLICK_BLOCK = new InputType("RIGHT_CLICK_BLOCK");
	public static final InputType RIGHT_CLICK_ENTITY = new InputType("RIGHT_CLICK_ENTITY");
	public static final InputType LEFT_CLICK_OFF_HAND = new InputType("LEFT_CLICK_OFF_HAND");
	public static final InputType LEFT_CLICK_BLOCK_OFF_HAND = new InputType("LEFT_CLICK_BLOCK_OFF_HAND");
	public static final InputType RIGHT_CLICK_AIR_OFF_HAND = new InputType("RIGHT_CLICK_AIR_OFF_HAND");
	public static final InputType RIGHT_CLICK_BLOCK_OFF_HAND = new InputType("RIGHT_CLICK_BLOCK_OFF_HAND");

	public static final InputType LEFT = new InputType("LEFT");
	public static final InputType RIGHT = new InputType("RIGHT");
	public static final InputType FORWARD = new InputType("FORWARD");
	public static final InputType BACKWARD = new InputType("BACKWARD");
	public static final InputType UP = new InputType("UP");
	public static final InputType DOWN = new InputType("DOWN");
	public static final InputType FLIGHT_ON = new InputType("FLIGHT_ON");
	public static final InputType FLIGHT_OFF = new InputType("FLIGHT_OFF");
	public static final InputType JUMP = new InputType("JUMP");
	public static final InputType KNOCK_BACK = new InputType("KNOCKBACK");

	public static final InputType SLOT_CHANGE = new InputType("SLOT_CHANGE");
	public static final InputType SWAP_HANDS = new InputType("SWAP_HANDS");
	public static final InputType THROW_ITEM = new InputType("THROW_ITEM");

	public static final Map<String, InputType> INPUTS = new HashMap<>(30);

	public static Collection<InputType> values = INPUTS.values();

	private String name;

	private InputType(String name) {
		this.name = name;
		INPUTS.put(name, this);
	}

	public static InputType registerInput(String name) {
		InputType val = INPUTS.get(name);

		return val == null ? new InputType(name) : val;
	}

	public static InputType getInput(String name) {
		return INPUTS.get(name);
	}

	public String getName() {
		return name;
	}

	@Override
	public boolean equals(Object o) {
		return (this == o) || ((o instanceof InputType) && this.name.equals(((InputType) o).name));
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
