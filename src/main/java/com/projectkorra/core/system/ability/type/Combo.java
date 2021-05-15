package com.projectkorra.core.system.ability.type;

import java.util.Queue;

import com.projectkorra.core.system.ability.activation.SequenceInfo;

public interface Combo {

	public Queue<SequenceInfo> getSequence();
}
