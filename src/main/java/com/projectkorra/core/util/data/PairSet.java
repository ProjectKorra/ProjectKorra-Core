package com.projectkorra.core.util.data;

import java.util.HashSet;

public class PairSet<T, E> extends HashSet<Pair<T, E>> {

	private static final long serialVersionUID = 9064677383638093874L;

	public boolean add(T left, E right) {
		return add(Pairing.of(left, right));
	}
	
	public boolean remove(T left, E right) {
		return remove(Pairing.of(left, right));
	}
	
	public boolean contains(T left, E right) {
		return contains(Pairing.of(left, right));
	}
}
