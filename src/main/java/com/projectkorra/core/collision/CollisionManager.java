package com.projectkorra.core.collision;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.Validate;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.BoundingBox;

import com.projectkorra.core.ProjectKorra;
import com.projectkorra.core.event.BendingCollisionEvent;
import com.projectkorra.core.util.CollisionUtil;
import com.projectkorra.core.util.data.MutablePair;
import com.projectkorra.core.util.data.Pair;
import com.projectkorra.core.util.data.Pairing;

public class CollisionManager {
	
	private static final ProjectKorra PLUGIN = JavaPlugin.getPlugin(ProjectKorra.class);
	
	private static final int BOUNDING_MIN = -29999984 / 2;
	private static final int BOUNDING_MAX = 29999984 / 2;
	private static final CollisionFile COLLISIONS = new CollisionFile(new File(PLUGIN.getDataFolder(), "collisions.txt"));

	private CollisionTree tree;
	private Set<Collidable> instances, removal;
	private Set<Pair<Collidable, Collidable>> collided;
	private Map<Pair<String, String>, CollisionData> valids;
	
	public CollisionManager() {
		tree = new CollisionTree(new BoundingBox(BOUNDING_MIN, BOUNDING_MIN, BOUNDING_MIN, BOUNDING_MAX, BOUNDING_MAX, BOUNDING_MAX), 5); //create a tree with the max world size
		instances = new HashSet<>();
		collided = new HashSet<>();
		valids = new HashMap<>();
		
		COLLISIONS.readAnd((cd) -> valids.put(Pairing.of(cd.getFirst().toLowerCase(), cd.getSecond().toLowerCase()), cd));
	}
	
	public void tick() {
		MutablePair<Collidable, Collidable> pair = Pairing.ofMutable(null, null);
		for (Collidable obj : instances) {
			Set<Collidable> found = tree.query(obj.getBoundary());
			found.remove(obj);
			pair.setLeft(obj);
			
			for (Collidable other : found) {
				pair.setRight(other);
				if (collided.contains(pair)) {
					continue;
				}
				
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
					
					BendingCollisionEvent event = new BendingCollisionEvent(first, second, data.getOperator());
					PLUGIN.getServer().getPluginManager().callEvent(event);
					
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
