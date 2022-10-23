package com.projectkorra.core.api;

import java.util.function.Predicate;

public class TimedCriteria extends Criteria {

    long t1;
    long t2;

    public TimedCriteria(long t1, long t2, boolean reset, Predicate<User> condition) {
        super(reset, condition);
        this.t1 = t1;
        this.t2 = t2;
    }

    /**
     * @return the t1
     */
    public long getT1() {
        return t1;
    }

    /**
     * @return the t2
     */
    public long getT2() {
        return t2;
    }

    @Override
    public TimedCriteria clone() {

        return new TimedCriteria(t1, t2, reset, condition);
    }
}