package com.projectkorra.core.collision;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.Validate;
import org.bukkit.util.BoundingBox;

import com.projectkorra.core.ProjectKorra;
import com.projectkorra.core.event.BendingCollisionEvent;
import com.projectkorra.core.util.CollisionUtil;
import com.projectkorra.core.util.data.Pair;

public class CollisionManager {
	
	private static final int BOUNDING_MIN = -29999984 / 2;
	private static final int BOUNDING_MAX = 29999984 / 2;
	private static final CollisionFile COLLISIONS = new CollisionFile(new File(ProjectKorra.plugin().getDataFolder(), "collisions.txt"));

	private CollisionTree tree;
	private Set<Collidable> instances, removal;
	private Set<Pair<Collidable, Collidable>> collided;
	private Map<Pair<String, String>, CollisionData> valids;
	
	public CollisionManager() {
		tree = new CollisionTree(new BoundingBox(BOUNDING_MIN, BOUNDING_MIN, BOUNDING_MIN, BOUNDING_MAX, BOUNDING_MAX, BOUNDING_MAX), 5); //create a tree with the max world size
		instances = new HashSet<>();
		collided = new HashSet<>();
		valids = new HashMap<>();
		
		COLLISIONS.readAnd((cd) -> valids.put(Pair.of(cd.getFirst().toLowerCase(), cd.getSecond().toLowerCase()), cd));
	}
	
	public void tick() {
		for (Collidable obj : instances) {
			Set<Collidable> found = tree.query(obj.getBoundary());
			found.remove(obj);
			
			for (Collidable other : found) {
				Pair<Collidable, Collidable> pair = Pair.of(obj, other);
				if (collided.contains(pair)) {
					continue;
				}
				//TODO: add check if a valid collision exists between obj and other
				if (doesValidCollisionExist(obj, other)) {
					CollisionData data = valids.get(CollisionUtil.pairTags(obj, other));
					Collidable first, second;
					
					if (obj.getTag().equalsIgnoreCase(data.getFirst())) {
						first = obj;
						second = other;
					} else {
						first = other;
						second = obj;
					}
					
					BendingCollisionEvent event = ProjectKorra.callEvent(new BendingCollisionEvent(first, second, data.getOperator()));
					
					if (event.isCancelled()) {
						continue;
					}
					
					if (event.isFirstBeingRemoved()) {
						removal.add(first);
					}
					
					if (event.isSecondBeingRemoved()) {
						removal.add(second);
					}
					
					collided.add(pair);
				}
			}
		}
		
		//do removal things with removal set
		
		tree.reset();
		instances.clear();
		collided.clear();
		removal.clear();
	}

	//call this after the progress method for Collidable abilities
	public void addCollidable(Collidable obj) {
		if (tree.insert(obj)) {
			instances.add(obj);
		}
	}
	
	public void reload() {
		tree.reset();
		instances.clear();
	}
	
	/**
	 * Checks for a valid collision between two collidables
	 * @param first a collidable
	 * @param second another collidable
	 * @return true if valid collision found
	 */
	public boolean doesValidCollisionExist(Collidable first, Collidable second) {
		Validate.isTrue(first != null, "First param cannot be null in a collision");
		Validate.isTrue(second != null, "Second param cannot be null in a collision");
		return valids.containsKey(CollisionUtil.pairTags(first, second));
	}
	
	/**
	 * Adds a valid collision between two collidables but will not overwrite
	 * any existing valid collision between them
	 * @param first lefthand collidable 
	 * @param second righthand collidable
	 * @param op interaction between the two collidables
	 * @return false if valid collision exists
	 */
	public boolean addValidCollision(Collidable first, Collidable second, CollisionOperator op) {
		Validate.isTrue(first != null && first.getTag() != null && !first.getTag().isEmpty(), "First param must be some text");
		Validate.isTrue(second != null && second.getTag() != null && !second.getTag().isEmpty(), "Second param must be some text");
		
		if (doesValidCollisionExist(first, second)) {
			return false;
		}
		
		CollisionData data = new CollisionData(first.getTag().toLowerCase(), second.getTag().toLowerCase(), op, null);
		
		valids.put(CollisionUtil.pairTags(first, second), data);
		COLLISIONS.write(data);
		return true;
	}
}
