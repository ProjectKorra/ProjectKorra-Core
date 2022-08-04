package com.projectkorra.core.game.firebending.flamebroil;

import org.bukkit.inventory.ItemStack;

import com.projectkorra.core.ability.Ability;
import com.projectkorra.core.ability.AbilityUser;
import com.projectkorra.core.ability.attribute.Attribute;
import com.projectkorra.core.game.firebending.FireAbilityInstance;

public class FlameBroilInstance extends FireAbilityInstance {

	@Attribute("cooktime")
	private long cooktime;
	
	private double staminaDrain;
	private ItemStack held;

	public FlameBroilInstance(Ability provider, AbilityUser user) {
		super(provider, user);
		// this.cooktime = provider.cooktime;
		// this.staminaDrain = provider.staminaDrain;
	}

	@Override
	protected boolean onStart() {
		return true;
	}

	@Override
	protected boolean onUpdate(double timeDelta) {
		if (!user.getStamina().consume(timeDelta * staminaDrain)) {
			return false;
		}

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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void preUpdate() {
		// TODO Auto-generated method stub

	}

}
