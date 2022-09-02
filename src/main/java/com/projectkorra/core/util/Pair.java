package com.projectkorra.core.util;

public class Pair<T1, T2> {
	
	private T1 t1;
	private T2 t2;
	
	public Pair(T1 t1, T2 t2) {
		this.t1 = t1;
		this.t2 = t2;
	}
	
	public T1 getKey() {
		return t1;
	}
	
	public T2 getValue() {
		return t2;
	}
	
	@Override
	public boolean equals(Object o) {
		if(!(o instanceof Pair<?, ?>)) {
			return false;
		}
		Pair<?, ?> p = (Pair<?, ?>) o;
		
		return (t1.equals(p.t1) && t2.equals(p.t2)) || (t1.equals(p.t2) && t2.equals(p.t1));
	}
	
	@Override
	public Pair<T1, T2> clone() {
		return new Pair<>(t1, t2);
	}
}
