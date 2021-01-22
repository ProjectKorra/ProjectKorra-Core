package com.projectkorra.core.util.data;

public class Holder<T> {

	private T held;
	
	Holder(T obj) {
		this.held = obj;
	}
	
	public T getHeld() {
		return held;
	}
	
	public void setHeld(T obj) {
		this.held = obj;
	}
	
	public static <T> Holder<T> of(T obj) {
		return new Holder<T>(obj);
	}
}
