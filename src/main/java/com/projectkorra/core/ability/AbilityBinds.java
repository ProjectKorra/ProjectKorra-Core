package com.projectkorra.core.ability;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;

import com.projectkorra.core.ability.type.Bindable;

public class AbilityBinds implements Iterable<Ability> {
	
	public static enum AbilityBindResult {
		FAIL_OUT_OF_BOUNDS, FAIL_NONBINDABLE, SUCCESS;
	}

	private Ability[] binds;
	
	public AbilityBinds() {
		this.binds = new Ability[9];
	}
	
	public AbilityBinds(Ability[] binds) {
		this.binds = Arrays.copyOf(binds, 9);
	}
	
	public Optional<Ability> get(int index) {
		if (index < 0 || index > 8) {
			return Optional.empty();
		}
		
		return Optional.ofNullable(binds[index]);
	}
	
	public AbilityBindResult set(int index, Ability ability) {
		if (index < 0 || index > 8) {
			return AbilityBindResult.FAIL_OUT_OF_BOUNDS;
		} else if (!(ability instanceof Bindable)) {
			return AbilityBindResult.FAIL_NONBINDABLE;
		}
		
		binds[index] = ability;
		return AbilityBindResult.SUCCESS;
	}

	public Set<Integer> slotsOf(Ability ability) {
		Set<Integer> slots = new HashSet<>();

		for (int i = 0; i < 9; ++i) {
			if (ability == binds[i]) {
				slots.add(i);
			}
		}

		return slots;
	}
	
	public void copy(AbilityBinds other) {
		binds = Arrays.copyOf(other.binds, 9);
	}

	@Override
	public AbilityBinds clone() {
		return new AbilityBinds(this.binds);
	}

	@Override
	public Iterator<Ability> iterator() {
		return new BindIterator(this);
	}

	private static class BindIterator implements Iterator<Ability> {

		private int cursor = -1;
		private Ability[] binds;

		private BindIterator(AbilityBinds abilityBinds) {
			this.binds = abilityBinds.binds;
		}

		@Override
		public boolean hasNext() {
			return cursor < 8;
		}

		@Override
		public Ability next() {
			return binds[++cursor];
		}

		/**
		 * @deprecated Unsupported operation
		 */
		@Deprecated
		public void remove() {
			throw new UnsupportedOperationException();
		}
	}
}
