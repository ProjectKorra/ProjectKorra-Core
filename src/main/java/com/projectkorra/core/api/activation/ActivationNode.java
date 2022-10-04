package com.projectkorra.core.api.activation;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

import com.projectkorra.core.api.User;
import com.projectkorra.core.api.game.Input;

public class ActivationNode implements Activatable {
	private long lastTestTime;
	private boolean reset = true;
	private Set<Predicate<User>> conditions = new HashSet<>();
	private Set<Predicate<User>> exclude = new HashSet<>();

	@SafeVarargs
	public ActivationNode(Input... inputs) {
		this(true, inputs);
	}

	@SafeVarargs
	public ActivationNode(boolean reset, Input... inputs) {
		this.lastTestTime = -1;
		this.reset = reset;
		addConditions(inputs);
	}

	@SafeVarargs
	public ActivationNode(Predicate<User>... predicate) {
		this(true, predicate);
	}

	@SafeVarargs
	public ActivationNode(boolean reset, Predicate<User>... predicate) {
		this.lastTestTime = -1;
		this.reset = reset;
		addConditions(predicate);
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
	public ActivationNode exclude(Predicate<User>... predicates) {
		for (Predicate<User> p : (Predicate<User>[]) predicates) {
			exclude.add(p);
		}
		return this;
	}

	public ActivationNode exclude(Input... predicates) {
		for (Input p : predicates) {
			exclude.add(b -> b.did(p));
		}
		return this;
	}

	@SuppressWarnings("unchecked")
	protected void addConditions(Predicate<User>... conditions) {
		if (conditions == null) {
			return;
		}
		for (Predicate<User> p : conditions) {
			this.conditions.add(p);
		}
	}

	protected void addConditions(Input... conditions) {
		if (conditions == null) {
			return;
		}
		for (Input p : conditions) {
			this.conditions.add(b -> b.did(p));
		}
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
