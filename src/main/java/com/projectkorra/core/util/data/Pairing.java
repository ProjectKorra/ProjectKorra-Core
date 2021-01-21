package com.projectkorra.core.util.data;

public final class Pairing {

	public static <L, R> Pair<L, R> of(L left, R right) {
		return new Pair<>(left, right);
	}
	
	public static <L, R> MutablePair<L, R> ofMutable(L left, R right) {
		return new MutablePair<>(left, right);
	}
}
