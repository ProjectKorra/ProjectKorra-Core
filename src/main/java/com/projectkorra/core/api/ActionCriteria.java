package com.projectkorra.core.api;

import java.util.List;
import java.util.function.BiFunction;

public class ActionCriteria extends Sequence<List<AbilityInstance>> {

    public ActionCriteria(BiFunction<User, AbilityInfo, List<AbilityInstance>> action) {
        super(action);
    }
}
