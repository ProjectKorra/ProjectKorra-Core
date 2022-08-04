package com.projectkorra.core.util.configuration;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Configure {

	/**
	 * Gets the configuration key for the configurable field
	 * @return configuration key
	 */
	public String value() default "";
	
	/**
	 * A comment to be put in the file for this configurable field
	 * @return configuration comment
	 */
	public String comment() default "";
}
