package com.projectkorra.core.api;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import com.projectkorra.core.api.activation.Activation;
import com.projectkorra.core.util.Pair;

public class PlayerUser extends User {
	private int slot;

	public PlayerUser(Player entity) {
		super(entity);
	}

	@Override
	public AbilityInfo getCurrentBind() {
		return null;
	}

	public void setSlot(int i) {
		this.slot = i;
	}

	public int getSlot() {
		return this.slot;
	}

	@Override
	protected final List<Pair<AbilityInfo, Activation>> getActivations(boolean current) {
		if (current) {
			List<Pair<AbilityInfo, Activation>> filtered = new ArrayList<>();
			for (Pair<AbilityInfo, Activation> e : activations) {
				if (e.getKey() == this.getCurrentBind() || !e.getKey().bindable) {
					filtered.add(e);
				}
			}
			return filtered;
		} else {
			return activations;
		}
	}

	@Override
	public List<AbilityInfo> getBinds() {
		// TODO Auto-generated method stub
		return null;
	}
}
