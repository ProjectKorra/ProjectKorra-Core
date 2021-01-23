package com.projectkorra.core.collision;

public class CollisionData {

	private String left, right;
	private String[] effects;
	private String[][] args;
	private CollisionOperator type;
	
	public CollisionData(String first, String second, CollisionOperator type) {
		this(first, second, type, null, null);
	}
	
	public CollisionData(String first, String second, CollisionOperator type, String[] effects, String[][] args) {
		this.left = first;
		this.right = second;
		this.type = type;
		this.effects = effects;
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
	
	public int getEffectAmount() {
		return effects.length;
	}
	
	public String getEffect(int index) {
		return effects[index];
	}
	
	public String[] getArgs(int index) {
		return args[index];
	}
}
