package com.projectkorra.core.util.data;

import java.util.Objects;

public class DistinctPair<T, E> {

	private T left;
	private E right;

	private DistinctPair(T left, E right) {
		this.left = left;
		this.right = right;
	}

	public T getLeft() {
		return left;
	}

	public E getRight() {
		return right;
	}

	@Override
	public int hashCode() {
		return Objects.hash(left, right);
	}

	@Override
	public boolean equals(Object other) {
		if (!(other instanceof DistinctPair)) {
			return false;
		}

		DistinctPair<?, ?> pother = (DistinctPair<?, ?>) other;
		return left.equals(pother.left) && right.equals(pother.right);
	}

	public static <T, E> DistinctPair<T, E> of(T first, E second) {
		return new DistinctPair<T, E>(first, second);
	}
}
