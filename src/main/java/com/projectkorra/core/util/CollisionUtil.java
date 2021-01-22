package com.projectkorra.core.util;

import com.projectkorra.core.collision.Collidable;
import com.projectkorra.core.collision.CollisionData;
import com.projectkorra.core.collision.CollisionOperator;
import com.projectkorra.core.util.data.Pair;
import com.projectkorra.core.util.data.Pairing;

public class CollisionUtil {

	private CollisionUtil() {}
	
	public static Pair<String, String> pairTags(Collidable first, Collidable second) {
		return Pairing.of(first.getTag().toLowerCase(), second.getTag().toLowerCase());
	}
	
	public static CollisionData parse(String line) throws CollisionParseException {
		String[] split = line.split(" ");
		if (split.length < 3 || split.length > 4) {
			throw new CollisionParseException(line, "arg amount");
		}
		
		CollisionOperator op;
		try {
			op = CollisionOperator.fromSymbol(split[1]);
		} catch (Exception e) {
			throw new CollisionParseException(line, "operator");
		}
		
		String effect = null;
		String[] args = null;
		if (split.length == 4) {
			String[] other = split[3].split("[\\(\\)]");
			effect = other[0];
			args = other[1].split(",");
		}
		
		return new CollisionData(split[0], split[2], op, effect, args);
	}
	
	public static String stringify(CollisionData data) {
		return data.getLeft() + " " + data.getOperator().getSymbol() + " " + data.getSecond() /* + */;
	}
	
	public static class CollisionParseException extends Exception {
		private static final long serialVersionUID = -8326822547202771825L;
		
		private CollisionParseException(String line, String field) {
			super("Problem with " + field + " in line " + line);
		}
	}
}
