package com.projectkorra.core.system.ability;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.projectkorra.core.system.ability.activation.Activation;
import com.projectkorra.core.system.ability.activation.SequenceInfo;

public class ComboTree {
	
	public static final ComboTree ROOT = new ComboTree();

	private ComboTree root;
	private SequenceInfo info;
	private List<ComboTree> branches;
	
	private ComboTree() {
		this(null, null, null);
	}
	
	private ComboTree(Ability ability, Activation trigger) {
		this(ability, trigger, null);
	}
	
	private ComboTree(Ability ability, Activation trigger, ComboTree root) {
		this.info = SequenceInfo.of(ability, trigger);
		this.root = root;
		this.branches = new ArrayList<>();
	}

	/**
	 * Branches off this {@link ComboTree} with the given {@link Ability} and {@link Activation}.
	 * This either makes a new branch if one doesn't exist, or returns the one that exists.
	 * @param ability The {@link Ability} for the desired branch
	 * @param trigger The {@link Activation} for the desired branch
	 * @return next branch of this {@link ComboTree}
	 */
	private ComboTree branch(Ability ability, Activation trigger) {
		for (ComboTree branch : branches) {
			if (branch.info.matches(ability, trigger)) {
				return branch;
			}
		}
		
		ComboTree branch = new ComboTree(ability, trigger, this);
		branches.add(branch);
		return branch;
	}
	
	/**
	 * Check whether a branch exists for the given {@link Ability} and {@link Activation}
	 * @param ability The {@link Ability} of the desired branch
	 * @param trigger The {@link Activation} of the desired branch
	 * @return true if branch exists
	 */
	public boolean exists(Ability ability, Activation trigger) {
		for (ComboTree branch : branches) {
			if (branch.info.matches(ability, trigger)) {
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Get the branch for the given {@link Ability} and {@link Activation}
	 * @param ability The {@link Ability} of the desired branch
	 * @param trigger The {@link Activation} of the desired branch
	 * @return null if branch doesn't exist
	 */
	public ComboTree getBranch(Ability ability, Activation trigger) {
		for (ComboTree branch : branches) {
			if (branch.info.matches(ability, trigger)) {
				return branch;
			}
		}
		
		return null;
	}
	
	/**
	 * Check whether this {@link ComboTree} branches
	 * @return true if this {@link ComboTree} does branch
	 */
	public boolean doesBranch() {
		return !branches.isEmpty();
	}
	
	/**
	 * Get the sequence that lead to this branch
	 * @return branch sequence
	 */
	public Queue<SequenceInfo> sequence() {
		LinkedList<SequenceInfo> sequence = new LinkedList<>();
		ComboTree branch = this;
		while (branch.root != null && branch.info != null) {
			sequence.addFirst(branch.info);
			branch = branch.root;
		}
		return sequence;
	}
	
	/**
	 * Construct the branches of this {@link ComboTree} based on a given sequence
	 * @param sequence Queue of {@link SequenceInfo} for the activation sequence
	 * @return the given sequence if it is accepted
	 * @throws IllegalArgumentException if the last branch of the given sequence already exists and branches further
	 */
	static Queue<SequenceInfo> build(Queue<SequenceInfo> sequence) throws IllegalArgumentException {
		ComboTree branch = ROOT;
		
		for (SequenceInfo info : sequence) {
			branch = branch.branch(info.getAbility(), info.getTrigger());
		}
		
		if (branch.doesBranch()) {
			throw new IllegalArgumentException();
		}
		
		return sequence;
	}
}
