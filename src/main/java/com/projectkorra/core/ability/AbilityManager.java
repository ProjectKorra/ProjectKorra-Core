package com.projectkorra.core.ability;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;

import org.bukkit.event.Event;
import org.bukkit.plugin.java.JavaPlugin;

import com.projectkorra.core.ProjectKorra;
import com.projectkorra.core.ability.activation.Activation;
import com.projectkorra.core.ability.activation.SequenceInfo;
import com.projectkorra.core.ability.attribute.Attribute;
import com.projectkorra.core.ability.attribute.AttributeGroup;
import com.projectkorra.core.ability.attribute.Modifier;
import com.projectkorra.core.ability.type.Combo;
import com.projectkorra.core.ability.type.ExpanderInstance;
import com.projectkorra.core.ability.type.Passive;
import com.projectkorra.core.collision.Collidable;
import com.projectkorra.core.collision.CollisionManager;
import com.projectkorra.core.event.ability.InstanceStartEvent;
import com.projectkorra.core.event.ability.InstanceStopEvent;
import com.projectkorra.core.event.ability.InstanceStopEvent.Reason;
import com.projectkorra.core.event.user.UserActivationEvent;
import com.projectkorra.core.skill.Skill;
import com.projectkorra.core.util.Events;
import com.projectkorra.core.util.configuration.Config;
import com.projectkorra.core.util.configuration.Configure;
import com.projectkorra.core.util.reflection.DynamicLoader;
import com.projectkorra.core.util.reflection.ReflectionUtil;

public final class AbilityManager {

	private AbilityManager() {
	}

	// static ability info
	private static final Map<Class<? extends Ability>, Ability> ABILITIES_BY_CLASS = new HashMap<>();
	private static final Map<String, Ability> ABILITIES_BY_NAME = new HashMap<>();
	private static final Map<Skill, Set<Ability>> ABILITIES_BY_SKILL = new HashMap<>();

	// instance info
	private static final Set<AbilityInstance> ACTIVE = new HashSet<>(256);
	private static final Map<Class<? extends AbilityInstance>, Map<String, Field>> ATTRIBUTES = new HashMap<>();
	private static final Map<AbilityInstance, Map<Field, Modifier>> MODIFIERS = new HashMap<>();
	private static final Map<AbilityUser, ActiveInfo> USER_INFO = new HashMap<>();

	// combo management
	private static final Map<String, Ability> COMBOS = new HashMap<>();

	// passive management
	private static final Map<Skill, Map<Activation, Set<Ability>>> PASSIVES = new HashMap<>();

	private static boolean init = false;
	private static ProjectKorra plugin;

	private static long prev = System.currentTimeMillis();

	public static void init(ProjectKorra pk) {
		if (init) {
			return;
		}

		init = true;
		plugin = pk;
		plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, () -> tick(), 0, 1);
	}

	private static ActiveInfo info(AbilityUser user) {
		return USER_INFO.computeIfAbsent(user, (u) -> new ActiveInfo(u));
	}

	public static Optional<Ability> getAbility(String name) {
		return Optional.ofNullable(ABILITIES_BY_NAME.get(name.toLowerCase()));
	}

	public static <T extends Ability> Optional<T> getAbility(Class<T> clazz) {
		return Optional.ofNullable(clazz.cast(ABILITIES_BY_CLASS.get(clazz)));
	}

	public static Set<Ability> getAbilities(Skill skill) {
		if (!ABILITIES_BY_SKILL.containsKey(skill)) {
			return new HashSet<>();
		}
		
		return new HashSet<>(ABILITIES_BY_SKILL.get(skill));
	}

	public static Set<Ability> getAbilities() {
		return new HashSet<>(ABILITIES_BY_CLASS.values());
	}

	public static Set<Ability> getAbilitiesUserCanBind(AbilityUser user) {
		Set<Ability> abilities = new HashSet<>();

		for (Ability ability : ABILITIES_BY_CLASS.values()) {
			if (user.canBind(ability)) {
				abilities.add(ability);
			}
		}

		return abilities;
	}

	public static <T extends AbilityInstance> Optional<T> getInstance(AbilityUser user, Class<T> clazz) {
		LinkedList<AbilityInstance> instances = info(user).getInstances(clazz);
		if (instances == null) {
			return Optional.ofNullable(null);
		} else {
			return Optional.ofNullable(clazz.cast(instances.peek()));
		}
	}

	public static List<AbilityInstance> getInstances(AbilityUser user, Class<? extends AbilityInstance> clazz) {
		if (!hasInstance(user, clazz)) {
			return Collections.emptyList();
		}

		return new ArrayList<>(info(user).getInstances(clazz));
	}

	public static boolean hasInstance(AbilityUser user, Class<? extends AbilityInstance> clazz) {
		return info(user).hasInstance(clazz);
	}

	public static boolean hasAttribute(AbilityInstance instance, String attribute) {
		return ATTRIBUTES.containsKey(instance.getClass()) && ATTRIBUTES.get(instance.getClass()).containsKey(attribute.toLowerCase());
	}
	
	/**
	 * Attempts to add the given modifier to the given attribute of the given instance.
	 * 
	 * @param instance  {@link AbilityInstance} to modify
	 * @param attribute name of attribute to modify (see {@link Attribute})
	 * @param mod       {@link Modifier} to use
	 * @return whether the modifier was successfully added or not
	 */
	public static boolean addModifier(AbilityInstance instance, String attribute, Modifier mod) {
		if (!hasAttribute(instance, attribute)) {
			return false;
		}

		Field field = ATTRIBUTES.get(instance.getClass()).get(attribute.toLowerCase());
		MODIFIERS.computeIfAbsent(instance, (i) -> new HashMap<>()).merge(field, mod, (a, b) -> a.and(b));
		return true;
	}

	/**
	 * Attempts to add the modifier to the given group of attributes for the given instance
	 * 
	 * @param instance {@link AbilityInstance} to modify
	 * @param group    {@link AttributeGroup} to modify for instance
	 * @param mod      {@link Modifier} to use
	 * @return array of booleans for each attribute in the group of whether the modifier was successfully added or not
	 */
	public static boolean[] addModifier(AbilityInstance instance, AttributeGroup group, Modifier mod) {
		boolean[] worked = new boolean[group.size()];
		int i = -1;
		
		for (String attribute : group.getAttributes()) {
			worked[++i] = addModifier(instance, attribute, mod);
		}
		
		return worked;
	}

	/**
	 * Attempts to register the given {@link Ability} into the system. The process
	 * of registration goes:
	 * <ul>
	 * <li>Ability is cached by class, name, and skill
	 * <li>Combos are loaded into the {@link ComboTree}
	 * <li>Passives are cached by their activation trigger
	 * <li>Instance attributes have their fields stored by name
	 * <li>The ability is auto-configured using the {@link Configure} annotation
	 * </ul>
	 * <br>
	 * 
	 * @param <T>     Type of the ability
	 * @param ability The ability to register
	 * @throws IllegalArgumentException if an ability with the given name exists
	 * @return the successfully registered ability
	 */
	public static <T extends Ability> T register(T ability) throws IllegalArgumentException {
		if (ABILITIES_BY_NAME.containsKey(ability.getName().toLowerCase())) {
			throw new IllegalArgumentException("An Ability with named '" + ability.getName() + "' already exists!");
		} else if (ABILITIES_BY_CLASS.containsKey(ability.getClass())) {
			throw new IllegalArgumentException("An Ability from class '" + ability.getClass().getSimpleName() + "' already exists!");
		}

		// check if ability is enabled, return if not (exception possibly? or would null
		// work?)

		ABILITIES_BY_CLASS.put(ability.getClass(), ability);
		ABILITIES_BY_NAME.put(ability.getName().toLowerCase(), ability);
		ABILITIES_BY_SKILL.computeIfAbsent(ability.getSkill(), (s) -> new HashSet<>()).add(ability);

		if (ability instanceof Combo) {
			try {
				COMBOS.put(SequenceInfo.stringify(ComboTree.build(((Combo) ability).getSequence())), ability);
			} catch (Exception e) {
				System.out.println(ability.getName() + " attempted to register a combo sequence which would never be activated!");
			}
		}

		if (ability instanceof Passive) {
			PASSIVES.computeIfAbsent(ability.getSkill(), (s) -> new HashMap<>()).computeIfAbsent(((Passive) ability).getTrigger(), (t) -> new HashSet<>()).add(ability);
		}

		return Config.process(Events.register(ability));
	}

	public static void registerFrom(JavaPlugin plugin, String path) {
		DynamicLoader.load(plugin, path, (c) -> Ability.class.isAssignableFrom(c) || AbilityInstance.class.isAssignableFrom(c), (clazz) -> {
			if (Ability.class.isAssignableFrom(clazz)) {
				try {
					register((Ability) clazz.getDeclaredConstructor().newInstance());
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (AbilityInstance.class.isAssignableFrom(clazz)) {
				Class<? extends AbilityInstance> instance = clazz.asSubclass(AbilityInstance.class);
				Map<String, Field> attributes = new HashMap<>();

				for (Field field : clazz.getDeclaredFields()) {
					if (field.isAnnotationPresent(Attribute.class)) {
						attributes.put(field.getAnnotation(Attribute.class).value().toLowerCase(), field);
					}
				}

				ATTRIBUTES.put(instance, attributes);
			}
		});
	}

	/**
	 * Calls activation for the given user, checking for a valid combo that they can
	 * activate with their bound ability and the given activation type, or activates
	 * their bound ability if nonnull
	 * 
	 * @param user     who to activate for
	 * @param trigger  how to activate
	 * @param provider the event providing this activation (nullable, but some may
	 *                 not activate without)
	 * @return false if null or event is cancelled, otherwise passed to
	 *         {@link #start(AbilityUser, AbilityInstance)}
	 */
	public static boolean activate(AbilityUser user, Activation trigger, Event provider) {
		if (user == null || trigger == null) {
			return false;
		}

		Ability ability = user.getBoundAbility().orElseGet(() -> null);

		if (ability == null) {
			return false;
		}

		if (Events.call(new UserActivationEvent(user, trigger, provider)).isCancelled()) {
			return false;
		}

		if (trigger.canCombo()) {
			ComboValidator combo = USER_INFO.computeIfAbsent(user, (u) -> new ActiveInfo(u)).updateCombos(ability, trigger);
			if (combo != null) {
				ability = COMBOS.get(SequenceInfo.stringify(combo.getSequence()));
				trigger = Activation.COMBO;
				provider = null;
			}
		}

		return ability.canActivate(user, trigger) && start(ability.activate(user, trigger, provider));
	}

	/**
	 * Attempts to start the given AbilityInstance. At this stage: an
	 * {@link InstanceStartEvent} will be called, an {@link ExpanderInstance} will
	 * have their abilities bound,
	 * 
	 * @param instance what to start
	 * @return false if user or instance is null or the event is cancelled
	 */
	public static boolean start(AbilityInstance instance) {
		if (instance == null || instance.getUser() == null) {
			return false;
		} else if (Events.call(new InstanceStartEvent(instance)).isCancelled()) {
			return false;
		}

		if (MODIFIERS.containsKey(instance)) {
			for (Entry<Field, Modifier> entry : MODIFIERS.get(instance).entrySet()) {
				try {
					Object applied = entry.getValue().apply(ReflectionUtil.getValueSafely(instance, entry.getKey(), Object.class));
					ReflectionUtil.setValueSafely(instance, entry.getKey(), applied);
				} catch (Exception e) {
				}
			}

			MODIFIERS.computeIfPresent(instance, (i, v) -> {
				v.clear();
				return null;
			});
		}

		if (!instance.start()) {
			return false;
		} else if (!info(instance.getUser()).addInstance(instance)) {
			return false;
		}
		
		if (instance.hasUpdate()) {
			ACTIVE.add(instance);
		}

		return true;
	}

	public static void refreshPassives(AbilityUser user) {
		for (Skill skill : user.getSkills()) {
			if (!PASSIVES.containsKey(skill)) {
				continue;
			} else if (PASSIVES.get(skill) == null) {
				continue;
			} else if (!PASSIVES.get(skill).containsKey(Activation.PASSIVE)) {
				continue;
			}

			for (Ability passive : PASSIVES.get(skill).get(Activation.PASSIVE)) {
				start(passive.activate(user, Activation.PASSIVE, null));
			}
		}
	}

	public static void removeAll(AbilityUser user) {
		info(user).clear();
	}

	public static void remove(AbilityInstance instance) {
		if (instance == null) {
			return;
		}

		ACTIVE.remove(instance);
		stop(instance, Reason.FORCED);
	}

	private static void stop(AbilityInstance instance, Reason reason) {
		Events.call(new InstanceStopEvent(instance, reason));
		USER_INFO.get(instance.getUser()).removeInstance(instance);
		instance.stop();
	}

	static void tick() {
		double timeDelta = (System.currentTimeMillis() - prev) / 1000D;
		Iterator<AbilityInstance> iter = ACTIVE.iterator();
		
		while (iter.hasNext()) {
			AbilityInstance instance = iter.next();
			instance.preUpdate();

			if (!instance.update(timeDelta)) {
				iter.remove();
				stop(instance, Reason.NATURAL);
				continue;
			}

			instance.postUpdate();
			if (instance instanceof Collidable) {
				CollisionManager.addCollidable((Collidable) instance);
			}
		}

		prev = System.currentTimeMillis();
	}
}
