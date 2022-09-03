package com.projectkorra.core.ability;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.projectkorra.core.ability.activation.Activation;
import com.projectkorra.core.game.InputType;
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
			for (InputType t : InputType.values()) {
				user.getInputs().put(t, new Pair<>(false, null));
			}
		}
	}

	public static void activate(User user) {
		if (user == null) {
			return;
		}
		for (Pair<AbilityInfo, Activation> e : user.getActivations()) {
			if (e.getValue().activate(user)) {
				user.getInstances().add(e.getKey().createInstance(user));
			}
		}
	}
}
