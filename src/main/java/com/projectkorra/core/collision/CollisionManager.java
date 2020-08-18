package com.projectkorra.core.collision;

import java.util.Set;

import org.bukkit.Location;

public class CollisionManager {

	private CollisionTree tree;
	private Set<Collidable> instances;
	
	public CollisionManager() {
		tree = new CollisionTree(new Location(null, 0, 0, 0), 29999984, 29999984, 29999984, 5); //create a tree with the max world size
	}
	
	public void tick() {
		for (Collidable obj : instances) {
			Set<Collidable> found = tree.query(obj.getBoundary());
			found.remove(obj);
			
			for (Collidable other : found) {
				//TODO: add check if a valid collision exists between obj and other
				obj.onCollision(other);
			}
		}
		tree.reset();
		instances.clear();
	}

	//call this after the progress method for Collidable abilities
	public void addCollidable(Collidable obj) {
		if (tree.insert(obj)) {
			instances.add(obj);
		}
	}
}
