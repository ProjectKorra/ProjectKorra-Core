package com.projectkorra.core.ability.activation;

import com.projectkorra.core.ability.User;

public interface Activatable {
	boolean activate(User b);

	boolean equals(Object o);

	Activatable clone();
}
