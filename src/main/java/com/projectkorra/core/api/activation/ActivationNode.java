package com.projectkorra.core.api.activation;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

import com.projectkorra.core.api.User;

public class ActivationNode implements Activatable {
	private long lastTestTime;
	private boolean reset = true;
	private Set<Predicate<User>> conditions = new HashSet<>();
	private Set<Predicate<User>> exclude = new HashSet<>();

	@SafeVarargs
	public ActivationNode(Predicate<User>... predicate) {
		this(true, predicate);
	}

	@SafeVarargs
	public ActivationNode(boolean reset, Predicate<User>... predicate) {
		this.lastTestTime = -1;
		this.reset = reset;
		for (Predicate<User> p : predicate) {
			conditions.add(p);
		}
	}

	@SuppressWarnings("unchecked")
	private ActivationNode(boolean reset, Set<Predicate<User>> predicate) {
		this(reset, (Predicate<User>[]) predicate.toArray());
	}

	@Override
	public boolean activate(User o) {
		this.lastTestTime = System.currentTimeMillis();

		for (Predicate<User> condition : conditions) {
			System.out.println(this.hashCode() + " Testing condition ");
			if (!condition.test(o)) {
				System.out.println(this.hashCode() + " Failed condition ");
				return false;
			}
		}
		System.out.println(this.hashCode() + " Passed condition ");
		return true;
	}

	@SuppressWarnings("unchecked")
	public ActivationNode excludeOn(Predicate<User>... predicates) {
		for (Predicate<User> p : (Predicate<User>[]) predicates) {
			exclude.add(p);
		}
		return this;
	}

	@Override
	public boolean equals(Object o) {
		ActivationNode s = (ActivationNode) o;

		return s.conditions.equals(this.conditions);
	}

	public long lastTestTime() {
		return this.lastTestTime;
	}

	public long timeElapsed() {
		return System.currentTimeMillis() - this.lastTestTime;
	}

	public boolean reset() {
		return reset;
	}

	public void setReset(boolean reset) {
		this.reset = reset;
	}

	public Set<Predicate<User>> getExclude() {
		return exclude;
	}

	public ActivationNode clone() {
		ActivationNode clone = new ActivationNode(reset, conditions);
		clone.exclude = this.exclude;
		return clone;
	}

}
