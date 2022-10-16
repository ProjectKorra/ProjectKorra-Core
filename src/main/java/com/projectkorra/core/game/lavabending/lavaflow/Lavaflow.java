package com.projectkorra.core.game.lavabending.lavaflow;

import org.bukkit.event.Event;

import com.projectkorra.core.ability.Ability;
import com.projectkorra.core.ability.AbilityInstance;
import com.projectkorra.core.ability.AbilityManager;
import com.projectkorra.core.ability.AbilityUser;
import com.projectkorra.core.ability.activation.Activation;
import com.projectkorra.core.ability.attribute.AttributeGroup;
import com.projectkorra.core.ability.type.Bindable;
import com.projectkorra.core.skill.Skill;
import com.projectkorra.core.util.configuration.Configure;

public class Lavaflow extends Ability implements Bindable {
	
	@Configure double sourceRange = 5;
	@Configure double sourceRadius = 1.3;
	@Configure long createTime = 700;
	@Configure double speed = 10;
	@Configure double staminaDrain = 0.1;
	@Configure long cooldown = 2000;
	@Configure long coolTime = 30000;

	public Lavaflow() {
		super("Lavaflow", "Turn the earth into lava", "ProjectKorra", "CORE", Skill.of("lavabending"));
	}
	
	@Override
	public void postProcessed() {
		AttributeGroup.CHARGE_TIME.add("lava_create_time");
		AttributeGroup.DURATION.add("lava_cool_time");
		AttributeGroup.SPEED.add("flow_speed");
	}

	@Override
	public String getInstructions() {
		return "Hold sneak while looking at the earth to turn it into lava";
	}

	@Override
	protected AbilityInstance activate(AbilityUser user, Activation trigger, Event provider) {
		if (user.hasCooldown(this)) {
			return null;
		}
		
		if (trigger == Activation.SNEAK_DOWN) {
			return new LavaflowInstance(this, user);
		} else if (trigger == Activation.SNEAK_UP) {
			user.getInstance(LavaflowInstance.class).ifPresent(AbilityManager::remove);
		}
		
		return null;
	}

}
