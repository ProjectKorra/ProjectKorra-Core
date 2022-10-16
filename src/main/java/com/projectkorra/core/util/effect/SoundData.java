package com.projectkorra.core.util.effect;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;

public class SoundData implements Spawnable {

	private Sound sound;
	private float volume = 0.5f, pitch = 0.5f;
	
	public SoundData(Sound sound) {
		this.sound = sound;
	}
	
	@Override
	public void spawn(Location loc) {
		loc.getWorld().playSound(loc, sound, volume, pitch);
	}
	
	@Override
	public void spawn(World world, double x, double y, double z) {
		world.playSound(new Location(world, x, y, z), sound, volume, pitch);
	}
	
	public Sound sound() {
		return sound;
	}
	
	public SoundData sound(Sound sound) {
		this.sound = sound;
		return this;
	}
	
	public float volume() {
		return volume;
	}
	
	public SoundData volume(float volume) {
		this.volume = Math.max(0, volume);
		return this;
	}
	
	public float pitch() {
		return pitch;
	}
	
	public SoundData pitch(float pitch) {
		this.pitch = pitch;
		return this;
	}
	
	public static SoundData builder(Sound sound) {
		return new SoundData(sound);
	}
}
