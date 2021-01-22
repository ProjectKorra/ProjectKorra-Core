package com.projectkorra.core.collision;

public class CollisionData {

	private String left, right, effect;
	private String[] args;
	private CollisionOperator type;
	
	public CollisionData(String first, String second, CollisionOperator type) {
		this(first, second, type, null, null);
	}
	
	public CollisionData(String first, String second, CollisionOperator type, String effect, String[] args) {
		this.left = first;
		this.right = second;
		this.type = type;
		this.effect = effect;
		this.args = args;
	}
	
	public String getLeft() {
		return left;
	}
	
	public String getSecond() {
		return right;
	}
	
	public CollisionOperator getOperator() {
		return type;
	}
	
	public String getEffect() {
		return effect;
	}
	
	public String[] getArgs() {
		return args;
	}
}
