package com.projectkorra.core.game.physique.landing;

import org.bukkit.event.entity.EntityDamageEvent;

import com.projectkorra.core.ability.Ability;
import com.projectkorra.core.ability.AbilityInstance;
import com.projectkorra.core.ability.AbilityUser;
import com.projectkorra.core.ability.attribute.Attribute;
import com.projectkorra.core.ability.attribute.AttributeGroup;

public class LandingInstance extends AbilityInstance {

	@Attribute(value = "damage_reduction", group = AttributeGroup.RESISTANCE)
	private double reduction;

	public LandingInstance(Ability provider, AbilityUser user, double reduction) {
		super(provider, user);
		this.reduction = reduction;
	}

	@Override
	public boolean hasUpdate() {
		return false;
	}

	@Override
	protected void onStart() {
	}

	@Override
	protected boolean onUpdate(double timeDelta) {
		return true;
	}

	@Override
	protected void postUpdate() {
	}

	@Override
	protected void onStop() {
	}

	@Override
	public String getName() {
		return provider.getName();
	}

	public void reduceDamage(EntityDamageEvent event) {
		if (event.getDamage() - reduction <= 0) {
			event.setCancelled(true);
		} else {
			event.setDamage(event.getDamage() - reduction);
		}
	}

	@Override
	protected void preUpdate() {
		// TODO Auto-generated method stub

	}
}
