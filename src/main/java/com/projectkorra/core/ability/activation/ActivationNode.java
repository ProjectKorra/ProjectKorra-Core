package com.projectkorra.core.ability.activation;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

import com.projectkorra.core.ability.BendingUser;

public class ActivationNode implements Activatable {
	private long timeLastBendingUserested;
	private Set<Predicate<BendingUser>> conditions = new HashSet<>();
	@SafeVarargs
	public ActivationNode(Predicate<BendingUser>... predicate) {
		this.timeLastBendingUserested = -1;
		for(Predicate<BendingUser> p : predicate) {
			conditions.add(p);
		}
	}
	
	@SuppressWarnings("unchecked")
	private ActivationNode(Set<Predicate<BendingUser>> predicate) {
		this((Predicate<BendingUser>[]) predicate.toArray());
	}
	
	@Override
	public boolean activate(BendingUser o) {
		this.timeLastBendingUserested = System.currentTimeMillis();
		
		for(Predicate<BendingUser> condition : conditions) {
			System.out.println("Testing condition");
			if(!condition.test(o)) {
				System.out.println("Failed condition");
				return false;
			}
		}
		System.out.println("Passed condition");
		return true;
	}
	
	@Override
	public boolean equals(Object o) {
		ActivationNode s = (ActivationNode) o;
		
		return s.conditions.equals(this.conditions);
	}
	
	public long timeLastBendingUserested() {
		return timeLastBendingUserested;
	}
	
	public long timeElapsed() {
		return System.currentTimeMillis() - timeLastBendingUserested;
	}
	
	public ActivationNode clone() {
		return new ActivationNode(conditions);
	}

}
