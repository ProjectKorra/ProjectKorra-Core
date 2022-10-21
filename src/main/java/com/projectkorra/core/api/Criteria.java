package com.projectkorra.core.api;

import java.util.function.Predicate;

public class Criteria {

    Predicate<User> condition;
    Predicate<User> excludes;
    long timeTested;
    boolean reset;

    public Criteria(Predicate<User> condition) {
        this(true, condition, b -> false);
    }

    public Criteria(boolean reset, Predicate<User> condition) {
        this(reset, condition, b -> false);
    }

    public Criteria(boolean reset, Predicate<User> condition, Predicate<User> excludes) {
        this.reset = reset;
        this.condition = condition;
        this.excludes = excludes;
    }

    public boolean test(User u) {
        timeTested = System.currentTimeMillis();
        return condition.test(u);
    }

    public long timeElapsed() {
        return System.currentTimeMillis() - timeTested;
    }

    public long timeTested() {
        return timeTested;
    }

    public boolean reset() {
        return reset;
    }

    public Predicate<User> getExcludes() {
        return excludes;
    }

}