package com.projectkorra.core.ability.activation;

import com.projectkorra.core.ability.BendingUser;

public interface Activatable {
	boolean activate(BendingUser b);
	boolean equals(Object o);
	Activatable clone();
}

