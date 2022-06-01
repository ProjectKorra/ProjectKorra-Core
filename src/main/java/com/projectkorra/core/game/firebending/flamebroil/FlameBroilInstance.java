package com.projectkorra.core.game.firebending.flamebroil;

import org.bukkit.inventory.ItemStack;

import com.projectkorra.core.ability.Ability;
import com.projectkorra.core.ability.AbilityUser;
import com.projectkorra.core.ability.attribute.Attribute;
import com.projectkorra.core.ability.attribute.AttributeGroup;
import com.projectkorra.core.game.firebending.FireAbilityInstance;

public class FlameBroilInstance extends FireAbilityInstance {

	@Attribute(value = "cooktime", group = AttributeGroup.CHARGE_TIME)
	private long cooktime;
	@Attribute(value = Attribute.STAMINA_DRAIN, group = AttributeGroup.STAMINA)
	private double staminaDrain;

	private ItemStack held;

	public FlameBroilInstance(Ability provider, AbilityUser user) {
		super(provider, user);
		// this.cooktime = provider.cooktime;
		// this.staminaDrain = provider.staminaDrain;
	}

	@Override
	protected void onStart() {

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
