package com.projectkorra.core.api;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.projectkorra.core.api.activation.Activation;
import com.projectkorra.core.api.game.Input;
import com.projectkorra.core.util.Pair;

public class AbilityManager {
	public static final Set<AbilityInfo> infos = new HashSet<>(100);

	public static void tick() {
		for (User user : UserManager.users) {
			Iterator<Ability> iter = user.getInstances().iterator();
			while (iter.hasNext()) {
				Ability i = iter.next();
				if (i.stopped()) {
					iter.remove();
				} else if (i.started()) {
					i.tick();
				} else {
					i.start();
				}
			}
		}

		for (User user : UserManager.users) {
			for (Input t : Input.values) {
				user.getInputs().put(t, new Pair<>(false, null));
			}
		}
	}

	public static void activate(User user) {
		if (user == null) {
			return;
		}
		for (Pair<AbilityInfo, Activation> e : user.getActivations(true)) {
			if (!e.getKey().needsMovement() && e.getValue().activate(user)) {
				user.getInstances().add(e.getKey().createInstance(user));
			}
		}
	}

	public static void activateMovement(User user) {
		if (user == null) {
			return;
		}
		for (Pair<AbilityInfo, Activation> e : user.getActivations(true)) {
			if (e.getKey().needsMovement() && e.getValue().activate(user)) {
				user.getInstances().add(e.getKey().createInstance(user));
			}
		}
	}

	public static void flagRemoval(User user, Ability ability, boolean cancel) {

	}
}
