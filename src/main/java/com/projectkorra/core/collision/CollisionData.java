package com.projectkorra.core.collision;

public class CollisionData {

	private String first, second;
	private CollisionOperator type;
	private Runnable extra;
	
	public CollisionData(String first, String second, CollisionOperator type, Runnable extra) {
		this.first = first;
		this.second = second;
		this.type = type;
		this.extra = extra;
	}
	
	public String getFirst() {
		return first;
	}
	
	public String getSecond() {
		return second;
	}
	
	public CollisionOperator getOperator() {
		return type;
	}
	
	public Runnable getExtra() {
		return extra;
	}
}
