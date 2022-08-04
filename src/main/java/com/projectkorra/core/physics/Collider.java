package com.projectkorra.core.physics;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

import com.projectkorra.core.util.Vectors;
import com.projectkorra.core.util.data.Pair;
import com.projectkorra.core.util.data.Pairing;

public final class Collider {

	private Location previous;
	private Set<BoundingBox> boxes = new HashSet<>();

	public Collider(Location center) {
		this.previous = center.clone();
	}

	public Location getLocation() {
		return previous.clone();
	}

	public void add(BoundingBox box) {
		this.boxes.add(box);
	}

	public void clear() {
		this.boxes.clear();
	}

	public void reset(Collection<BoundingBox> boxes) {
		this.boxes.clear();
		this.boxes.addAll(boxes);
	}

	public void set(Collection<BoundingBox> boxes) {
		this.boxes.addAll(boxes);
	}

	public void shift(Location center) {
		Vector to = Vectors.direction(previous, center);
		this.boxes.forEach((b) -> b.shift(to));
		this.previous = center.clone();
	}

	public Set<Pair<BoundingBox, BoundingBox>> intersections(Collider other) {
		if (this == other) {
			return new HashSet<>();
		}

		Set<Pair<BoundingBox, BoundingBox>> inters = new HashSet<>();
		for (BoundingBox box : boxes) {
			for (BoundingBox otherBox : other.boxes) {
				if (box.overlaps(otherBox)) {
					inters.add(Pairing.of(box, otherBox));
				}
			}
		}

		return inters;
	}

	public boolean overlaps(BoundingBox aabb) {
		for (BoundingBox box : boxes) {
			if (box.overlaps(aabb)) {
				return true;
			}
		}

		return false;
	}

	public double getVolume() {
		double volume = 0;
		for (BoundingBox box : boxes) {
			volume += box.getVolume();
		}

		return volume;
	}

	public double getCenterX() {
		return previous.getX();
	}

	public double getCenterY() {
		return previous.getY();
	}

	public double getCenterZ() {
		return previous.getZ();
	}
}
