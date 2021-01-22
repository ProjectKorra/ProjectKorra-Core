package com.projectkorra.core.collision;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.util.BoundingBox;

public class CollisionTree {
	
	private static final int[] XS = { 1, 1, 1, 1, 0, 0, 0, 0 };
	private static final int[] YS = { 1, 1, 0, 0, 1, 1, 0, 0 };
	private static final int[] ZS = { 1, 0, 1, 0, 1, 0, 1, 0 };

	private BoundingBox bounds;
	private int capacity;
	private Set<Collidable> contents;
	private CollisionTree[] children;
	
	public CollisionTree(BoundingBox bounds, int capacity) {
		this.bounds = bounds;
		this.capacity = capacity;
		this.contents = new HashSet<Collidable>(capacity);
		this.children = null;
	}
	
	private void divide() {
		if (children == null) {
			children = new CollisionTree[8];
			for (int i = 0; i < 8; i++) {
				children[i] = new CollisionTree(new BoundingBox(bounds.getMinX() + XS[i] * bounds.getWidthX() / 2, bounds.getMinY() + YS[i] * bounds.getHeight() / 2, bounds.getMinZ() + ZS[i] * bounds.getWidthZ() / 2, bounds.getMaxX() - XS[7 - i] * bounds.getWidthX() / 2, bounds.getMaxY() - YS[7 - i] * bounds.getHeight() / 2, bounds.getMaxZ() - ZS[7 - i] * bounds.getWidthZ() / 2), capacity);
			}
			
			for (Collidable obj : contents) {
				insert(obj);
			}
			
			contents.clear();
		}
	}
	
	void reset() {
		this.contents.clear();
		this.children = null;
	}
	
	public boolean insert(Collidable obj) {
		if (!bounds.contains(obj.getLocation().getX(), obj.getLocation().getY(), obj.getLocation().getZ())) {
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
	
	public Set<Collidable> query(BoundingBox range) {
		Set<Collidable> found = new HashSet<>();
		
		if (bounds.overlaps(range)) {
			return found;
		}
		
		if (children != null) {
			for (CollisionTree branch : children) {
				found.addAll(branch.query(range));
			}
		} else {
			for (Collidable obj : contents) {
				if (range.overlaps(obj.getHitbox())) {
					found.add(obj);
				}
			}
		}
		
		return found;
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
