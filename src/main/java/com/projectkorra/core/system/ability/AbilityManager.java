package com.projectkorra.core.system.ability;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import com.projectkorra.core.ProjectKorra;
import com.projectkorra.core.system.ability.activation.Activation;
import com.projectkorra.core.system.ability.activation.SequenceInfo;
import com.projectkorra.core.system.ability.modifier.Modifier;
import com.projectkorra.core.system.ability.type.Combo;
import com.projectkorra.core.system.skill.Skill;
import com.projectkorra.core.util.configuration.Configurable;

public final class AbilityManager {
	
	private AbilityManager() {}

	private static final Map<Class<? extends Ability>, Ability> ABILITIES_BY_CLASS = new HashMap<>(256);
	private static final Map<String, Ability> ABILITIES_BY_NAME = new HashMap<>(256);
	private static final Map<Skill, Set<Ability>> ABILITIES_BY_SKILL = new HashMap<>();
	private static final Map<AbilityUser, Map<Class<? extends AbilityInstance>, Set<AbilityInstance>>> INSTANCES = new HashMap<>();
	private static final Map<AbilityInstance, Queue<Modifier<?>>> INSTANCE_MODIFIERS = new HashMap<>();
	private static final Set<AbilityInstance> ACTIVE = new HashSet<>(256);
	
	//combo management
	private static final ComboTree COMBO_ROOT = new ComboTree();
	private static final Map<List<SequenceInfo>, Ability> COMBOS = new HashMap<>();
	private static final Map<AbilityUser, LinkedList<ComboTree>> USER_SEQUENCES = new HashMap<>();
	
	private static boolean init = false;
	
	public static void init(ProjectKorra plugin) {
		if (init) {
			return;
		}
		
		init = true;
		plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, () -> tick(), 0, 1);
	}
	
	//overriding abilities with the same name is a nono
	public static void register(Ability ability) throws IllegalArgumentException {
		if (ABILITIES_BY_NAME.containsKey(ability.getName())) {
			throw new IllegalArgumentException("An Ability with that name already exists!");
		}
		
		ABILITIES_BY_CLASS.put(ability.getClass(), ability);
		ABILITIES_BY_NAME.put(ability.getName(), ability);
		
		for (Skill skill : ability.getSkills()) {
			if (!ABILITIES_BY_SKILL.containsKey(skill)) {
				ABILITIES_BY_SKILL.put(skill, new HashSet<>());
			}
			ABILITIES_BY_SKILL.get(skill).add(ability);
		}
		
		if (ability instanceof Combo) {
			List<SequenceInfo> sequence = ((Combo) ability).getSequence();
			COMBO_ROOT.build(sequence);
			COMBOS.put(sequence, ability);
		}
		
		//do config things depending on how configs will work
		for (Field field : ability.getClass().getDeclaredFields()) {
			if (field.isAnnotationPresent(Configurable.class)) {
				try {
					setField(field, ability, 0);
				} catch (Exception e) {
					continue;
				}
			}
		}
	}
	
	/**
	 * Calls activation for the given user, checking for a valid
	 * combo that they can activate with their bound ability and 
	 * the given activation type, or activates their bound ability
	 * if nonnull
	 * @param user who to activate for
	 * @param type how to activate
	 * @return false if null or event is canceled, otherwise passed to {@link #start(AbilityUser, AbilityInstance)}
	 */
	public static boolean activate(AbilityUser user, Activation type) {
		Ability ability = user.getBoundAbility();
		
		//can't activate a null ability
		if (user == null || type == null || ability == null) {
			return false;
		}
		
		//AbilityActivateEvent, return false if canceled
		
		//start a new agent at the root
		USER_SEQUENCES.computeIfAbsent(user, (a) -> new LinkedList<>()).add(COMBO_ROOT);
		
		//check through existing agents
		Iterator<ComboTree> iter = USER_SEQUENCES.get(user).iterator();
		while (iter.hasNext()) {
			ComboTree branch = iter.next().getBranch(ability, type);
			
			iter.remove(); //regardless of outcome, we don't want this branch anymore
			if (branch == null) {
				continue;
			} else if (branch.isEnd()) {
				//end of branches means that we can activate a combo from the sequence
				ability = COMBOS.get(branch.sequence());
				type = Activation.SEQUENCED;
			} else {
				//update agent with new branch
				USER_SEQUENCES.get(user).addFirst(branch);
			}
		}
		
		return start(user, ability.activate(type));
	}
	
	/**
	 * Attempts to start the given AbilityInstance for the user
	 * @param user who to start the instance for
	 * @param instance what to start
	 * @return false if user or instance is null or the event is canceled
	 */
	public static boolean start(AbilityUser user, AbilityInstance instance) {
		if (user == null || instance == null) {
			return false;
		}
		
		//AbilityPreStartEvent, return false if cancelled
		
		//throw in any modifications that have been added
		for (Modifier<?> mod : INSTANCE_MODIFIERS.get(instance)) {
			try {
				Field field = instance.getClass().getDeclaredField(mod.getField());
				setField(field, instance, mod.apply(field.get(instance)));
			} catch (Exception e) {
				continue;
			}
		}
		
		INSTANCES.computeIfAbsent(user, (u) -> new HashMap<>()).computeIfAbsent(instance.getClass(), (c) -> new HashSet<>()).add(instance);
		ACTIVE.add(instance);
		instance.start(user);
		return true;
	}
	
	private static void setField(Field field, Object instance, Object value) throws IllegalArgumentException, IllegalAccessException {
		boolean access = field.isAccessible();
		field.setAccessible(true);
		field.set(instance, value);
		field.setAccessible(access);
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
