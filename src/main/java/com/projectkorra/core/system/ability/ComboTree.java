package com.projectkorra.core.system.ability;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.projectkorra.core.system.ability.activation.Activation;
import com.projectkorra.core.util.data.DistinctPair;

public class ComboTree {

	private Ability ability;
	private Activation activation;
	private ComboTree root;
	private DistinctPair<Ability, Activation> pair;
	private List<ComboTree> branches;
	
	public ComboTree() {
		this(null, null, null);
	}
	
	public ComboTree(Ability ability, Activation type) {
		this(ability, type, null);
	}
	
	public ComboTree(Ability ability, Activation type, ComboTree root) {
		this.ability = ability;
		this.activation = type;
		this.pair = DistinctPair.of(ability, type);
		this.root = root;
		this.branches = new ArrayList<>();
	}
	
	public boolean exists(Ability ability, Activation type) {
		for (ComboTree branch : branches) {
			if (branch.ability == ability && branch.activation == type) {
				return true;
			}
		}
		
		return false;
	}
	
	public ComboTree getBranch(Ability ability, Activation type) {
		for (ComboTree branch : branches) {
			if (branch.ability == ability && branch.activation == type) {
				return branch;
			}
		}
		
		return null;
	}
	
	private ComboTree branch(Ability ability, Activation type) {
		for (ComboTree branch : branches) {
			if (branch.ability == ability && branch.activation == type) {
				return branch;
			}
		}
		
		ComboTree branch = new ComboTree(ability, type);
		branches.add(branch);
		return branch;
	}
	
	void build(List<DistinctPair<Ability, Activation>> sequence) {
		ComboTree branch = this;
		for (DistinctPair<Ability, Activation> step : sequence) {
			branch = branch.branch(step.getLeft(), step.getRight());
		}
	}
	
	public boolean isEnd() {
		return branches.isEmpty();
	}
	
	public List<DistinctPair<Ability, Activation>> sequence() {
		LinkedList<DistinctPair<Ability, Activation>> list = new LinkedList<>();
		ComboTree branch = this;
		while (branch.root != null && branch.ability != null && branch.activation != null) {
			list.addFirst(branch.pair);
			branch = branch.root;
		}
		return list;
	}
}
