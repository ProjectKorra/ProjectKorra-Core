package com.projectkorra.core.system.ability.modifier;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.function.Function;

public class Modifier<T> {
	
	/**
	 * Marker annotation for fields that can be modified by a {@link Modifier} object
	 */
	@Retention(RUNTIME)
	@Target(FIELD)
	public static @interface Modifiable {}

	private String field;
	private Function<Object, T> func;
	
	public Modifier(String field, Function<Object, T> func) {
		this.field = field;
		this.func = func;
	}
	
	public String getField() {
		return field;
	}
	
	public T apply(Object value) {
		return func.apply(value);
	}
	
	/**
	 * Returns a premade function for doing multiplication
	 * @param modifier what to multiply by
	 * @return multiplication function
	 */
	public static Function<Number, Number> multiply(Number modifier) {
		return (x) -> {
			if (x instanceof Double) {
				return x.doubleValue() * modifier.doubleValue();
			} else if (x instanceof Float) {
				return x.floatValue() * modifier.floatValue();
			} else if (x instanceof Long) {
				return x.longValue() * modifier.longValue();
			} else if (x instanceof Integer) {
				return x.intValue() * modifier.intValue();
			} else {
				return x;
			}
		};
	}
	
	/**
	 * Returns a premade function for doing addition
	 * @param modifier what to add by
	 * @return addition function
	 */
	public static Function<Number, Number> add(Number modifier) {
		return (x) -> {
			if (x instanceof Double) {
				return x.doubleValue() + modifier.doubleValue();
			} else if (x instanceof Float) {
				return x.floatValue() + modifier.floatValue();
			} else if (x instanceof Long) {
				return x.longValue() + modifier.longValue();
			} else if (x instanceof Integer) {
				return x.intValue() + modifier.intValue();
			} else {
				return x;
			}
		};
	}
	
	/**
	 * Returns a premade function for doing subtraction
	 * @param modifier what to subtract by
	 * @return subtraction function
	 */
	public static Function<Number, Number> subtract(Number modifier) {
		return (x) -> {
			if (x instanceof Double) {
				return x.doubleValue() - modifier.doubleValue();
			} else if (x instanceof Float) {
				return x.floatValue() - modifier.floatValue();
			} else if (x instanceof Long) {
				return x.longValue() - modifier.longValue();
			} else if (x instanceof Integer) {
				return x.intValue() - modifier.intValue();
			} else {
				return x;
			}
		};
	}
	
	/**
	 * Returns a premade function for doing division. The function
	 * given by this method will return the input value in the case
	 * that the modifier is zero.
	 * @param modifier what to divide by
	 * @return division function
	 */
	public static Function<Number, Number> divide(Number modifier) {
		return (x) -> {
			if (modifier.doubleValue() == 0.0) {
				return x; 
			} else if (x instanceof Double) {
				return x.doubleValue() / modifier.doubleValue();
			} else if (x instanceof Float) {
				return x.floatValue() / modifier.floatValue();
			} else if (x instanceof Long) {
				return x.longValue() / modifier.longValue();
			} else if (x instanceof Integer) {
				return x.intValue() / modifier.intValue();
			} else {
				return x;
			}
		};
	}
}
