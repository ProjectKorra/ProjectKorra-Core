package com.projectkorra.core.api.activation;

import com.projectkorra.core.api.User;

public interface Activatable {
	boolean activate(User b);

	boolean equals(Object o);

}
