package com.projectkorra.core.system.ability.activation;

import java.util.LinkedList;

import com.projectkorra.core.system.ability.Ability;
import com.projectkorra.core.util.data.DistinctPair;

public class Sequence extends Activation {

	LinkedList<DistinctPair<Ability, Activation>> sequence;
	
	Sequence(LinkedList<DistinctPair<Ability, Activation>> sequence) {
		super(sequence.toString());
		this.sequence = sequence;
	}
	
	public int length() {
		return sequence.size();
	}
	
	public DistinctPair<Ability, Activation> at(int index) {
		return sequence.get(index);
	}

	public boolean matches(Sequence other) {
		if (sequence.size() != other.sequence.size()) {
			return false;
		}
		
		for (int i = 0; i < sequence.size(); i++) {
			if (!sequence.get(i).equals(other.sequence.get(i))) {
				return false;
			}
		}
		
		return true;
	}
	
	public static class Builder {
		
		private LinkedList<DistinctPair<Ability, Activation>> sequence;
		
		public Builder() {
			this.sequence = new LinkedList<>();
		}
		
		public Builder add(DistinctPair<Ability, Activation> info) {
			this.sequence.add(info);
			return this;
		}
		
		public Builder clear() {
			this.sequence.clear();
			return this;
		}
		
		public Builder pop() {
			this.sequence.poll();
			return this;
		}
		
		public Sequence build() {
			return new Sequence(sequence);
		}
		
		public boolean contains(Sequence other) {
			if (other.length() > sequence.size()) {
				return false;
			}
			
			for (int i = 1; i <= other.length(); i++) {
				if (!sequence.get(sequence.size() - i).equals(other.at(other.length() - i))) {
					return false;
				}
			}
			
			return true;
		}
	}
}
