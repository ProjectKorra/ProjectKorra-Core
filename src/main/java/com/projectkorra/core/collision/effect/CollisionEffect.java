package com.projectkorra.core.collision.effect;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang.Validate;

import com.projectkorra.core.event.BendingCollisionEvent;
import com.projectkorra.core.util.data.Pair;

public abstract class CollisionEffect {

	public static interface Parameter<T> {
		public T fromString(String arg);
	}

	public static final Parameter<String> STRING = (s) -> s;
	public static final Parameter<Double> DOUBLE = (s) -> Double.parseDouble(s);
	public static final Parameter<Integer> INT = (s) -> Integer.parseInt(s);
	public static final Parameter<Float> FLOAT = (s) -> Float.parseFloat(s);

	private static final Map<String, CollisionEffect> EFFECTS = new HashMap<>();

	private String label;
	private Pair<Parameter<?>, Object>[] params;

	@SafeVarargs
	public CollisionEffect(String label, Pair<Parameter<?>, Object>... params) {
		this.label = label.toLowerCase();
		this.params = params;
	}

	public final String stringify(String... args) {
		StringBuilder builder = new StringBuilder(label + "(");
		for (int i = 0; i < args.length; ++i) {
			builder.append(args[i]);
			if (i < args.length - 1) {
				builder.append(",");
			}
		}
		return builder.append(")").toString();
	}

	public void accept(BendingCollisionEvent event, String... args) {
		Object[] objs = new Object[args.length];
		for (int i = 0; i < args.length; ++i) {
			try {
				objs[i] = params[i].getLeft().fromString(args[i]);
			} catch (Exception e) {
				objs[i] = params[i].getRight();
			}
		}
		accept(event, objs);
	}

	/**
	 * Take in the collision event and a list of args to do this effect with. The
	 * args will be of the type given by the parameter defined in the same position
	 * for the effect.
	 * 
	 * @param event collision event for a collision attempting to use this effect
	 * @param args  parameters for this effect
	 */
	protected abstract void accept(BendingCollisionEvent event, Object... args);

	public static void register(CollisionEffect effect) {
		Validate.notNull(effect, "Attempted register of null collision effect");
		Validate.notNull(effect.label, "Attempted register of null-labeled collision effect");
		Validate.isTrue(EFFECTS.containsKey(effect.label), "Attempted register of collision effect with existing label");
		EFFECTS.put(effect.label, effect);
	}

	public static void defaults() {
		register(new CollisionParticles());
	}

	public static Optional<CollisionEffect> ofLabel(String label) {
		return Optional.ofNullable(EFFECTS.get(label.toLowerCase()));
	}
}
