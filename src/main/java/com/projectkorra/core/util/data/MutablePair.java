package com.projectkorra.core.util.data;

/**
 * This class denotes a mutable extension of an indistinct pair, see
 * {@link Pair}
 * 
 * @param <L> Lefthand type
 * @param <R> Righthand type
 */
public class MutablePair<L, R> extends Pair<L, R> {

	MutablePair(L left, R right) {
		super(left, right);
	}

	/**
	 * Set the lefthand value of this pair to a new value
	 * 
	 * @param left new value
	 * @return same pair object
	 */
	public MutablePair<L, R> setLeft(L left) {
		this.left = left;
		return this;
	}

	/**
	 * Set the righthand value of this pair to a new value
	 * 
	 * @param right new value
	 * @return same pair object
	 */
	public MutablePair<L, R> setRight(R right) {
		this.right = right;
		return this;
	}

	/**
	 * Set both sides of this pair to new values
	 * 
	 * @param left  new lefthand value
	 * @param right new righthand value
	 * @return same pair object
	 */
	public MutablePair<L, R> set(L left, R right) {
		this.left = left;
		this.right = right;
		return this;
	}
}
