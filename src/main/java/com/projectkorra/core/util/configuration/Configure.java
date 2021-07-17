package com.projectkorra.core.util.configuration;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Configure {

	/**
	 * Gets the config path for the configurable field
	 * @return config path
	 */
	public String value();
	//public String comment() default "";
}
