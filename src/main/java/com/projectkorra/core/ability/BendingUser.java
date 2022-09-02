package com.projectkorra.core.ability;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.entity.Entity;

import com.projectkorra.core.ability.activation.Activation;
import com.projectkorra.core.skills.Skill;
import com.projectkorra.core.util.Pair;
import com.projectkorra.core.game.InputType;

public abstract class BendingUser {
	
	private Set<Skill> skills;
	private List<Pair<AbilityInfo, Activation>> activations;
	private List<Ability> instances;
	private Map<AbilityInfo, Cooldown> cooldowns;
	private AbilityInfo[] binds;
	private Entity entity;
	private InputType mostRecentInput;
	
	public BendingUser(Entity entity) {
		this.entity = entity;
		skills = new HashSet<>(4);
	}
	
	public void does(InputType p) {
		this.mostRecentInput = p;
	}

	public boolean did(InputType p) {
		return this.mostRecentInput == p;
	}
	
	public boolean canBend(AbilityInfo info) {
		for(Skill s : info.skills) {
			if(!skills.contains(s)) {
				return false;
			}
		}
		return true;
	}
	
	public void addCooldown(AbilityInfo info, long cooldown) {
		Cooldown c = new Cooldown(cooldown);
		c.addCooldown();
		cooldowns.put(info, c);
	}
	
	public boolean onCooldown(AbilityInfo info) {
		Cooldown cooldown = cooldowns.get(info);
		if(cooldown == null) {
			return false;
		} else if (System.currentTimeMillis() - cooldown.getCooldown() >= cooldown.getChecked()) {
			return false;
		} else {
			return true;
		}
	}

	public abstract AbilityInfo getCurrentBind();
	
	public AbilityInfo[] getBinds() {
		return binds;
	}

	public Set<Skill> getSkills() {
		return skills;
	}
	
	public boolean copyBinds(BendingUser other) {
		for(AbilityInfo info : binds) {
			if(!other.canBend(info)) {
				return false;
			}
		}
		other.setBinds(Arrays.copyOf(binds, 9));	
		return true;
	}

	public void addSkills(Skill... skill) {
		skills.addAll(Arrays.asList(skill));
	}
	
	public Entity getEntity() {
		return entity;
	}
	
	protected List<Pair<AbilityInfo, Activation>> getActivations() {
		return activations;
	}
	
	protected List<Ability> getInstances() {
		return instances;
	}
	
	private void setBinds(AbilityInfo[] arr) {
		this.binds = arr;
	}
	
	private class Cooldown {
		private long checked;
		private long cooldown;
		
		public Cooldown(long cooldown) {
			this.cooldown = cooldown;
		}
		
		public void addCooldown() {
			this.checked = System.currentTimeMillis();
		}
		
		public long getCooldown() {
			return this.cooldown;
		}
		
		public long getChecked() {
			return this.checked;
		}
	}
}
