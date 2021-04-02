package com.projectkorra.core.system.ability;

import java.util.Arrays;

import com.projectkorra.core.system.ability.type.Bindable;

public class AbilityBinds {
	
	public static enum AbilityBindResult {
		OUT_OF_BOUNDS, UNBINDABLE, SUCCESS;
	}

	private Ability[] binds;
	
	public AbilityBinds() {
		this.binds = new Ability[9];
	}
	
	public AbilityBinds(Ability[] binds) {
		this.binds = Arrays.copyOf(binds, 9);
	}
	
	public Ability get(int index) throws ArrayIndexOutOfBoundsException {
		if (index < 0 || index > 8) {
			throw new ArrayIndexOutOfBoundsException();
		}
		
		return binds[index];
	}
	
	public AbilityBindResult set(int index, Ability ability) {
		if (index < 0 || index > 8) {
			return AbilityBindResult.OUT_OF_BOUNDS;
		} else if (!(ability instanceof Bindable)) {
			return AbilityBindResult.UNBINDABLE;
		}
		
		binds[index] = ability;
		return AbilityBindResult.SUCCESS;
	}
	
	public void copy(AbilityBinds other) {
		binds = Arrays.copyOf(other.binds, 9);
	}
	
	public static AbilityBinds copyOf(AbilityBinds other) {
		return new AbilityBinds(other.binds);
	}
}
