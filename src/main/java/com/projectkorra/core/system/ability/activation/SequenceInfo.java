package com.projectkorra.core.system.ability.activation;

import com.projectkorra.core.system.ability.Ability;

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
	
	public static SequenceInfo of(Ability ability, Activation trigger) {
		if (ability == null || trigger == null) {
			return null;
		}
		
		return new SequenceInfo(ability, trigger);
	}
}
