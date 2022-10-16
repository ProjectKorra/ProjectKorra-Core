package com.projectkorra.core.util.effect;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;

public class ParticleData implements Spawnable {

	private Particle particle;
	private double ox = 0.0, oy = 0.0, oz = 0.0, extra = 0.0;
	private int amount = 1;
	private Object data = null;
	
	public ParticleData(Particle particle) {
		this.particle = particle;
	}
	
	@Override
	public void spawn(World world, double x, double y, double z) {
		world.spawnParticle(particle, ox, oy, oz, amount, ox, oy, oz, extra, data);
	}
	
	@Override
	public void spawn(Location loc) {
		loc.getWorld().spawnParticle(particle, loc.getX(), loc.getY(), loc.getZ(), amount, oy, ox, amount, extra, data);
	}

	public Particle getParticle() {
		return particle;
	}

	public void setParticle(Particle particle) {
		this.particle = particle;
	}
	
	public ParticleData offsets(double d) {
		return this.offsetX(d).offsetY(d).offsetZ(d);
	}

	public double offsetX() {
		return ox;
	}

	public ParticleData offsetX(double ox) {
		this.ox = ox;
		return this;
	}

	public double offsetY() {
		return oy;
	}

	public ParticleData offsetY(double oy) {
		this.oy = oy;
		return this;
	}

	public double offsetZ() {
		return oz;
	}

	public ParticleData offsetZ(double oz) {
		this.oz = oz;
		return this;
	}

	public double extra() {
		return extra;
	}

	public ParticleData extra(double extra) {
		this.extra = extra;
		return this;
	}

	public int amount() {
		return amount;
	}

	public ParticleData amount(int amount) {
		this.amount = amount;
		return this;
	}

	public Object data() {
		return data;
	}

	public ParticleData data(Object data) {
		this.data = data;
		return this;
	}
	
	public static ParticleData builder(Particle particle) {
		return new ParticleData(particle);
	}
}
