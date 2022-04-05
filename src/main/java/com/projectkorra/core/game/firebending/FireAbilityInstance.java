package com.projectkorra.core.game.firebending;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;

import com.projectkorra.core.ability.Ability;
import com.projectkorra.core.ability.AbilityInstance;
import com.projectkorra.core.ability.AbilityUser;
import com.projectkorra.core.skill.Skill;

public abstract class FireAbilityInstance extends AbilityInstance {

	public FireAbilityInstance(Ability provider, AbilityUser user) {
		super(provider, user);
	}
	
	public final void particles(Location loc, int amount, double xOffset, double yOffset, double zOffset) {
		loc.getWorld().spawnParticle(getParticle(), loc, amount, xOffset, yOffset, zOffset, 0.025);
	}
	
	public final void particles(World world, double x, double y, double z, int amount, double xOffset, double yOffset, double zOffset) {
		world.spawnParticle(getParticle(), x, y, z, amount, xOffset, yOffset, zOffset, 0.025);
	}
	
	public final Particle getParticle() {
		return user.hasSkill(Skill.BLUEFIREBENDING) ? Particle.SOUL_FIRE_FLAME : Particle.FLAME;
	}
	
	public final Material getFireType() {
		return user.hasSkill(Skill.BLUEFIREBENDING) ? Material.SOUL_FIRE : Material.FIRE;
	}
}
