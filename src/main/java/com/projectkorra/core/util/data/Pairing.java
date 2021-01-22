package com.projectkorra.core.util.data;

public final class Pairing {

	/**
	 * Create a new Pair with the given objects
	 * @param <L> Lefthand type
	 * @param <R> Righthand type
	 * @param left lefthand value
	 * @param right righthand value
	 * @return new pair of objects
	 */
	public static <L, R> Pair<L, R> of(L left, R right) {
		return new Pair<>(left, right);
	}
	
	/**
	 * Create a new mutable Pair with the given objects
	 * @param <L> Lefthand type
	 * @param <R> Righthand type
	 * @param left lefthand value
	 * @param right righthand value
	 * @return new mutable pair of objects
	 */
	public static <L, R> MutablePair<L, R> ofMutable(L left, R right) {
		return new MutablePair<>(left, right);
	}
}
