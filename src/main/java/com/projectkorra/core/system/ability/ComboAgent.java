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
	
	/**
	 * Construct a new agent that starts from the root of the {@link ComboTree}
	 */
	public ComboAgent() {
		this(ComboTree.ROOT);
	}
	
	/**
	 * Construct a new agent that starts from the given {@link ComboTree}
	 * @param starting where to start in the {@link ComboTree}
	 */
	public ComboAgent(ComboTree starting) {
		this.current = starting;
		this.sequence = new ArrayList<>();
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
