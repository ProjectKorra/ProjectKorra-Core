package com.projectkorra.core.system.ability;

import java.util.ArrayList;
import java.util.List;

import com.projectkorra.core.system.ability.activation.Activation;
import com.projectkorra.core.system.ability.activation.SequenceInfo;

public class ComboAgent {
	
	public enum Result {
		FAILED, COMPLETE, INCOMPLETE;
	}

	private ComboTree current;
	private List<SequenceInfo> sequence;
	
	public ComboAgent(ComboTree root) {
		this.current = root;
		this.sequence = new ArrayList<>();
	}
	
	public Result next(Ability ability, Activation trigger) {
		sequence.add(SequenceInfo.of(ability, trigger));
		current = current.getBranch(ability, trigger);
		
		return current == null ? Result.FAILED : (current.isEnd() ? Result.COMPLETE : Result.INCOMPLETE);
	}
	
	public List<SequenceInfo> getSequence() {
		return sequence;
	}
}
