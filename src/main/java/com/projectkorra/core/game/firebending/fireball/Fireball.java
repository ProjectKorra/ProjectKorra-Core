package com.projectkorra.core.game.firebending.fireball;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.bukkit.event.Event;

import com.projectkorra.core.ability.Ability;
import com.projectkorra.core.ability.AbilityInstance;
import com.projectkorra.core.ability.AbilityUser;
import com.projectkorra.core.ability.activation.Activation;
import com.projectkorra.core.ability.activation.SequenceInfo;
import com.projectkorra.core.ability.type.Bindable;
import com.projectkorra.core.ability.type.Combo;
import com.projectkorra.core.skill.Skill;
import com.projectkorra.core.util.configuration.Configure;

public class Fireball extends Ability implements Bindable, Combo {

	@Configure("blast.range")
	double blastRange = 20;
	@Configure("blast.speed")
	double blastSpeed = 30;
	@Configure("blast.damage")
	double blastDamage = 3;
	@Configure("blast.cooldown")
	long blastCooldown = 1500;
	@Configure("blast.staminaCost")
	double staminaCost = 0.1;
	@Configure("charged.range.max")
	double chargedMaxRange = 40;
	@Configure("charged.range.min")
	double chargedMinRange = 25;
	@Configure("charged.speed.max")
	double chargedMaxSpeed = 25;
	@Configure("charged.speed.min")
	double chargedMinSpeed = 20;
	@Configure("charged.damage.max")
	double chargedMaxDamage = 7;
	@Configure("charged.damage.min")
	double chargedMinDamage = 4;
	@Configure("charged.cooldown")
	long chargedCooldown = 3500;
	@Configure("charged.knockback")
	double chargedKnockback = 20;
	@Configure("charged.staminaCost")
	double chargedStaminaCost = 0.2;
	@Configure("charged.chargeTime.max")
	long chargedTime = 2000;
	@Configure("charged.chargeTime.min")
	long chargedMinTime = 500;
	@Configure("combo.speed")
	double comboSpeed = 18;
	@Configure("combo.damage")
	double comboDamage = 4;
	@Configure("combo.cooldown")
	long comboCooldown = 3000;

	public Fireball() {
		super("Fireball", "Throw fireballs!", "ProjectKorra", "CORE", Skill.of("firebending"));
	}

	@Override
	public void postProcessed() {
	}

	@Override
	protected AbilityInstance activate(AbilityUser user, Activation trigger, Event provider) {
		if (user.hasCooldown("ChargedFireball")) {
			return null;
		}

		if (trigger == Activation.SNEAK_DOWN && !user.hasCooldown(this)) {
			Optional<ChargedFireballInstance> cfb = user.getInstance(ChargedFireballInstance.class);
			if (cfb.isPresent()) {
				cfb.get().pressSneak();
				return null;
			}

			return new ChargedFireballInstance(this, user);
		}

		if (trigger == Activation.SNEAK_UP) {
			user.getInstance(ChargedFireballInstance.class).ifPresent(ChargedFireballInstance::releaseSneak);
		}

		if (trigger == Activation.LEFT_CLICK) {
			Optional<ChargedFireballInstance> cfb = user.getInstance(ChargedFireballInstance.class);
			
			if (cfb.isPresent()) {
				if (cfb.get().canShoot()) {
					cfb.get().shoot();
				}
				return null;
			} else if (user.hasCooldown(this) || !user.getStamina().consume(staminaCost)) {
				return null;
			}

			return new FireballInstance(this, user, false);
		}

		if (trigger == Activation.COMBO && user.hasCooldown(this) && !user.hasCooldown("FireballCombo") && user.getStamina().consume(staminaCost)) {
			user.addCooldown("FireballCombo", comboCooldown);
			return new FireballInstance(this, user, true);
		}

		return null;
	}

	@Override
	protected void onRegister() {
	}

	@Override
	public String getInstructions() {
		return "Left click for blast and the power of the fireball will be increased by the charge";
	}

	@Override
	public List<SequenceInfo> getSequence() {
		return Arrays.asList(SequenceInfo.of("Fireball", Activation.LEFT_CLICK), SequenceInfo.of("Fireball", Activation.SNEAK_DOWN), SequenceInfo.of("Fireball", Activation.LEFT_CLICK));
	}
}
