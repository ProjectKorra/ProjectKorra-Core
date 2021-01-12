package com.projectkorra.core.util.function;

@FunctionalInterface
public interface TriPredicate<A, B, C> {

	public boolean test(A a, B b, C c);
	
	public default TriPredicate<A, B, C> and(TriPredicate<? super A, ? super B, ? super C> other) throws NullPointerException {
		return (a, b, c) -> test(a, b, c) && other.test(a, b, c);
	}
	
	public default TriPredicate<A, B, C> or(TriPredicate<? super A, ? super B, ? super C> other) throws NullPointerException {
		return (a, b, c) -> test(a, b, c) || other.test(a, b, c);
	}
	
	public default TriPredicate<A, B, C> negate() {
		return (a, b, c) -> !test(a, b, c);
	}
}
