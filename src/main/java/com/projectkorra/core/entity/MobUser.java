package com.projectkorra.core.entity;

import com.projectkorra.core.ProjectKorra;

import org.bukkit.Location;
import org.bukkit.entity.Mob;
import org.bukkit.plugin.java.JavaPlugin;

public class MobUser extends User<Mob> {
	
	private MobSlotAI ai;

	MobUser(Mob entity, MobSlotAI ai) {
		super(entity);
		this.ai = ai;
	}

	@Override
	public boolean shouldRemove() {
		return typed.isDead();
	}

	@Override
	public int getCurrentSlot() {
		return ai.slot();
	}

	@Override
	public void sendMessage(String message) {
		// add config option to silence such messages or log them somewhere else
		JavaPlugin.getPlugin(ProjectKorra.class).getLogger().info("Message sent to SkilledMob (" + typed.getUniqueId() + ", " + typed.getCustomName() + "): `" + message + "`");
	}

	@Override
	public boolean hasPermission(String perm) {
		return typed.hasPermission(perm);
	}

	@Override
	public boolean checkDefaultProtections(Location loc) {
		return false;
	}

}
