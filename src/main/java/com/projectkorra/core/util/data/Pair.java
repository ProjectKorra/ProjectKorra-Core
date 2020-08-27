package com.projectkorra.core.util.data;

public class Pair<T, E> {

	private T first;
	private E second;
	
	private Pair(T first, E second) {
		this.first = first;
		this.second = second;
	}
	
	public T getFirst() {
		return first;
	}
	
	public E getSecond() {
		return second;
	}
	
	@Override
	public int hashCode() {
		return first.hashCode() + second.hashCode();
	}
	
	@Override
	public boolean equals(Object other) {
		if (!(other instanceof Pair)) {
			return false;
		}
		
		Pair<?, ?> pother = (Pair<?, ?>) other;
		return (first.equals(pother.first) && second.equals(pother.second)) || (first.equals(pother.second) && second.equals(pother.first));
	}
	
	public static <T, E> Pair<T, E> of(T first, E second) {
		return new Pair<T, E>(first, second);
	}
}
