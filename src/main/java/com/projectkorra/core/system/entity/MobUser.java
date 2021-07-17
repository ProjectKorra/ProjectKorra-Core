package com.projectkorra.core.system.entity;

import java.util.Collection;

import org.bukkit.Location;
import org.bukkit.entity.Mob;
import org.bukkit.plugin.java.JavaPlugin;

import com.projectkorra.core.ProjectKorra;
import com.projectkorra.core.system.ability.AbilityBinds;
import com.projectkorra.core.system.skill.Skill;

public class MobUser extends User<Mob> {
	
	private MobSlotAI ai;

	MobUser(Mob entity, Collection<Skill> skills, Collection<Skill> toggled, AbilityBinds binds, MobSlotAI ai) {
		super(entity, skills, toggled, binds);
		this.ai = ai;
	}

	@Override
	public boolean shouldRemove() {
		return entity.isDead();
	}

	@Override
	public int getCurrentSlot() {
		return ai.slot();
	}

	@Override
	public void sendMessage(String message) {
		// add config option to silence such messages or log them somewhere else
		JavaPlugin.getPlugin(ProjectKorra.class).getLogger().info("Message sent to SkilledMob (" + entity.getUniqueId() + ", " + entity.getCustomName() + "): `" + message + "`");
	}

	@Override
	public boolean hasPermission(String perm) {
		return entity.hasPermission(perm);
	}

	@Override
	public boolean checkDefaultProtections(Location loc) {
		return false;
	}

}