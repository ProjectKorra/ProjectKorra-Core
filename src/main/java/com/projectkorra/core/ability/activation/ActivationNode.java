package com.projectkorra.core.ability.activation;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

public class ActivationNode<T> implements Activatable {
	private long timeLastTested;
	private Set<Predicate<T>> conditions;
	@SafeVarargs
	public ActivationNode(Predicate<T>... predicate) {
		this.timeLastTested = -1;
		conditions = new HashSet<>();
		for(Predicate<T> p : predicate) {
			conditions.add(p);
		}
	}
	
	@SuppressWarnings("unchecked")
	private ActivationNode(Set<Predicate<T>> predicate) {
		this((Predicate<T>[]) predicate.toArray());
	}
	
	@SuppressWarnings("unchecked")
	public boolean activate(Object o) {
		this.timeLastTested = System.currentTimeMillis();
		boolean temp = true;
		
		for(Predicate<T> condition : conditions) {
			try {
				temp = temp && condition.test((T) o);
			} catch(ClassCastException e) {
				return false;
			}
		}
		
		return temp;
	}
	
	@Override
	public boolean equals(Object o) {
		ActivationNode<?> s = (ActivationNode<?>) o;
		
		return s.conditions.equals(this.conditions);
	}
	
	public long timeLastTested() {
		return timeLastTested;
	}
	
	public long timeElapsed() {
		return System.currentTimeMillis() - timeLastTested;
	}
	
	public ActivationNode<T> clone() {
		return new ActivationNode<T>(conditions);
	}

}
