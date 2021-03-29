package com.projectkorra.core.system.ability.type;

import java.util.List;

import com.projectkorra.core.system.ability.Ability;
import com.projectkorra.core.system.ability.activation.Activation;
import com.projectkorra.core.util.data.DistinctPair;

public interface Combo {

	public List<DistinctPair<Ability, Activation>> getSequence();
}
