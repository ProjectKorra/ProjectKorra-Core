package com.projectkorra.core.ability.activation;

public interface Activatable {
	<T> boolean activate(T o);
	boolean equals(Object o);
	Activatable clone();
}

