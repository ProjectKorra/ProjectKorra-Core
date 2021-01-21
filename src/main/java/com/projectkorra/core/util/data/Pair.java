package com.projectkorra.core.util.data;

public class Pair<L, R> {

	protected L left;
	protected R right;
	
	Pair(L left, R right) {
		this.left = left;
		this.right = right;
	}
	
	public L getLeft() {
		return left;
	}
	
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
