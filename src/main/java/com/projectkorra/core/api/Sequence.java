package com.projectkorra.core.api;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Predicate;

import com.projectkorra.core.api.game.Input;

public abstract class Sequence<T> {

    List<TimedCriteria> conditions;
    BiFunction<User, AbilityInfo, T> action;
    int step = 0;

    public Sequence(BiFunction<User, AbilityInfo, T> action) {
        this.action = action;
    }

    public Sequence(Sequence<T> seq) {
        this.action = seq.action;
        seq.conditions.forEach(i -> this.conditions.add(i.clone()));
    }

    public Sequence<T> check(Input... inputs) {
        return check(30000, inputs);
    }

    public Sequence<T> check(long maxTime, Input... inputs) {
        return check(maxTime, true, inputs);
    }

    public Sequence<T> check(long maxTime, boolean reset, Input... inputs) {
        return check(0, maxTime, reset, inputs);
    }

    public Sequence<T> check(long t1, long t2, boolean reset, Input... inputs) {
        return check(t1, t2, reset, asPredicate(inputs));
    }

    public Sequence<T> check(long t1, long t2, boolean reset, Predicate<User> predicate) {
        this.conditions.add(new TimedCriteria(t1, t2, reset, predicate));
        return this;
    }

    public boolean test(User u) {

        TimedCriteria curr = conditions.get(step);

        boolean valid = curr.test(u);

        if (valid) {
            if (step > 0) {
                TimedCriteria prev = conditions.get(step - 1);
                long t1 = curr.getT1();
                long t2 = curr.getT2();
                long dT = curr.timeTested - prev.timeTested;

                if (dT > t2 || dT < t1) {
                    this.step = curr.reset ? 0 : this.step;
                } else {
                    this.step++;
                }
            } else {
                this.step++;
            }
        } else {
            if (curr.reset) {
                this.step = curr.getExcludes().test(u) ? this.step : 0;
            } else {
                this.step = curr.getExcludes().test(u) ? 0 : this.step;
            }
        }

        if (step == conditions.size()) {
            this.step = 0;
            return true;
        } else {
            return false;
        }
    }

    T activate(User user, AbilityInfo info) {
        return this.action.apply(user, info);
    }

    private Predicate<User> asPredicate(Input[] inputs) {
        Predicate<User> predicate = b -> true;
        for (Input input : inputs) {
            predicate = predicate.and(b -> b.did(input));
        }
        return predicate;
    }
}
