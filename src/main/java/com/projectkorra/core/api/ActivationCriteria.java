package com.projectkorra.core.api;

public class ActivationCriteria extends Sequence<AbilityInstance> {

    public ActivationCriteria() {
        super((u, i) -> i.createInstance(u));
    }
}
