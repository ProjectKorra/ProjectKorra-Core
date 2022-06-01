package com.projectkorra.core.game.firebending.passives.sunlight;

import org.bukkit.World;

import com.projectkorra.core.ability.AbilityInstance;
import com.projectkorra.core.ability.AbilityManager;
import com.projectkorra.core.ability.AbilityUser;
import com.projectkorra.core.ability.attribute.AttributeGroup;
import com.projectkorra.core.ability.attribute.Modifier;

public class SunlightInstance extends AbilityInstance {

	private double modDamage, modRange, modSpeed, modSize;

	public SunlightInstance(SunlightPassive provider, AbilityUser user) {
		super(provider, user);
		this.modDamage = provider.modDamage;
		this.modRange = provider.modRange;
		this.modSpeed = provider.modSpeed;
		this.modSize = provider.modSize;
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
		return false;
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

	void applyBuffs(AbilityInstance instance) {
		World world = instance.getUser().getLocation().getWorld();

		if (world.getTime() > 13500 && world.getTime() < 22500) {
			return;
		}

		AbilityManager.addModifier(instance, AttributeGroup.DAMAGE, Modifier.multiply(modDamage));
		AbilityManager.addModifier(instance, AttributeGroup.RANGE, Modifier.multiply(modRange));
		AbilityManager.addModifier(instance, AttributeGroup.SPEED, Modifier.multiply(modSpeed));
		AbilityManager.addModifier(instance, AttributeGroup.SIZE, Modifier.multiply(modSize));
	}

	@Override
	protected void preUpdate() {
		// TODO Auto-generated method stub

	}
}
