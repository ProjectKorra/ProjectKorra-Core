package com.projectkorra.core.game.firebending;

import java.util.function.Function;
import java.util.function.Predicate;

import org.bukkit.Particle;
import org.bukkit.Sound;

import com.projectkorra.core.ability.AbilityUser;
import com.projectkorra.core.skill.Skill;
import com.projectkorra.core.util.data.Pair;
import com.projectkorra.core.util.data.PairSet;
import com.projectkorra.core.util.effect.ParticleData;
import com.projectkorra.core.util.effect.SoundData;
import com.projectkorra.core.util.text.DisplayVariant;

import net.md_5.bungee.api.ChatColor;

public class Firebending extends Skill {
	
	private static PairSet<Predicate<AbilityUser>, Function<AbilityUser, ParticleData>> PARTICLE_OVERRIDE = new PairSet<>();
	private static PairSet<Predicate<AbilityUser>, Function<AbilityUser, SoundData>> SOUND_OVERRIDE = new PairSet<>();

	protected Firebending() {
		super("Firebending", new DisplayVariant("firebending", "firebend", "firebender", ChatColor.of("#a10000")), "Being able to create and control fire");
	}

	@Override
	public void postProcessed() {
		
	}

	@Override
	public ParticleData getParticle(AbilityUser user) {
		for (Pair<Predicate<AbilityUser>, Function<AbilityUser, ParticleData>> entry : PARTICLE_OVERRIDE) {
			if (entry.getLeft().test(user)) {
				return entry.getRight().apply(user);
			}
		}
		
		return new ParticleData(Particle.FLAME).extra(0.025);
	}

	@Override
	public SoundData getSound(AbilityUser user) {
		for (Pair<Predicate<AbilityUser>, Function<AbilityUser, SoundData>> entry : SOUND_OVERRIDE) {
			if (entry.getLeft().test(user)) {
				return entry.getRight().apply(user);
			}
		}
		
		return new SoundData(Sound.BLOCK_FIRE_AMBIENT);
	}

	public static void addParticleOverride(Predicate<AbilityUser> condition, Function<AbilityUser, ParticleData> override) {
		PARTICLE_OVERRIDE.add(condition, override);
	}
	
	public static void addSoundOverride(Predicate<AbilityUser> condition, Function<AbilityUser, SoundData> override) {
		SOUND_OVERRIDE.add(condition, override);
	}
	
	public static void addOverride(Skill skill, boolean particle, boolean sound) {
		if (particle) {
			PARTICLE_OVERRIDE.add((u) -> u.hasSkill(skill), (u) -> skill.getParticle(u));
		}
		
		if (sound) {
			SOUND_OVERRIDE.add((u) -> u.hasSkill(skill), (u) -> skill.getSound(u));
		}
	}
}
