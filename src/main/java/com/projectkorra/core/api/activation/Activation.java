package com.projectkorra.core.api.activation;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import com.projectkorra.core.api.User;
import com.projectkorra.core.game.InputType;
import com.projectkorra.core.util.Pair;

public class Activation implements Activatable {
	private List<Pair<Pair<Long, Long>, ActivationNode>> conditions = new ArrayList<>();
	private int step;

	public Activation() {
		this.step = 0;
	}

	public Activation(Activation a) {
		this.step = 0;
		a.conditions.forEach((i) -> this.conditions.add(i.clone()));
	}

	public Activation check(InputType... inputs) {
		return check(30000, inputs);
	}

	public Activation check(long maxTime, InputType... inputs) {
		return check(maxTime, true, inputs);
	}

	public Activation check(long maxTime, boolean reset, InputType... inputs) {
		return check(0, maxTime, reset, inputs);
	}

	public Activation check(long t1, long t2, boolean reset, InputType... inputs) {
		return check(t1, t2, reset, asPredicates(inputs));
	}

	@SuppressWarnings("unchecked")
	public Activation check(Predicate<User>... conditions) {
		return check(30000, conditions);
	}

	@SuppressWarnings("unchecked")
	public Activation check(long maxTime, Predicate<User>... conditions) {
		return check(maxTime, true, conditions);
	}

	@SuppressWarnings("unchecked")
	public Activation check(long maxTime, boolean reset, Predicate<User>... conditions) {
		return check(0, maxTime, reset, conditions);
	}

	@SuppressWarnings("unchecked")
	public Activation check(long t1, long t2, boolean reset, Predicate<User>... conditions) {
		ActivationNode a = new ActivationNode(reset, conditions);
		return check(t1, t2, a);
	}

	public Activation check(long t1, long t2, ActivationNode a) {
		Pair<Pair<Long, Long>, ActivationNode> pair = new Pair<>(new Pair<>(t1, t2), a);
		this.conditions.add(pair);
		return this;
	}

	public Activation exclude(InputType... excludes) {
		return exclude(true, asPredicates(excludes));
	}

	public Activation exclude(boolean reset, InputType... excludes) {
		return exclude(reset, asPredicates(excludes));
	}

	@SuppressWarnings("unchecked")
	public Activation exclude(boolean reset, Predicate<User>... excludes) {
		conditions.get(conditions.size() - 1).getValue().excludeOn(excludes).setReset(reset);
		return this;
	}

	public Activation excludeAll(InputType... excludes) {
		return excludeAll(true, asPredicates(excludes));
	}

	public Activation excludeAll(boolean reset, InputType... excludes) {
		return excludeAll(reset, asPredicates(excludes));
	}

	@SuppressWarnings("unchecked")
	public Activation excludeAll(boolean reset, Predicate<User>... excludes) {
		for (int i = 0; i < conditions.size(); i++) {
			conditions.get(i).getValue().excludeOn(excludes).setReset(reset);
		}
		return this;
	}

	@Override
	public boolean activate(User o) {
		Pair<Pair<Long, Long>, ActivationNode> current = conditions.get(step);

		Pair<Pair<Long, Long>, ActivationNode> lastMet = step > 0 ? conditions.get(step - 1) : null;

		boolean criteriaMet = current.getValue().activate(o);

		long t1 = current.getKey().getKey();
		long t2 = current.getKey().getValue();

		long delta_t = lastMet != null ? lastMet.getValue().timeElapsed() : 0;

		if (criteriaMet) {
			System.out.println(this.hashCode() + " Criteria met ");
			if (lastMet != null && delta_t > t2) {
				System.out.println(this.hashCode() + " Time elapsed greater than time maximum ");
				this.step = 0;
			} else if ((lastMet != null && (t2 - t1) > (delta_t - t1)) || (lastMet == null)) {
				System.out.println(this.hashCode() + " Previous criteria null: " + (lastMet == null));
				System.out.println(this.hashCode() + " Time elapsed within interval, proceeding to next criteria ");
				this.step = (this.step + 1) % conditions.size();
				System.out.println(step == 0 ? this.hashCode() + " All criteria met, activating "
						: this.hashCode() + " Still have criteria to go ");
				return this.step == 0;
			} else {

			}
		} else {
			System.out.println(this.hashCode() + " Criteria not met");
			boolean reset = current.getValue().reset();
			for (Predicate<User> criteria : current.getValue().getExclude()) {
				if (criteria.test(o)) {
					System.out.println(this.hashCode() + " Valid " + (reset ? "exclude" : "break") + " criteria met");
					this.step = reset ? this.step : 0;
					return false;
				}
			}
			System.out.println(this.hashCode() + " No valid " + (reset ? "exclude" : "break") + " criteria");
			this.step = reset ? 0 : this.step;

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
			Pair<Pair<Long, Long>, ActivationNode> o1 = s.conditions.get(i);
			Pair<Pair<Long, Long>, ActivationNode> o2 = this.conditions.get(i);
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

	@SuppressWarnings("unchecked")
	private Predicate<User>[] asPredicates(InputType[] inputs) {
		List<Predicate<User>> predicates = new ArrayList<>();

		for (InputType input : inputs) {
			predicates.add(b -> b.did(input));
		}
		return (Predicate<User>[]) predicates.toArray();
	}
}
