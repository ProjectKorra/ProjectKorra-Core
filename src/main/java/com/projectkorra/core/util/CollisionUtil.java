package com.projectkorra.core.util;

import com.projectkorra.core.collision.Collidable;
import com.projectkorra.core.collision.CollisionData;
import com.projectkorra.core.collision.CollisionOperator;
import com.projectkorra.core.util.data.Pair;
import com.projectkorra.core.util.data.Pairing;

public class CollisionUtil {

	private CollisionUtil() {
	}

	public static Pair<String, String> pairTags(Collidable first, Collidable second) {
		return Pairing.of(first.getTag().toLowerCase(), second.getTag().toLowerCase());
	}

	public static CollisionData parse(String line) throws CollisionParseException {
		String[] split = line.split(" ");
		if (split.length < 3) {
			throw new CollisionParseException(line, "arg amount");
		}

		CollisionOperator op;
		try {
			op = CollisionOperator.fromSymbol(split[1]);
		} catch (Exception e) {
			throw new CollisionParseException(line, "operator");
		}

		String[] effects = null;
		String[][] args = null;

		if (split.length > 3) {
			effects = new String[split.length - 3];
			args = new String[split.length - 3][];
			for (int i = 0; i < split.length - 3; ++i) {
				String[] other = split[i + 3].split(":");
				effects[i] = other[0];
				args[i] = other[1].split(",");
			}
		}

		return new CollisionData(split[0], split[2], op, effects, args);
	}

	public static String stringify(CollisionData data) {
		return data.getLeft() + " " + data.getOperator().getSymbol() + " " + data.getRight() /* + */;
	}

	public static class CollisionParseException extends Exception {
		private static final long serialVersionUID = -8326822547202771825L;

		private CollisionParseException(String line, String field) {
			super("Problem with " + field + " in line " + line);
		}
	}
}
