package com.projectkorra.core.system.ability;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import org.bukkit.event.Event;

import com.projectkorra.core.ProjectKorra;
import com.projectkorra.core.system.ability.activation.Activation;
import com.projectkorra.core.system.ability.activation.SequenceInfo;
import com.projectkorra.core.system.ability.modifier.Modifier;
import com.projectkorra.core.system.ability.type.Combo;
import com.projectkorra.core.system.ability.type.ExpanderInstance;
import com.projectkorra.core.system.ability.type.Passive;
import com.projectkorra.core.system.skill.Skill;
import com.projectkorra.core.util.configuration.Configurable;

public final class AbilityManager {
	
	private AbilityManager() {}

	private static final Map<Class<? extends Ability>, Ability> ABILITIES_BY_CLASS = new HashMap<>();
	private static final Map<String, Ability> ABILITIES_BY_NAME = new HashMap<>();
	private static final Map<Skill, Set<Ability>> ABILITIES_BY_SKILL = new HashMap<>();
	private static final Map<AbilityInstance, Queue<Modifier<?>>> INSTANCE_MODIFIERS = new HashMap<>();
	private static final Set<AbilityInstance> ACTIVE = new HashSet<>(256);
	private static final Map<AbilityUser, ActiveInfo> USER_INFO = new HashMap<>();
	
	//combo management
	static final ComboTree COMBO_ROOT = new ComboTree();
	private static final Map<List<SequenceInfo>, Ability> COMBOS = new HashMap<>();
	
	//passive management
	private static final Map<Skill, Map<Activation, Set<Ability>>> PASSIVES = new HashMap<>();
	
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
		ABILITIES_BY_SKILL.computeIfAbsent(ability.getSkill(), (s) -> new HashSet<>()).add(ability);
		
		if (ability instanceof Combo) {
			List<SequenceInfo> sequence = ((Combo) ability).getSequence();
			COMBO_ROOT.build(sequence);
			COMBOS.put(sequence, ability);
		}
		
		if (ability instanceof Passive) {
			PASSIVES.computeIfAbsent(ability.getSkill(), (s) -> new HashMap<>()).computeIfAbsent(((Passive) ability).getTrigger(), (t) -> new HashSet<>()).add(ability);
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
	 * @param trigger how to activate
	 * @param provider the event providing this activation (nullable)
	 * @return false if null or event is canceled, otherwise passed to {@link #start(AbilityUser, AbilityInstance)}
	 */
	public static boolean activate(AbilityUser user, Activation trigger, Event provider) {
		if (user == null || trigger == null) {
			return false;
		}
		
		Ability ability = user.getBoundAbility();
		
		if (ability == null) {
			return false;
		}
		//AbilityActivateEvent, return false if canceled
		
		USER_INFO.computeIfAbsent(user, (u) -> new ActiveInfo());
		//attempt combo progression based on bound ability
		if (ability != null) {
			//start a new agent at the root
			USER_INFO.get(user).updateCombos(ability, trigger);
		}
		
		return ability.canActivate(user, trigger) && start(ability.activate(user, trigger, provider));
	}
	
	/**
	 * Attempts to start the given AbilityInstance
	 * @param instance what to start
	 * @return false if user or instance is null or the event is canceled
	 */
	public static boolean start(AbilityInstance instance) {
		if (instance == null || instance.getUser() == null) {
			return false;
		}
		
		if (instance instanceof ExpanderInstance && !USER_INFO.get(instance.getUser()).expand((ExpanderInstance) instance)) {
			return false;
		}
		
		if (!USER_INFO.get(instance.getUser()).addInstance(instance)) {
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
		
		ACTIVE.add(instance);
		instance.start();
		return true;
	}
	
	public static void refreshPassives(AbilityUser user) {
		for (Skill skill : user.getSkills()) {
			for (Ability passive : PASSIVES.get(skill).get(Activation.PASSIVE)) {
				start(passive.activate(user, Activation.PASSIVE, null));
			}
		}
	}
	
	public static void remove(AbilityInstance instance) {
		if (instance == null) {
			return;
		}
		
		ACTIVE.remove(instance);
		USER_INFO.get(instance.getUser()).removeInstance(instance);
		instance.stop();
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
			if (!instance.canUpdate()) {
				iter.remove();
				remove(instance);
			} else {
				instance.update();
			}
		}
	}
}
