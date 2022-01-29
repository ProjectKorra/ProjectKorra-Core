package com.projectkorra.core.ability.activation;

import java.util.Collection;
import java.util.Iterator;

import com.projectkorra.core.ability.Ability;

public class SequenceInfo {

	private Ability ability;
	private Activation trigger;
	
	private SequenceInfo(Ability ability, Activation trigger) {
		this.ability = ability;
		this.trigger = trigger;
	}
	
	public Ability getAbility() {
		return ability;
	}
	
	public boolean abilityEquals(Ability other) {
		return ability == other;
	}
	
	public Activation getTrigger() {
		return trigger;
	}
	
	public boolean triggerEquals(Activation other) {
		return trigger == other;
	}
	
	/**
	 * Compare the contents of this and another {@link SequenceInfo}
	 * to check if they match
	 * @param other The other {@link SequenceInfo}
	 * @return true if contents match
	 */
	public boolean matches(SequenceInfo other) {
		return ability == other.ability && trigger == other.trigger;
	}
	
	/**
	 * Compare the contents of this {@link SequenceInfo} and the given
	 * {@link Ability} and {@link Activation}
	 * @param ability The {@link Ability} to compare against
	 * @param trigger The {@link Activation} to compare against
	 * @return true if contents match the given parameters
	 */
	public boolean matches(Ability ability, Activation trigger) {
		return this.ability == ability && this.trigger == trigger;
	}
	
	@Override
	public boolean equals(Object other) {
		if (other instanceof SequenceInfo) {
			return matches((SequenceInfo) other);
		}
		
		return false;
	}
	
	@Override
	public String toString() {
		return "[" + ability.getName() + ":" + trigger.toString() + "]";
	}
	
	public String toDisplay() {
		return trigger.getDisplay() + " " + ability.getName();
	}

	public static String stringify(Collection<SequenceInfo> infos) {
		String strung = "";
		Iterator<SequenceInfo> iter = infos.iterator();
		while (iter.hasNext()) {
			strung += iter.next().toString() + (iter.hasNext() ? "+" : "");
		}
		
		return strung;
	}
	
	public static SequenceInfo of(Ability ability, Activation trigger) {
		if (ability == null || trigger == null) {
			return null;
		}
		
		return new SequenceInfo(ability, trigger);
	}
}
