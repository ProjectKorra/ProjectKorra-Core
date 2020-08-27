package com.projectkorra.core.util;

import java.util.Arrays;
import java.util.List;

import com.projectkorra.core.collision.Collidable;
import com.projectkorra.core.collision.CollisionData;
import com.projectkorra.core.collision.CollisionOperator;
import com.projectkorra.core.util.data.Pair;

public class CollisionUtil {

	private CollisionUtil() {}
	
	private static List<String> symbols = Arrays.asList("==", "<=", "=>", "><");
	
	public static Pair<String, String> pair(Collidable first, Collidable second) {
		return Pair.of(first.getTag().toLowerCase(), second.getTag().toLowerCase());
	}
	
	public static CollisionData parse(String line) throws CollisionParseException {
		String[] split = line.split(" ");
		
		//first = split[0]
		//second = split[2]
		//symbol = split[1]
		
		if (!symbols.contains(split[1])) {
			throw new CollisionParseException(line, "symbol");
		}
		
		// add checks to make sure the abilities exist
		Runnable extra = null;
		// TODO (ben): add support for extra effects in collisions
		
		return new CollisionData(split[0], split[2], CollisionOperator.fromSymbol(split[1]), extra);
	}
	
	public static String stringify(CollisionData data) {
		return data.getFirst() + " " + data.getOperator().getSymbol() + " " + data.getSecond() /* + */;
	}
	
	public static class CollisionParseException extends Exception {
		private static final long serialVersionUID = -8326822547202771825L;
		
		private CollisionParseException(String line, String field) {
			super("Unable to parse " + field + " CollisionData from: " + line);
		}
	}
}
