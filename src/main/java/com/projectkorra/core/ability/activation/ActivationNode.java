package com.projectkorra.core.ability.activation;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

import com.projectkorra.core.ability.User;

public class ActivationNode implements Activatable {
	private long timeLastBendingUserested;
	private Set<Predicate<User>> conditions = new HashSet<>();

	@SafeVarargs
	public ActivationNode(Predicate<User>... predicate) {
		this.timeLastBendingUserested = -1;
		for (Predicate<User> p : predicate) {
			conditions.add(p);
		}
	}

	@SuppressWarnings("unchecked")
	private ActivationNode(Set<Predicate<User>> predicate) {
		this((Predicate<User>[]) predicate.toArray());
	}

	@Override
	public boolean activate(User o) {
		this.timeLastBendingUserested = System.currentTimeMillis();

		for (Predicate<User> condition : conditions) {
			System.out.println("Testing condition");
			if (!condition.test(o)) {
				System.out.println("Failed condition");
				return false;
			}
		}
		System.out.println("Passed condition");
		return true;
	}

	@Override
	public boolean equals(Object o) {
		ActivationNode s = (ActivationNode) o;

		return s.conditions.equals(this.conditions);
	}

	public long timeLastBendingUserested() {
		return timeLastBendingUserested;
	}

	public long timeElapsed() {
		return System.currentTimeMillis() - timeLastBendingUserested;
	}

	public ActivationNode clone() {
		return new ActivationNode(conditions);
	}

}
