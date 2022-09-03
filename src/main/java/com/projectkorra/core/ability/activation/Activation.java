package com.projectkorra.core.ability.activation;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import com.projectkorra.core.ability.User;
import com.projectkorra.core.util.Pair;

public class Activation implements Activatable {
	private List<Pair<Long, ActivationNode>> conditions = new ArrayList<>();
	private int step;

	public Activation() {
		this.step = 0;
	}

	public Activation(Activation a) {
		this.step = 0;
		a.conditions.forEach((i) -> this.conditions.add(i.clone()));
	}

	@SuppressWarnings("unchecked")
	public Activation chain(long time, Predicate<User>... conditions) {
		ActivationNode a = new ActivationNode(conditions);

		Pair<Long, ActivationNode> pair = new Pair<>(time, a);

		this.conditions.add(pair);

		return this;
	}

	public Activation chain(long time, ActivationNode a) {
		Pair<Long, ActivationNode> pair = new Pair<Long, ActivationNode>(time, a.clone());
		this.conditions.add(pair);

		return this;
	}

	@Override
	public boolean activate(User o) {
		Pair<Long, ActivationNode> currentCriterion = conditions.get(step);

		Pair<Long, ActivationNode> lastMetCriterion = step > 0 ? conditions.get(step - 1) : null;

		boolean criteriaMet = false;

		System.out.println("Checking activation " + step);

		try {
			criteriaMet = currentCriterion.getValue().activate(o);
		} catch (ClassCastException e) {
			System.out.println("Error: Criteria could not be verified");
		}

		if (criteriaMet) {
			this.step++;
			System.out.println("Successfully met criteria " + step);
			if (lastMetCriterion != null && lastMetCriterion.getValue().timeElapsed() > currentCriterion.getKey()) {
				this.step = 0;
				System.out.println("But I was too slow....");
			} else {
				if (step >= conditions.size()) {
					System.out.println("You've made it through! Nice!");
					this.step = 0;
					return true;
				}
			}
		} else {
			System.out.println("You've made it ");
			this.step = 0;
		}

		return false;
	}

	@Override
	public boolean equals(Object o) {
		Activation s = (Activation) o;

		if (s.conditions.size() != this.conditions.size()) {
			return false;
		}
		boolean equals = true;
		for (int i = 0; i < conditions.size(); i++) {
			Pair<Long, ActivationNode> o1 = s.conditions.get(i);
			Pair<Long, ActivationNode> o2 = this.conditions.get(i);
			equals = equals && o1.equals(o2);
		}

		return equals;
	}

	@Override
	public Activation clone() {
		return new Activation(this);
	}

	public int getStep() {
		return step;
	}
}
