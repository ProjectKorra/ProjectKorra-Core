package com.projectkorra.core.collision;

public enum CollisionOperator {
	NEITHER_CANCELED("=="), 
	FIRST_CANCELED("<="), 
	SECOND_CANCELED("=>"),
	BOTH_CANCELED("><");
	
	private String symbol;
	private CollisionOperator(String symbol) {
		this.symbol = symbol;
	}
	
	public String getSymbol() {
		return symbol;
	}
	
	public static CollisionOperator fromSymbol(String symbol) {
		if (symbol == "==") return NEITHER_CANCELED;
		else if (symbol == "<=") return FIRST_CANCELED;
		else if (symbol == "=>") return SECOND_CANCELED;
		else if (symbol == "><") return BOTH_CANCELED;
		else return null;
	}
}
