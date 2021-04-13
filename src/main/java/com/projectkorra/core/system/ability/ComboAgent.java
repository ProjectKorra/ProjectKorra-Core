package com.projectkorra.core.system.ability;

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
		this.sequence = null;
	}
	
	/**
	 * Update the agent to the next branch of the {@link ComboTree} if possible
	 * @param ability The given {@link Ability}
	 * @param trigger The given {@link Activation}
	 * @return {@link Result} of updating this agent
	 */
	public Result update(Ability ability, Activation trigger) {
		sequence.add(SequenceInfo.of(ability, trigger));
		current = current.getBranch(ability, trigger);
		
		return current == null ? Result.FAILED : (current.doesBranch() ? Result.INCOMPLETE : Result.COMPLETE);
	}
	
	/**
	 * Gets the sequence that was followed by this agent
	 * @return followed sequence
	 */
	public List<SequenceInfo> getSequence() {
		return sequence;
	}
}
