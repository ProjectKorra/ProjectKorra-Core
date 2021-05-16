package com.projectkorra.core.util.function;

@FunctionalInterface
public interface TriConsumer<A, B, C> {

	public void accept(A a, B b, C c);
	
	public default TriConsumer<A, B, C> andThen(TriConsumer<A, B, C> next) {
		return (a, b, c) -> {
			accept(a, b, c);
			next.accept(a, b, c);
		};
	}
}
