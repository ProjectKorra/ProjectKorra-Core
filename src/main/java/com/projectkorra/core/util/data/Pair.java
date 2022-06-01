package com.projectkorra.core.util.data;

/**
 * This class denotes an indistinct pair, e.g. the order of the elements do not
 * matter. This means a pair of (a, b) is the same as a pair of (b, a).
 *
 * @param <L> Lefthand type
 * @param <R> Righthand type
 */
public class Pair<L, R> {

	protected L left;
	protected R right;

	Pair(L left, R right) {
		this.left = left;
		this.right = right;
	}

	/**
	 * Gets the lefthand value of this pair
	 * 
	 * @return lefthand value
	 */
	public L getLeft() {
		return left;
	}

	/**
	 * Gets the righthand value of this pair
	 * 
	 * @return righthand value
	 */
	public R getRight() {
		return right;
	}

	@Override
	public int hashCode() {
		return left.hashCode() + right.hashCode();
	}

	@Override
	public boolean equals(Object other) {
		if (!(other instanceof Pair)) {
			return false;
		}

		Pair<?, ?> pother = (Pair<?, ?>) other;
		return (left.equals(pother.left) && right.equals(pother.right)) || (left.equals(pother.right) && right.equals(pother.left));
	}
}
