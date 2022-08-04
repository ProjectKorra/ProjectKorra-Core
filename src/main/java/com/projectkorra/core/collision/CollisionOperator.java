package com.projectkorra.core.collision;

public enum CollisionOperator {
	NEITHER_CANCELED("="), FIRST_CANCELED("<"), SECOND_CANCELED(">"), BOTH_CANCELED("x");

	private String symbol;

	private CollisionOperator(String symbol) {
		this.symbol = symbol;
	}

	public String getSymbol() {
		return symbol;
	}

	public static CollisionOperator fromSymbol(String symbol) {
		switch (symbol) {
		case "=":
			return NEITHER_CANCELED;
		case "<":
			return FIRST_CANCELED;
		case ">":
			return SECOND_CANCELED;
		case "x":
			return BOTH_CANCELED;
		}

		throw new IllegalArgumentException("No operator from " + symbol + " found");
	}
}
