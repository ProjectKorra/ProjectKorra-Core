package com.projectkorra.core.system.ability;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.projectkorra.core.system.ability.activation.Activation;
import com.projectkorra.core.system.ability.activation.SequenceInfo;

public class ComboTree {

	private ComboTree root;
	private SequenceInfo info;
	private List<ComboTree> branches;
	
	public ComboTree() {
		this(null, null, null);
	}
	
	public ComboTree(Ability ability, Activation type) {
		this(ability, type, null);
	}
	
	public ComboTree(Ability ability, Activation type, ComboTree root) {
		this.info = SequenceInfo.of(ability, type);
		this.root = root;
		this.branches = new ArrayList<>();
	}
	
	public boolean exists(Ability ability, Activation type) {
		for (ComboTree branch : branches) {
			if (branch.info.abilityEquals(ability) && branch.info.triggerEquals(type)) {
				return true;
			}
		}
		
		return false;
	}
	
	public ComboTree getBranch(Ability ability, Activation type) {
		for (ComboTree branch : branches) {
			if (branch.info.abilityEquals(ability) && branch.info.triggerEquals(type)) {
				return branch;
			}
		}
		
		return null;
	}
	
	private ComboTree branch(Ability ability, Activation type) {
		for (ComboTree branch : branches) {
			if (branch.info.abilityEquals(ability) && branch.info.triggerEquals(type)) {
				return branch;
			}
		}
		
		ComboTree branch = new ComboTree(ability, type, this);
		branches.add(branch);
		return branch;
	}
	
	void build(List<SequenceInfo> sequence) {
		ComboTree branch = this;
		for (SequenceInfo info : sequence) {
			branch = branch.branch(info.getAbility(), info.getTrigger());
		}
	}
	
	public boolean isEnd() {
		return branches.isEmpty();
	}
	
	public List<SequenceInfo> sequence() {
		LinkedList<SequenceInfo> sequence = new LinkedList<>();
		ComboTree branch = this;
		while (branch.root != null && branch.info != null) {
			sequence.addFirst(branch.info);
			branch = branch.root;
		}
		return sequence;
	}
}
