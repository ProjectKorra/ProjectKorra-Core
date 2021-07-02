package com.projectkorra.core.system.ability;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;

import org.bukkit.event.Event;

import com.projectkorra.core.ProjectKorra;
import com.projectkorra.core.event.ability.AbilityInstanceStartEvent;
import com.projectkorra.core.event.ability.AbilityInstanceStopEvent;
import com.projectkorra.core.event.ability.AbilityInstanceStopEvent.Reason;
import com.projectkorra.core.event.user.UserActivationEvent;
import com.projectkorra.core.system.ability.activation.Activation;
import com.projectkorra.core.system.ability.activation.SequenceInfo;
import com.projectkorra.core.system.ability.attribute.Attribute;
import com.projectkorra.core.system.ability.attribute.Modifier;
import com.projectkorra.core.system.ability.type.Combo;
import com.projectkorra.core.system.ability.type.ExpanderInstance;
import com.projectkorra.core.system.ability.type.Passive;
import com.projectkorra.core.system.skill.Skill;
import com.projectkorra.core.util.EventUtil;
import com.projectkorra.core.util.configuration.Config;
import com.projectkorra.core.util.reflection.ReflectionUtil;

public final class AbilityManager {
	
	private AbilityManager() {}

	//static ability info
	private static final Map<Class<? extends Ability>, Ability> ABILITIES_BY_CLASS = new HashMap<>();
	private static final Map<String, Ability> ABILITIES_BY_NAME = new HashMap<>();
	private static final Map<Skill, Set<Ability>> ABILITIES_BY_SKILL = new HashMap<>();
	
	//instance info
	private static final Set<AbilityInstance> ACTIVE = new HashSet<>(256);
	private static final Map<Class<? extends AbilityInstance>, Map<String, Field>> ATTRIBUTES = new HashMap<>();
	private static final Map<AbilityInstance, Map<Field, Modifier>> MODIFIERS = new HashMap<>();
	private static final Map<AbilityUser, ActiveInfo> USER_INFO = new HashMap<>();
	
	//combo management
	private static final Map<Queue<SequenceInfo>, Ability> COMBOS = new HashMap<>();
	
	//passive management
	private static final Map<Skill, Map<Activation, Set<Ability>>> PASSIVES = new HashMap<>();
	
	private static boolean init = false;
	
	private static long prev = System.currentTimeMillis();
	
	public static void init(ProjectKorra plugin) {
		if (init) {
			return;
		}
		
		init = true;
		plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, () -> tick(), 0, 1);
	}
	
	private static ActiveInfo info(AbilityUser user) {
		return USER_INFO.computeIfAbsent(user, (u) -> new ActiveInfo());
	}
	
	public static Optional<Ability> getAbility(String name) {
		return Optional.ofNullable(ABILITIES_BY_NAME.get(name.toLowerCase()));
	}
	
	public static <T extends Ability> Optional<T> getAbility(Class<T> clazz) {
		return Optional.ofNullable(clazz.cast(ABILITIES_BY_CLASS.get(clazz)));
	}
	
	public static Set<Ability> getAbilities(Skill skill) {
		return new HashSet<>(ABILITIES_BY_SKILL.get(skill));
	}
	
	public static <T extends AbilityInstance> Optional<T> getInstance(AbilityUser user, Class<T> clazz) {
		AbilityInstances instances = info(user).getInstances(clazz);
		if (instances == null) {
			return Optional.ofNullable(null);
		} else {
			return Optional.ofNullable(clazz.cast(instances.top()));
		}
	}
	
	public static Optional<AbilityInstances> getInstances(AbilityUser user, Class<? extends AbilityInstance> clazz) {
		return Optional.ofNullable(info(user).getInstances(clazz));
	}
	
	public static boolean hasAttribute(AbilityInstance instance, String attribute) {
		return ATTRIBUTES.containsKey(instance.getClass()) && ATTRIBUTES.get(instance.getClass()).containsKey(attribute.toLowerCase());
	}
	
	public static boolean addModifier(AbilityInstance instance, String attribute, Modifier mod) {
		if (!hasAttribute(instance, attribute)) {
			return false;
		}
		
		Field field = ATTRIBUTES.get(instance.getClass()).get(attribute.toLowerCase());
		MODIFIERS.computeIfAbsent(instance, (i) -> new HashMap<>()).merge(field, mod, (a, b) -> a.and(b));
		return true;
	}
	
	/**
	 * Attempts to register the given {@link Ability} into the system.
	 * @param ability The ability to register
	 * @throws IllegalArgumentException if an ability with the given name exists
	 */
	public static void register(Ability ability) throws IllegalArgumentException {
		if (ABILITIES_BY_NAME.containsKey(ability.getName())) {
			throw new IllegalArgumentException("An Ability with named '" + ability.getName() + "' already exists!");
		} else if (ABILITIES_BY_CLASS.containsKey(ability.getClass())) {
			throw new IllegalArgumentException("An Ability from class '" + ability.getClass().getSimpleName() + "' already exists!");
		}
		
		//check if ability is enabled, return if not
		
		ABILITIES_BY_CLASS.put(ability.getClass(), ability);
		ABILITIES_BY_NAME.put(ability.getName(), ability);
		ABILITIES_BY_SKILL.computeIfAbsent(ability.getSkill(), (s) -> new HashSet<>()).add(ability);
		
		if (ability instanceof Combo) {
			try {
				COMBOS.put(ComboTree.build(((Combo) ability).getSequence()), ability);
			} catch (Exception e) {
				System.out.println(ability.getName() + " attempted to register a combo sequence which would never be activated!");
			}
		}
		
		if (ability instanceof Passive) {
			PASSIVES.computeIfAbsent(ability.getSkill(), (s) -> new HashMap<>()).computeIfAbsent(((Passive) ability).getTrigger(), (t) -> new HashSet<>()).add(ability);
		}
		
		for (Class<? extends AbilityInstance> instance : ability.instanceClasses()) {
			Map<String, Field> attributes = new HashMap<>();
			for (Field field : instance.getDeclaredFields()) {
				if (field.isAnnotationPresent(Attribute.class)) {
					attributes.put(field.getAnnotation(Attribute.class).value().toLowerCase(), field);
				}
			}
			ATTRIBUTES.put(instance, attributes);
		}
		
		Config.configure(ability);
	}
	
	/**
	 * Calls activation for the given user, checking for a valid
	 * combo that they can activate with their bound ability and 
	 * the given activation type, or activates their bound ability
	 * if nonnull
	 * @param user who to activate for
	 * @param trigger how to activate
	 * @param provider the event providing this activation (nullable)
	 * @return false if null or event is cancelled, otherwise passed to {@link #start(AbilityUser, AbilityInstance)}
	 */
	public static boolean activate(AbilityUser user, Activation trigger, Event provider) {
		if (user == null || trigger == null) {
			return false;
		}
		
		Ability ability = user.getBoundAbility();
		
		if (ability == null) {
			return false;
		}
		
		if (EventUtil.call(new UserActivationEvent(user, trigger, provider)).isCancelled()) {
			return false;
		}
		
		USER_INFO.computeIfAbsent(user, (u) -> new ActiveInfo());
		if (ability != null) {
			USER_INFO.get(user).updateCombos(ability, trigger);
		}
		
		return ability.canActivate(user, trigger) && start(ability.activate(user, trigger, provider));
	}
	
	/**
	 * Attempts to start the given AbilityInstance
	 * @param instance what to start
	 * @return false if user or instance is null or the event is cancelled
	 */
	public static boolean start(AbilityInstance instance) {
		if (instance == null || instance.getUser() == null) {
			return false;
		}
		
		if (instance instanceof ExpanderInstance) {
			if (!USER_INFO.get(instance.getUser()).expand((ExpanderInstance) instance)) {
				return false;
			}
		} else if (!USER_INFO.get(instance.getUser()).addInstance(instance)) {
			return false;
		}
		
		if (EventUtil.call(new AbilityInstanceStartEvent(instance)).isCancelled()) {
			return false;
		}
		
		if (MODIFIERS.containsKey(instance)) {
			for (Entry<Field, Modifier> entry : MODIFIERS.get(instance).entrySet()) {
				try {
					ReflectionUtil.setValueSafely(instance, entry.getKey(), entry.getValue().apply(entry.getKey().get(instance)));
				} catch (Exception e) {}
			}
			
			MODIFIERS.computeIfPresent(instance, (i, v) -> {
				v.clear();
				return null;
			});
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
		stop(instance, Reason.FORCED);
	}
	
	private static void stop(AbilityInstance instance, Reason reason) {
		EventUtil.call(new AbilityInstanceStopEvent(instance, reason));
		USER_INFO.get(instance.getUser()).removeInstance(instance);
		instance.stop();
	}
	
	static void tick() {
		double timeDelta = (System.currentTimeMillis() - prev) / 1000D;
		Iterator<AbilityInstance> iter = ACTIVE.iterator();
		
		while (iter.hasNext()) {
			AbilityInstance instance = iter.next();
			
			if (!instance.update(timeDelta)) {
				iter.remove();
				stop(instance, Reason.NATURAL);
				continue;
			}
			
			instance.postUpdate();
		}
		
		prev = System.currentTimeMillis();
	}
}
