package com.projectkorra.core;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import com.projectkorra.core.system.ability.Ability;
import com.projectkorra.core.system.ability.AbilityInstance;
import com.projectkorra.core.system.ability.AbilityUser;
import com.projectkorra.core.system.ability.modifier.Modifier;
import com.projectkorra.core.system.ability.modifier.Modifier.Modifiable;
import com.projectkorra.core.system.skill.Skill;
import com.projectkorra.core.util.configuration.Configurable;

public final class AbilityManager {

	private static final Map<Class<? extends Ability>, Ability> ABILITIES_BY_CLASS = new HashMap<>(256);
	private static final Map<String, Ability> ABILITIES_BY_NAME = new HashMap<>(256);
	private static final Map<Skill, Set<Ability>> ABILITIES_BY_SKILL = new HashMap<>();
	private static final Map<AbilityUser, Map<Class<? extends AbilityInstance>, Set<AbilityInstance>>> INSTANCES = new HashMap<>();
	private static final Map<AbilityInstance, Queue<Modifier<?>>> INSTANCE_MODIFIERS = new HashMap<>();
	private static final Set<AbilityInstance> ACTIVE = new HashSet<>(256);
	
	//overriding abilities with the same name is a nono
	public static boolean register(Ability ability) {
		if (ABILITIES_BY_NAME.containsKey(ability.getName())) {
			return false;
		}
		
		ABILITIES_BY_CLASS.put(ability.getClass(), ability);
		ABILITIES_BY_NAME.put(ability.getName(), ability);
		
		for (Skill skill : ability.getSkills()) {
			if (!ABILITIES_BY_SKILL.containsKey(skill)) {
				ABILITIES_BY_SKILL.put(skill, new HashSet<>());
			}
			ABILITIES_BY_SKILL.get(skill).add(ability);
		}
		
		//do config things depending on how configs will work
		for (Field field : ability.getClass().getDeclaredFields()) {
			if (field.isAnnotationPresent(Configurable.class)) {
				
			}
		}
		
		return true;
	}
	
	public static void track(AbilityUser activator, Ability ability, AbilityInstance instance) {
		//AbilityStartEvent
		
		if (!INSTANCES.containsKey(activator)) {
			INSTANCES.put(activator, new HashMap<>());
		}
		
		if (!INSTANCES.get(activator).containsKey(instance.getClass())) {
			INSTANCES.get(activator).put(instance.getClass(), new HashSet<>());
		}
		
		//set values to be the configured ones
		for (Field field : instance.getClass().getDeclaredFields()) {
			if (field.isAnnotationPresent(Modifiable.class)) {
				boolean access = field.isAccessible();
				field.setAccessible(true);
				try {
					field.set(instance, ability.getClass().getDeclaredField(field.getName()).get(ability));
				} catch (Exception e) {
					e.printStackTrace();
				}
				field.setAccessible(access);
			}
		}
		
		//throw in any modifications that have been added
		for (Modifier<?> mod : INSTANCE_MODIFIERS.get(instance)) {
			try {
				Field field = instance.getClass().getDeclaredField(mod.getField());
				field.set(instance, mod.apply(field.get(instance)));
			} catch (Exception e) {
				continue;
			}
		}
		
		INSTANCES.get(activator).get(instance.getClass()).add(instance);
		ACTIVE.add(instance);
	}
	
	static void tick() {
		Iterator<AbilityInstance> iter = ACTIVE.iterator();
		while (iter.hasNext()) {
			AbilityInstance instance = iter.next();
			if (instance.shouldRemove()) {
				iter.remove();
				continue;
			}
			instance.update();
		}
	}
}
