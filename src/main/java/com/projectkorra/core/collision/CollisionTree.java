package com.projectkorra.core.collision;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Location;

public class CollisionTree {
	
	private static final int[] XS = { 1, 1, 1, 1, -1, -1, -1, -1 };
	private static final int[] YS = { 1, 1, -1, -1, 1, 1, -1, -1 };
	private static final int[] ZS = { 1, -1, 1, -1, 1, -1, 1, -1 };

	private Boundary bounds;
	private int capacity;
	private Set<Collidable> contents;
	private CollisionTree[] children;
	
	public CollisionTree(Location center, double length, double height, double width, int capacity) {
		this.bounds = new Boundary(center, length, height, width);
		this.capacity = capacity;
		this.contents = new HashSet<Collidable>(capacity);
		this.children = null;
	}
	
	private void divide() {
		if (children == null) {
			children = new CollisionTree[8];
			for (int i = 0; i < 8; i++) {
				children[i] = new CollisionTree(bounds.getCenter().clone().add(XS[i] * bounds.getLength() / 4, YS[i] * bounds.getHeight() / 4, ZS[i] * bounds.getWidth() / 4), bounds.getLength() / 2, bounds.getHeight() / 2, bounds.getWidth() / 2, capacity);
			}
			
			for (Collidable obj : contents) {
				insert(obj);
			}
			
			contents.clear();
		}
	}
	
	public boolean insert(Collidable obj) {
		if (!bounds.contains(obj.getLocation())) {
			return false;
		}
		
		if (children != null) {
			boolean inserted = false;
			int i = 0;
			
			while (!inserted && i < children.length) {
				inserted = children[i++].insert(obj);
			}
			
			return inserted;
		}
		
		if (contents.size() + 1 > capacity) {
			divide();
			return insert(obj);
		} else {
			return contents.add(obj);
		}
	}
	
	public Set<Collidable> query(Boundary range) {
		Set<Collidable> found = new HashSet<>();
		
		if (bounds.intersects(range)) {
			return found;
		}
		
		if (children != null) {
			for (CollisionTree branch : children) {
				found.addAll(branch.query(range));
			}
		} else {
			for (Collidable obj : contents) {
				if (range.contains(obj.getLocation())) {
					found.add(obj);
				}
			}
		}
		
		return found;
	}
	
	public void reset() {
		this.contents.clear();
		this.children = null;
	}
	
	public Set<Collidable> getContents() {
		Set<Collidable> all = new HashSet<>();
		
		if (children != null) {
			for (CollisionTree branch : children) {
				all.addAll(branch.getContents());
			}
		} else {
			all.addAll(contents);
		}
		
		return all;
	}
}
