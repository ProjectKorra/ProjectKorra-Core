package com.projectkorra.core.util.reflection;

import com.google.common.base.Preconditions;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;

/**
 * A utility class to provide simple and uniform ways utilize java.lang.reflect
 * Reflection.
 */
public class ReflectionUtil {
	/**
	 * Regex {@link java.util.regex.Pattern Pattern} used to match path variables in
	 * {@link getClass(final String classPath) getClass(final String classPath)}
	 */
	private static Pattern MATCH_VARIABLE = Pattern.compile("\\{([^\\}]+)\\}");

	private ReflectionUtil() {
	}

	// Classes

	/**
	 * Returns the {@link java.lang.Class Class} found at the given
	 * {@link java.lang.String String} path if it exists.
	 *
	 * @param classPath The {@link java.lang.String String} path to the desired
	 *                  class.
	 *                  Supports the automatic expansion of certain path variables:
	 *                  {OBC} = org.bukkit.craftbukkit.[version]
	 *                  {NMS} = net.minecraft.server.[version]
	 *                  {VERSION} = [version]
	 *
	 *                  Example: "{NMS}.Entity" =
	 *                  net.minecraft.server.[version].Entity
	 *
	 * @return The class represented by the given {@link java.lang.String String}
	 *         path.
	 *
	 * @throws IllegalArgumentException if the given {@link java.lang.String String}
	 *                                  path does not correspond with a valid class
	 *                                  or if an invalid path variable is used.
	 *
	 * @version 1.0.0
	 * @since 0.0.1
	 */
	public static Class<?> getClass(final String classPath) throws IllegalArgumentException {
		return getCanonicalClass(expandPathVariables(classPath));
	}

	/**
	 * Returns the {@link java.lang.Class Class} found at the given
	 * {@link java.lang.String String} path if it exists.
	 *
	 * @param canonicalName The exact {@link java.lang.String String} path of the
	 *                      desired class.
	 *
	 * @return The class represented by the given {@link java.lang.String String}
	 *         path.
	 *
	 * @throws IllegalArgumentException if the given {@link java.lang.String String}
	 *                                  path does not correspond with a valid class.
	 *
	 * @version 1.0.0
	 * @since 0.0.1
	 */
	private static Class<?> getCanonicalClass(final String canonicalName) throws IllegalArgumentException {
		try {
			return Class.forName(canonicalName);
		} catch (ClassNotFoundException e) {
			throw new IllegalArgumentException(String.format("The class %s could not be found", canonicalName));
		}
	}

	/**
	 * Converts the path variable aliases within the given {@link java.lang.String
	 * String} path to their actual counterparts.
	 *
	 * @param path The {@link java.lang.String String} path containing path variable
	 *             aliases to be expanded.
	 *
	 * @return The new {@link java.lang.String String} path with all path variable
	 *         aliases expanded to their actual counterparts.
	 *
	 * @throws IllegalArgumentException if the given {@link java.lang.String String}
	 *                                  path contains an invalid path variable.
	 *
	 * @version 1.0.0
	 * @since 0.0.1
	 */
	private static String expandPathVariables(final String path) throws IllegalArgumentException {
		StringBuffer output = new StringBuffer();
		Matcher matcher = MATCH_VARIABLE.matcher(path);

		while (matcher.find()) {
			String variable = matcher.group(1);
			String replacement = PathVariable.fromAlias(variable).getActual();

			// Assume the expanded variables are all part of packages and append a dot.
			if (replacement.length() > 0 && matcher.end() < path.length() && path.charAt(matcher.end()) != '.') {
				replacement += ".";
			}

			matcher.appendReplacement(output, Matcher.quoteReplacement(replacement));
		}

		matcher.appendTail(output);
		return output.toString();
	}

	/**
	 * Returns the {@link java.lang.Class Class} representing the type of the given
	 * {@link java.lang.Object Object}.
	 *
	 * @param object The {@link java.lang.Object Object} whose type should be
	 *               retrieved.
	 *
	 * @return The {@link java.lang.Class Class} representing the type of the given
	 *         {@link java.lang.Object Object}.
	 *
	 * @throws NullPointerException if the given {@link java.lang.Object Object} is
	 *                              null.
	 *
	 * @version 1.0.0
	 * @since 0.0.1
	 */
	private static Class<?> getType(final Object object) throws NullPointerException {
		Preconditions.checkNotNull(object);

		return object.getClass();
	}

	/**
	 * Returns a {@link java.lang.Class Class} array representing the types of the
	 * data stored within the given {@link java.lang.Object Object} array.
	 *
	 * @param objects The {@link java.lang.Object Object} array whose types should
	 *                be retrieved.
	 *
	 * @return A {@link java.lang.Class Class} array representing the recursively
	 *         accessed types of the data stored within the given
	 *         {@link java.lang.Object Object}.
	 *
	 * @throws NullPointerException if any of the data within the given
	 *                              {@link java.lang.Object Object} array is null.
	 *
	 * @version 1.0.0
	 * @since 0.0.1
	 */
	private static Class<?>[] getType(final Object[] objects) throws NullPointerException {
		final int length = (objects == null) ? 0 : objects.length;
		final Class<?>[] types = new Class<?>[length];

		for (int i = 0; i < length; i++) {
			types[i] = getType(objects[i]);
		}

		return types;
	}

	// Constructors

	/**
	 * Returns the {@link java.lang.reflect.Constructor Constructor} in the class at
	 * the given {@link java.lang.String String} path if it exists.
	 *
	 * @param classPath      The {@link java.lang.String String} path to the desired
	 *                       class.
	 * @param parameterTypes The {@link java.lang.Class Class} array representing
	 *                       the parameter types of the desired
	 *                       {@link java.lang.reflect.Constructor Constructor}.
	 *
	 * @return The {@link java.lang.reflect.Constructor Constructor} in the class at
	 *         the given {@link java.lang.String String} path if it exists.
	 *
	 * @throws IllegalArgumentException if the given {@link java.lang.String String}
	 *                                  path contains an invalid path variable.
	 * @throws ClassNotFoundException   if the given {@link java.lang.String String}
	 *                                  path does not correspond with an existing
	 *                                  class.
	 * @throws NoSuchMethodException    if a {@link java.lang.reflect.Constructor
	 *                                  Constructor} with the given parameter types
	 *                                  does not exist.
	 * @throws SecurityException        if the desired
	 *                                  {@link java.lang.reflect.Constructor
	 *                                  Constructor} cannot be accessed due to
	 *                                  insufficient permissions.
	 *
	 * @see #getClass(String)
	 *
	 * @version 1.0.0
	 * @since 0.0.1
	 */
	public static Constructor<?> getConstructor(final String classPath, final Class<?>... parameterTypes)
			throws IllegalArgumentException, ClassNotFoundException, NoSuchMethodException, SecurityException {
		return getConstructor(getClass(classPath), parameterTypes);
	}

	/**
	 * Returns the {@link java.lang.reflect.Constructor Constructor} in the given
	 * {@link java.lang.Class Class} if it exists.
	 *
	 * @param <T>            The type corresponding the the {@link java.lang.Class
	 *                       Class} the returned
	 *                       {@link java.lang.reflect.Constructor Constructor} comes
	 *                       from.
	 * @param clazz          The {@link java.lang.Class Class} containing the
	 *                       desired {@link java.lang.reflect.Constructor
	 *                       Constructor}.
	 * @param parameterTypes The {@link java.lang.Class Class} array representing
	 *                       the parameter types of the desired
	 *                       {@link java.lang.reflect.Constructor Constructor}.
	 *
	 * @return The {@link java.lang.reflect.Constructor Constructor} in the given
	 *         {@link java.lang.Class Class} if it exists.
	 *
	 * @throws NullPointerException  if the given {@link java.lang.Class Class} is
	 *                               null.
	 * @throws NoSuchMethodException if a {@link java.lang.reflect.Constructor
	 *                               Constructor} with the given parameter types
	 *                               does not exist.
	 * @throws SecurityException     if the desired
	 *                               {@link java.lang.reflect.Constructor
	 *                               Constructor} cannot be accessed due to
	 *                               insufficient permissions.
	 *
	 * @version 1.0.0
	 * @since 0.0.1
	 */
	public static <T> Constructor<T> getConstructor(final Class<T> clazz, final Class<?>... parameterTypes)
			throws NullPointerException, NoSuchMethodException, SecurityException {
		Preconditions.checkNotNull(clazz, "clazz cannot be null");

		Constructor<T> constructor = clazz.getDeclaredConstructor(parameterTypes);
		constructor.setAccessible(true);

		return constructor;
	}

	/**
	 * Creates and returns an instance of the class at the given
	 * {@link java.lang.String String} path if it exists.
	 *
	 * @param classPath The {@link java.lang.String String} path to the desired
	 *                  class to instantiate.
	 * @param arguments The {@link java.lang.Object Object} array containing the
	 *                  arguments needed to construct the new instance.
	 *
	 * @return The new {@link java.lang.Object Object} instance of the class at the
	 *         given {@link java.lang.String String} path if it exists.
	 *
	 * @throws IllegalArgumentException  if the given {@link java.lang.String
	 *                                   String} path contains an invalid path
	 *                                   variable or invalid arguments are passed to
	 *                                   the corresponding
	 *                                   {@link java.lang.reflect.Constructor
	 *                                   Constructor}.
	 * @throws ClassNotFoundException    if the given {@link java.lang.String
	 *                                   String} path does not correspond with an
	 *                                   existing class.
	 * @throws NoSuchMethodException     if a {@link java.lang.reflect.Constructor
	 *                                   Constructor} with the given argument types
	 *                                   does not exist.
	 * @throws InstantiationException    if the {@link java.lang.Object Object}
	 *                                   cannot be instantiated, most likely caused
	 *                                   by the specified class being abstract.
	 * @throws IllegalAccessException    if the corresponding
	 *                                   {@link java.lang.reflect.Constructor
	 *                                   Constructor} cannot be accessed.
	 * @throws InvocationTargetException if the found
	 *                                   {@link java.lang.reflect.Constructor
	 *                                   Constructor} throws an exception during
	 *                                   instantiation.
	 *
	 * @see #getClass(String)
	 * @see #getConstructor(String, Class...)
	 *
	 * @version 1.0.0
	 * @since 0.0.1
	 */
	public static Object instantiateObject(final String classPath, final Object... arguments)
			throws IllegalArgumentException, ClassNotFoundException, NoSuchMethodException, InstantiationException,
			IllegalAccessException, InvocationTargetException {
		return instantiateObject(getClass(classPath), arguments);
	}

	/**
	 * Creates and returns an instance of the given class at the given
	 * {@link java.lang.Class Class} if a corresponding constructor it exists.
	 *
	 * @param <T>       The type corresponding the the {@link java.lang.Class Class}
	 *                  the invoked {@link java.lang.reflect.Constructor
	 *                  Constructor} comes from.
	 * @param clazz     The {@link java.lang.Class Class} to instantiate.
	 * @param arguments The {@link java.lang.Object Object} array containing the
	 *                  arguments needed to construct the new instance.
	 *
	 * @return The new T instance of the given {@link java.lang.Class Class} if a
	 *         corresponding constructor it exists.
	 *
	 * @throws NullPointerException      if the given {@link java.lang.Class Class}
	 *                                   is null or if any of the specified
	 *                                   arguments are null.
	 * @throws NoSuchMethodException     if a {@link java.lang.reflect.Constructor
	 *                                   Constructor} with the given argument types
	 *                                   does not exist.
	 * @throws SecurityException         if the corresponding
	 *                                   {@link java.lang.reflect.Constructor
	 *                                   Constructor} cannot be accessed due to
	 *                                   insufficient permissions.
	 * @throws InstantiationException    if the type T cannot be instantiated, most
	 *                                   likely caused by the class being abstract.
	 * @throws IllegalAccessException    if the corresponding
	 *                                   {@link java.lang.reflect.Constructor
	 *                                   Constructor} cannot be accessed.
	 * @throws IllegalArgumentException  if invalid arguments are passed to the
	 *                                   corresponding
	 *                                   {@link java.lang.reflect.Constructor
	 *                                   Constructor}.
	 * @throws InvocationTargetException if the found
	 *                                   {@link java.lang.reflect.Constructor
	 *                                   Constructor} throws an exception during
	 *                                   instantiation.
	 *
	 * @see #getConstructor(Class, Class...)
	 *
	 * @version 1.0.0
	 * @since 0.0.1
	 */
	public static <T> T instantiateObject(final Class<T> clazz, final Object... arguments)
			throws NullPointerException, NoSuchMethodException, SecurityException, InstantiationException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		return getConstructor(clazz, getType(arguments)).newInstance(arguments);
	}

	/**
	 * Creates and returns an instance of the class corresponding to the given
	 * {@link java.lang.reflect.Constructor Constructor}.
	 *
	 * @param <T>         The type corresponding the the {@link java.lang.Class
	 *                    Class} the invoked {@link java.lang.reflect.Constructor
	 *                    Constructor} comes from.
	 * @param constructor The {@link java.lang.reflect.Constructor Constructor}
	 *                    being invoked.
	 * @param arguments   The {@link java.lang.Object Object} array containing the
	 *                    arguments needed to construct the new instance.
	 *
	 * @return The new T instance of the class corresponding to the given
	 *         {@link java.lang.reflect.Constructor Constructor}
	 *
	 * @throws NullPointerException      if the given
	 *                                   {@link java.lang.reflect.Constructor
	 *                                   Constructor} is null.
	 * @throws InstantiationException    if the type T cannot be instantiated, most
	 *                                   likely caused by the class being abstract.
	 * @throws IllegalAccessException    if the given
	 *                                   {@link java.lang.reflect.Constructor
	 *                                   Constructor} cannot be accessed.
	 * @throws IllegalArgumentException  if invalid arguments are passed to the
	 *                                   corresponding
	 *                                   {@link java.lang.reflect.Constructor
	 *                                   Constructor}.
	 * @throws InvocationTargetException if the given
	 *                                   {@link java.lang.reflect.Constructor
	 *                                   Constructor} throws an exception during
	 *                                   instantiation.
	 *
	 * @version 1.0.0
	 * @since 0.0.1
	 */
	public static <T> T instantiateObject(final Constructor<T> constructor, final Object... arguments)
			throws NullPointerException, InstantiationException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException {
		Preconditions.checkNotNull(constructor, "constructor cannot be null");

		return constructor.newInstance(arguments);
	}

	// Methods

	/**
	 * Returns the {@link java.lang.reflect.Method Method} in the class at the given
	 * {@link java.lang.String String} path if it exists.
	 *
	 * @param classPath      The {@link java.lang.String String} path to the class
	 *                       conatining the desired {@link java.lang.reflect.Method
	 *                       Method}.
	 * @param methodName     The {@link java.lang.String String} name of the desired
	 *                       {@link java.lang.reflect.Method Method}.
	 * @param parameterTypes The {@link java.lang.Class Class} array representing
	 *                       the parameter types of the desired
	 *                       {@link java.lang.reflect.Method Method}.
	 *
	 * @return The {@link java.lang.reflect.Method Method} in the class at the given
	 *         {@link java.lang.String String} path if it exists.
	 *
	 * @throws IllegalArgumentException if the given {@link java.lang.String String}
	 *                                  path contains an invalid path variable.
	 * @throws ClassNotFoundException   if the given {@link java.lang.String String}
	 *                                  path does not correspond with an existing
	 *                                  class.
	 * @throws NoSuchMethodException    if a {@link java.lang.reflect.Method Method}
	 *                                  with the given parameter types does not
	 *                                  exist.
	 * @throws SecurityException        if the desired
	 *                                  {@link java.lang.reflect.Method Method}
	 *                                  cannot be accessed due to insufficient
	 *                                  permissions.
	 *
	 * @see #getClass(String)
	 *
	 * @version 1.0.0
	 * @since 0.0.1
	 */
	public static Method getMethod(final String classPath, final String methodName, final Class<?>... parameterTypes)
			throws IllegalArgumentException, ClassNotFoundException, NoSuchMethodException, SecurityException {
		return getMethod(getClass(classPath), methodName, parameterTypes);
	}

	/**
	 * Returns the {@link java.lang.reflect.Method Method} in the given
	 * {@link java.lang.Class Class} if it exists.
	 *
	 * @param clazz          The {@link java.lang.Class Class} containing the
	 *                       desired {@link java.lang.reflect.Method Method}.
	 * @param methodName     The {@link java.lang.String String} name of the desired
	 *                       {@link java.lang.reflect.Method Method}.
	 * @param parameterTypes The {@link java.lang.Class Class} array representing
	 *                       the parameter types of the desired
	 *                       {@link java.lang.reflect.Method Method}.
	 *
	 * @return The {@link java.lang.reflect.Method Method} in the class at the given
	 *         {@link java.lang.String String} path if it exists.
	 *
	 * @throws NullPointerException  if the given {@link java.lang.Class Class} is
	 *                               null.
	 * @throws NoSuchMethodException if a {@link java.lang.reflect.Method Method}
	 *                               with the given parameter types does not exist.
	 * @throws SecurityException     if the desired {@link java.lang.reflect.Method
	 *                               Method} cannot be accessed due to insufficient
	 *                               permissions.
	 *
	 * @version 1.0.0
	 * @since 0.0.1
	 */
	public static Method getMethod(final Class<?> clazz, final String methodName, final Class<?>... parameterTypes)
			throws NullPointerException, NoSuchMethodException, SecurityException {
		Preconditions.checkNotNull(clazz, "clazz cannot be null");

		// Checks all methods within the specified class.
		for (final Method method : clazz.getDeclaredMethods()) {
			if (method.getName().equals(methodName) && Arrays.equals(method.getParameterTypes(), parameterTypes)) {
				method.setAccessible(true);
				return method;
			}
		}

		// Search in the parent class.
		if (clazz.getSuperclass() != null) {
			return getMethod(clazz.getSuperclass(), methodName, parameterTypes);
		}

		throw new NoSuchMethodException(
				String.format("The method %s could not be found with the specified parameter types", methodName));
	}

	/**
	 * Invokes the specified {@link java.lang.reflect.Method Method} for the given
	 * instance.
	 *
	 * @param instance   The {@link java.lang.Object Object} instance calling the
	 *                   desired {@link java.lang.reflect.Method Method}.
	 * @param classPath  The {@link java.lang.String String} path to the class
	 *                   conatining the desired {@link java.lang.reflect.Method
	 *                   Method}.
	 * @param methodName The {@link java.lang.String String} name of the desired
	 *                   {@link java.lang.reflect.Method Method}.
	 * @param arguments  The {@link java.lang.Object Object} array containing the
	 *                   arguments needed to call the desired
	 *                   {@link java.lang.reflect.Method Method}.
	 *
	 * @return The {@link java.lang.Object Object} data returned by the desired
	 *         {@link java.lang.reflect.Method Method} if it has a return value,
	 *         otherwise null.
	 *
	 * @throws IllegalArgumentException  if the given {@link java.lang.String
	 *                                   String} path contains an invalid path
	 *                                   variable or invalid arguments are passed to
	 *                                   the corresponding
	 *                                   {@link java.lang.reflect.Method Method}.
	 * @throws ClassNotFoundException    if the given {@link java.lang.String
	 *                                   String} path does not correspond with an
	 *                                   existing class.
	 * @throws NoSuchMethodException     if a {@link java.lang.reflect.Method
	 *                                   Method} with the given parameter types does
	 *                                   not exist.
	 * @throws NullPointerException      if the given
	 *                                   {@link java.lang.reflect.Method Method} is
	 *                                   null or if any of the specified arguments
	 *                                   are null.
	 * @throws SecurityException         if the desired
	 *                                   {@link java.lang.reflect.Method Method}
	 *                                   cannot be accessed due to insufficient
	 *                                   permissions.
	 * @throws IllegalAccessException    if the desired
	 *                                   {@link java.lang.reflect.Method Method}
	 *                                   cannot be accessed.
	 * @throws InvocationTargetException if the desired
	 *                                   {@link java.lang.reflect.Method Method}
	 *                                   throws an exception during invocation.
	 *
	 * @see #getClass(String)
	 * @see #getType(Object[])
	 * @see #getMethod(String, String, Class...)
	 *
	 * @version 1.0.0
	 * @since 0.0.1
	 */
	public static Object invokeMethod(final Object instance, final String classPath, final String methodName,
			final Object... arguments)
			throws IllegalArgumentException, ClassNotFoundException, NoSuchMethodException, NullPointerException,
			SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		return invokeMethod(instance, getMethod(getClass(classPath), methodName, getType(arguments)), arguments);
	}

	/**
	 * Invokes the specified {@link java.lang.reflect.Method Method} for the given
	 * instance.
	 *
	 * @param instance   The {@link java.lang.Object Object} instance calling the
	 *                   desired {@link java.lang.reflect.Method Method}.
	 * @param clazz      The {@link java.lang.Class Class} containing the desired
	 *                   {@link java.lang.reflect.Method Method}.
	 * @param methodName The {@link java.lang.String String} name of the desired
	 *                   {@link java.lang.reflect.Method Method}.
	 * @param arguments  The {@link java.lang.Object Object} array containing the
	 *                   arguments needed to call the desired
	 *                   {@link java.lang.reflect.Method Method}.
	 *
	 * @return The {@link java.lang.Object Object} data returned by the desired
	 *         {@link java.lang.reflect.Method Method} if it has a return value,
	 *         otherwise null.
	 *
	 * @throws NoSuchMethodException     if a {@link java.lang.reflect.Method
	 *                                   Method} with the given parameter types does
	 *                                   not exist.
	 * @throws NullPointerException      if the given
	 *                                   {@link java.lang.reflect.Method Method} is
	 *                                   null, if the given {@link java.lang.Class
	 *                                   Class} is null, or if any of the specified
	 *                                   arguments are null.
	 * @throws SecurityException         if the desired
	 *                                   {@link java.lang.reflect.Method Method}
	 *                                   cannot be accessed due to insufficient
	 *                                   permissions.
	 * @throws IllegalAccessException    if the desired
	 *                                   {@link java.lang.reflect.Method Method}
	 *                                   cannot be accessed.
	 * @throws InvocationTargetException if the desired
	 *                                   {@link java.lang.reflect.Method Method}
	 *                                   throws an exception during invocation.
	 * @throws IllegalArgumentException  if invalid arguments are passed to the
	 *                                   corresponding
	 *                                   {@link java.lang.reflect.Method Method}.
	 *
	 * @see #getType(Object[])
	 * @see #getMethod(String, String, Class...)
	 *
	 * @version 1.0.0
	 * @since 0.0.1
	 */
	public static Object invokeMethod(final Object instance, final Class<?> clazz, final String methodName,
			final Object... arguments) throws NoSuchMethodException, NullPointerException, IllegalAccessException,
			InvocationTargetException, IllegalArgumentException {
		return invokeMethod(instance, getMethod(clazz, methodName, getType(arguments)), arguments);
	}

	/**
	 * Invokes the specified {@link java.lang.reflect.Method Method} for the given
	 * instance.
	 *
	 * @param instance  The {@link java.lang.Object Object} instance calling the
	 *                  given {@link java.lang.reflect.Method Method}.
	 * @param method    The {@link java.lang.reflect.Method Method} being invoked.
	 * @param arguments The {@link java.lang.Object Object} array containing the
	 *                  arguments needed to call the given
	 *                  {@link java.lang.reflect.Method Method}.
	 *
	 * @return The {@link java.lang.Object Object} data returned by the desired
	 *         {@link java.lang.reflect.Method Method} if it has a return value,
	 *         otherwise null.
	 *
	 * @throws IllegalAccessException    if the given
	 *                                   {@link java.lang.reflect.Method Method}
	 *                                   cannot be accessed.
	 * @throws InvocationTargetException if the given
	 *                                   {@link java.lang.reflect.Method Method}
	 *                                   throws an exception during invocation.
	 * @throws IllegalArgumentException  if invalid arguments are passed to the
	 *                                   corresponding
	 *                                   {@link java.lang.reflect.Method Method}.
	 *
	 * @version 1.0.0
	 * @since 0.0.1
	 */
	public static Object invokeMethod(final Object instance, final Method method, final Object... arguments)
			throws IllegalAccessException, InvocationTargetException, IllegalArgumentException {
		return method.invoke(instance, arguments);
	}

	/**
	 * Invokes the specified {@link java.lang.reflect.Method Method} for the given
	 * instance.
	 *
	 * @param <T>        The type corresponding the the return type of the given
	 *                   {@link java.lang.reflect.Method Method}.
	 * @param instance   The {@link java.lang.Object Object} instance calling the
	 *                   desired {@link java.lang.reflect.Method Method}.
	 * @param classPath  The {@link java.lang.String String} path to the class
	 *                   conatining the desired {@link java.lang.reflect.Method
	 *                   Method}.
	 * @param methodName The {@link java.lang.String String} name of the desired
	 *                   {@link java.lang.reflect.Method Method}.
	 * @param returnType The {@link java.lang.Class Class} representing the return
	 *                   type of the given {@link java.lang.reflect.Method Method}.
	 * @param arguments  The {@link java.lang.Object Object} array containing the
	 *                   arguments needed to call the desired
	 *                   {@link java.lang.reflect.Method Method}.
	 *
	 * @return The T data returned by the desired {@link java.lang.reflect.Method
	 *         Method} if it has a return value, otherwise null.
	 *
	 * @throws IllegalArgumentException  if the given {@link java.lang.String
	 *                                   String} path contains an invalid path
	 *                                   variable, if invalid arguments are passed
	 *                                   to the corresponding
	 *                                   {@link java.lang.reflect.Method Method}, or
	 *                                   if the given type T is not assignable to
	 *                                   the actual return type of the given
	 *                                   {@link java.lang.reflect.Method Method}.
	 * @throws ClassNotFoundException    if the given {@link java.lang.String
	 *                                   String} path does not correspond with an
	 *                                   existing class.
	 * @throws NoSuchMethodException     if a {@link java.lang.reflect.Method
	 *                                   Method} with the given parameter types does
	 *                                   not exist.
	 * @throws NullPointerException      if the given
	 *                                   {@link java.lang.reflect.Method Method} is
	 *                                   null or if any of the specified arguments
	 *                                   are null.
	 * @throws SecurityException         if the desired
	 *                                   {@link java.lang.reflect.Method Method}
	 *                                   cannot be accessed due to insufficient
	 *                                   permissions.
	 * @throws InvocationTargetException if the desired
	 *                                   {@link java.lang.reflect.Method Method}
	 *                                   throws an exception during invocation.
	 * @throws IllegalAccessException    if the desired
	 *                                   {@link java.lang.reflect.Method Method}
	 *                                   cannot be accessed.
	 *
	 * @see #getClass(String)
	 * @see #getType(Object[])
	 * @see #getMethod(String, String, Class...)
	 *
	 * @version 1.0.0
	 * @since 0.0.1
	 */
	public static <T> T invokeMethod(final Object instance, final String classPath, final String methodName,
			final Class<T> returnType, final Object... arguments)
			throws IllegalArgumentException, ClassNotFoundException, NoSuchMethodException, NullPointerException,
			SecurityException, InvocationTargetException, IllegalAccessException {
		return invokeMethod(instance, getMethod(getClass(classPath), methodName, getType(arguments)), returnType,
				arguments);
	}

	/**
	 * Invokes the specified {@link java.lang.reflect.Method Method} for the given
	 * instance.
	 *
	 * @param <T>        The type corresponding the the return type of the given
	 *                   {@link java.lang.reflect.Method Method}.
	 * @param instance   The {@link java.lang.Object Object} instance calling the
	 *                   desired {@link java.lang.reflect.Method Method}.
	 * @param clazz      The {@link java.lang.Class Class} containing the desired
	 *                   {@link java.lang.reflect.Method Method}.
	 * @param methodName The {@link java.lang.String String} name of the desired
	 *                   {@link java.lang.reflect.Method Method}.
	 * @param returnType The {@link java.lang.Class Class} representing the return
	 *                   type of the given {@link java.lang.reflect.Method Method}.
	 * @param arguments  The {@link java.lang.Object Object} array containing the
	 *                   arguments needed to call the desired
	 *                   {@link java.lang.reflect.Method Method}.
	 *
	 * @return The T data returned by the desired {@link java.lang.reflect.Method
	 *         Method} if it has a return value, otherwise null.
	 *
	 * @throws NoSuchMethodException     if a {@link java.lang.reflect.Method
	 *                                   Method} with the given parameter types does
	 *                                   not exist.
	 * @throws NullPointerException      if the given
	 *                                   {@link java.lang.reflect.Method Method} is
	 *                                   null or if the given {@link java.lang.Class
	 *                                   Class} is null or if any of the specified
	 *                                   arguments are null.
	 * @throws SecurityException         if the desired
	 *                                   {@link java.lang.reflect.Method Method}
	 *                                   cannot be accessed due to insufficient
	 *                                   permissions.
	 * @throws IllegalAccessException    if the desired
	 *                                   {@link java.lang.reflect.Method Method}
	 *                                   cannot be accessed.
	 * @throws InvocationTargetException if the desired
	 *                                   {@link java.lang.reflect.Method Method}
	 *                                   throws an exception during invocation.
	 * @throws IllegalArgumentException  if invalid arguments are passed to the
	 *                                   corresponding
	 *                                   {@link java.lang.reflect.Method Method} or
	 *                                   if the given type T is not assignable to
	 *                                   the actual return type of the corresponding
	 *                                   {@link java.lang.reflect.Method Method}.
	 *
	 * @see #getType(Object[])
	 * @see #getMethod(String, String, Class...)
	 *
	 * @version 1.0.0
	 * @since 0.0.1
	 */
	public static <T> T invokeMethod(final Object instance, final Class<?> clazz, final String methodName,
			final Class<T> returnType, final Object... arguments) throws NoSuchMethodException, NullPointerException,
			SecurityException, IllegalAccessException, InvocationTargetException, IllegalArgumentException {
		return invokeMethod(instance, getMethod(clazz, methodName, getType(arguments)), returnType, arguments);
	}

	/**
	 * Invokes the specified {@link java.lang.reflect.Method Method} for the given
	 * instance.
	 *
	 * @param <T>        The type corresponding the the return type of the given
	 *                   {@link java.lang.reflect.Method Method}.
	 * @param instance   The {@link java.lang.Object Object} instance calling the
	 *                   given {@link java.lang.reflect.Method Method}.
	 * @param method     The {@link java.lang.reflect.Method Method} being invoked.
	 * @param returnType The {@link java.lang.Class Class} representing the return
	 *                   type of the given {@link java.lang.reflect.Method Method}.
	 * @param arguments  The {@link java.lang.Object Object} array containing the
	 *                   arguments needed to call the given
	 *                   {@link java.lang.reflect.Method Method}.
	 *
	 * @return The T data returned by the desired {@link java.lang.reflect.Method
	 *         Method} if it has a return value, otherwise null.
	 *
	 * @throws NullPointerException      if the given
	 *                                   {@link java.lang.reflect.Method Method} is
	 *                                   null.
	 * @throws IllegalAccessException    if the given
	 *                                   {@link java.lang.reflect.Method Method}
	 *                                   cannot be accessed.
	 * @throws InvocationTargetException if the given
	 *                                   {@link java.lang.reflect.Method Method}
	 *                                   throws an exception during invocation.
	 * @throws IllegalArgumentException  if invalid arguments are passed to the
	 *                                   corresponding
	 *                                   {@link java.lang.reflect.Method Method} or
	 *                                   if the given type T is not assignable to
	 *                                   the actual return type of the given
	 *                                   {@link java.lang.reflect.Method Method}.
	 *
	 * @version 1.0.0
	 * @since 0.0.1
	 */
	public static <T> T invokeMethod(final Object instance, final Method method, final Class<T> returnType,
			final Object... arguments)
			throws NullPointerException, IllegalAccessException, InvocationTargetException, IllegalArgumentException {
		Preconditions.checkNotNull(method, "method cannot be null");

		if (returnType == null) {
			return (T) method.invoke(instance, arguments); // null if void or Object, T will be Object if null is passed
															// in but we still have to cast.
		} else if (method.getReturnType().isAssignableFrom(returnType)) {
			Object obj = method.invoke(instance, arguments);

			return (obj == null) ? null : returnType.cast(obj); // null if void or T.
		} else {
			throw new IllegalArgumentException(String.format("The type %s is not assignable from the return type of %s",
					returnType, method.getName()));
		}
	}

	// Fields

	/**
	 * Returns the {@link java.lang.reflect.Field Field} in the class at the given
	 * {@link java.lang.String String} path if it exists.
	 *
	 * @param classPath The {@link java.lang.String String} path to the class
	 *                  conatining the desired {@link java.lang.reflect.Field
	 *                  Field}.
	 * @param fieldName The {@link java.lang.String String} name of the desired
	 *                  {@link java.lang.reflect.Field Field}.
	 *
	 * @return The {@link java.lang.reflect.Field Field} in the class at the given
	 *         {@link java.lang.String String} path if it exists.
	 *
	 * @throws IllegalArgumentException if the given {@link java.lang.String String}
	 *                                  path contains an invalid path variable.
	 * @throws ClassNotFoundException   if the given {@link java.lang.String String}
	 *                                  path does not correspond with an existing
	 *                                  class.
	 * @throws NoSuchFieldException     if a {@link java.lang.reflect.Field Field}
	 *                                  with the given name does not exist.
	 * @throws SecurityException        if the desired
	 *                                  {@link java.lang.reflect.Field Field} cannot
	 *                                  be accessed due to insufficient permissions.
	 *
	 * @see #getClass(String)
	 *
	 * @version 1.0.0
	 * @since 0.0.1
	 */
	public static Field getField(final String classPath, final String fieldName)
			throws IllegalArgumentException, ClassNotFoundException, NoSuchFieldException, SecurityException {
		return getField(getClass(classPath), fieldName);
	}

	/**
	 * Returns the {@link java.lang.reflect.Field Field} in the given
	 * {@link java.lang.Class Class} if it exists.
	 *
	 * @param clazz     The {@link java.lang.Class Class} containing the desired
	 *                  {@link java.lang.reflect.Field Field}.
	 * @param fieldName The {@link java.lang.String String} name of the desired
	 *                  {@link java.lang.reflect.Field Field}.
	 *
	 * @return The {@link java.lang.reflect.Field Field} in the class at the given
	 *         {@link java.lang.String String} path if it exists.
	 *
	 * @throws NullPointerException if the given {@link java.lang.Class Class} is
	 *                              null.
	 * @throws NoSuchFieldException if a {@link java.lang.reflect.Field Field} with
	 *                              the given name does not exist.
	 * @throws SecurityException    if the desired {@link java.lang.reflect.Field
	 *                              Field} cannot be accessed due to insufficient
	 *                              permissions.
	 *
	 * @version 1.0.0
	 * @since 0.0.1
	 */
	public static Field getField(final Class<?> clazz, final String fieldName)
			throws NullPointerException, NoSuchFieldException, SecurityException {
		Preconditions.checkNotNull(clazz, "clazz cannot be null");

		// Checks all fields within the specified class.
		for (final Field field : clazz.getDeclaredFields()) {
			if (field.getName().equals(fieldName)) {
				field.setAccessible(true);
				return field;
			}
		}

		// Search in the parent class.
		if (clazz.getSuperclass() != null) {
			return getField(clazz.getSuperclass(), fieldName);
		}

		throw new NoSuchFieldException(String.format("Cannot find the field %s", fieldName));
	}

	/**
	 * Returns the {@link java.lang.Object Object} value of the
	 * {@link java.lang.reflect.Field Field} with the given {@link java.lang.String
	 * String} name for the given instance.
	 *
	 * @param instance  The {@link java.lang.Object Object} instance whose data is
	 *                  being retrieved.
	 * @param classPath The {@link java.lang.String String} path to the class
	 *                  conatining the desired {@link java.lang.reflect.Field Field}
	 *                  value.
	 * @param fieldName The {@link java.lang.String String} name of the desired
	 *                  {@link java.lang.reflect.Field Field}.
	 *
	 * @return the {@link java.lang.Object Object} value of the
	 *         {@link java.lang.reflect.Field Field} with the given
	 *         {@link java.lang.String String} name for the given instance.
	 *
	 * @throws IllegalArgumentException if the given {@link java.lang.String String}
	 *                                  path contains an invalid path variable.
	 * @throws ClassNotFoundException   if the given {@link java.lang.String String}
	 *                                  path does not correspond with an existing
	 *                                  class.
	 * @throws NoSuchFieldException     if a {@link java.lang.reflect.Field Field}
	 *                                  with the given name does not exist.
	 * @throws SecurityException        if the desired
	 *                                  {@link java.lang.reflect.Field Field} cannot
	 *                                  be accessed due to insufficient permissions.
	 * @throws IllegalAccessException   if the desired
	 *                                  {@link java.lang.reflect.Field Field} cannot
	 *                                  be accessed.
	 *
	 * @version 1.0.0
	 * @since 0.0.1
	 */
	public static Object getValue(final Object instance, final String classPath, final String fieldName)
			throws IllegalArgumentException, ClassNotFoundException, NoSuchFieldException, SecurityException,
			IllegalAccessException {
		return getValue(instance, getField(getClass(classPath), fieldName), null);
	}

	/**
	 * Returns the {@link java.lang.Object Object} value of the
	 * {@link java.lang.reflect.Field Field} with the given {@link java.lang.String
	 * String} name for the given instance.
	 *
	 * @param instance  The {@link java.lang.Object Object} instance whose data is
	 *                  being retrieved.
	 * @param clazz     The {@link java.lang.Class Class} containing the desired
	 *                  {@link java.lang.reflect.Field Field} value.
	 * @param fieldName The {@link java.lang.String String} name of the desired
	 *                  {@link java.lang.reflect.Field Field}.
	 *
	 * @return the {@link java.lang.Object Object} value of the
	 *         {@link java.lang.reflect.Field Field} with the given
	 *         {@link java.lang.String String} name for the given instance.
	 *
	 * @throws NullPointerException   if the given {@link java.lang.Class Class} is
	 *                                null.
	 * @throws NoSuchFieldException   if a {@link java.lang.reflect.Field Field}
	 *                                with the given name does not exist.
	 * @throws SecurityException      if the desired {@link java.lang.reflect.Field
	 *                                Field} cannot be accessed due to insufficient
	 *                                permissions.
	 * @throws IllegalAccessException if the desired {@link java.lang.reflect.Field
	 *                                Field} cannot be accessed.
	 *
	 * @version 1.0.0
	 * @since 0.0.1
	 */
	public static Object getValue(final Object instance, final Class<?> clazz, final String fieldName)
			throws NullPointerException, NoSuchFieldException, SecurityException, IllegalAccessException {
		return getValue(instance, getField(clazz, fieldName), null);
	}

	/**
	 * Returns the {@link java.lang.Object Object} value of the
	 * {@link java.lang.reflect.Field Field} for the given instance.
	 *
	 * @param instance The {@link java.lang.Object Object} instance whose data is
	 *                 being retrieved.
	 * @param field    The {@link java.lang.reflect.Field Field} containing the
	 *                 desired value.
	 *
	 * @return the {@link java.lang.Object Object} value of the
	 *         {@link java.lang.reflect.Field Field} for the given instance.
	 *
	 * @throws NullPointerException   if the given {@link java.lang.reflect.Field
	 *                                Field} is null.
	 * @throws IllegalAccessException if the desired {@link java.lang.reflect.Field
	 *                                Field} cannot be accessed.
	 *
	 * @version 1.0.0
	 * @since 0.0.1
	 */
	public static Object getValue(final Object instance, final Field field)
			throws NullPointerException, IllegalAccessException {
		return getValue(instance, field, null);
	}

	/**
	 * Returns the T value of the {@link java.lang.reflect.Field Field} with the
	 * given {@link java.lang.String String} name for the given instance.
	 *
	 * @param <T>       The type corresponding the the type of the desired
	 *                  {@link java.lang.reflect.Field Field}.
	 * @param instance  The {@link java.lang.Object Object} instance containing the
	 *                  desired {@link java.lang.reflect.Field Field} value.
	 * @param classPath The {@link java.lang.String String} path to the class
	 *                  conatining the desired {@link java.lang.reflect.Field Field}
	 *                  value.
	 * @param fieldName The {@link java.lang.String String} name of the desired
	 *                  {@link java.lang.reflect.Field Field}.
	 * @param type      The {@link java.lang.Class Class} representing the type of
	 *                  the desired {@link java.lang.reflect.Field Field} value.
	 *
	 * @return the T value of the {@link java.lang.reflect.Field Field} with the
	 *         given {@link java.lang.String String} name for the given instance.
	 *
	 * @throws IllegalArgumentException if the given {@link java.lang.String String}
	 *                                  path contains an invalid path variable or if
	 *                                  the given type T is not assignable to the
	 *                                  actual type of the desired
	 *                                  {@link java.lang.reflect.Field Field}.
	 * @throws ClassNotFoundException   if the given {@link java.lang.String String}
	 *                                  path does not correspond with an existing
	 *                                  class.
	 * @throws NoSuchFieldException     if a {@link java.lang.reflect.Field Field}
	 *                                  with the given name does not exist.
	 * @throws SecurityException        if the desired
	 *                                  {@link java.lang.reflect.Field Field} cannot
	 *                                  be accessed due to insufficient permissions.
	 * @throws IllegalAccessException   if the desired
	 *                                  {@link java.lang.reflect.Field Field} cannot
	 *                                  be accessed.
	 *
	 * @version 1.0.0
	 * @since 0.0.1
	 */
	public static <T> T getValue(final Object instance, final String classPath, final String fieldName,
			final Class<T> type) throws IllegalArgumentException, ClassNotFoundException, NoSuchFieldException,
			SecurityException, IllegalAccessException {
		return getValue(instance, getField(getClass(classPath), fieldName), type);
	}

	/**
	 * Returns the T value of the {@link java.lang.reflect.Field Field} with the
	 * given {@link java.lang.String String} name for the given instance.
	 *
	 * @param <T>       The type corresponding the the type of the desired
	 *                  {@link java.lang.reflect.Field Field}.
	 * @param instance  The {@link java.lang.Object Object} instance containing the
	 *                  desired {@link java.lang.reflect.Field Field} value.
	 * @param clazz     The {@link java.lang.Class Class} containing the desired
	 *                  {@link java.lang.reflect.Field Field} value.
	 * @param fieldName The {@link java.lang.String String} name of the desired
	 *                  {@link java.lang.reflect.Field Field}.
	 * @param type      The {@link java.lang.Class Class} representing the type of
	 *                  the desired {@link java.lang.reflect.Field Field} value.
	 *
	 * @return the T value of the {@link java.lang.reflect.Field Field} with the
	 *         given {@link java.lang.String String} name for the given instance.
	 *
	 * @throws NoSuchFieldException     if a {@link java.lang.reflect.Field Field}
	 *                                  with the given name does not exist.
	 * @throws SecurityException        if the desired
	 *                                  {@link java.lang.reflect.Field Field} cannot
	 *                                  be accessed due to insufficient permissions.
	 * @throws IllegalAccessException   if the desired
	 *                                  {@link java.lang.reflect.Field Field} cannot
	 *                                  be accessed.
	 * @throws IllegalArgumentException if the given type T is not assignable to the
	 *                                  actual type of the desired
	 *                                  {@link java.lang.reflect.Field Field}.
	 *
	 * @version 1.0.0
	 * @since 0.0.1
	 */
	public static <T> T getValue(final Object instance, final Class<?> clazz, final String fieldName,
			final Class<T> type)
			throws NoSuchFieldException, SecurityException, IllegalAccessException, IllegalArgumentException {
		return getValue(instance, getField(clazz, fieldName), type);
	}

	/**
	 * Returns the T value of the {@link java.lang.reflect.Field Field} for the
	 * given instance.
	 *
	 * @param <T>      The type corresponding the the type of the desired
	 *                 {@link java.lang.reflect.Field Field}.
	 * @param instance The {@link java.lang.Object Object} instance containing the
	 *                 desired {@link java.lang.reflect.Field Field} value.
	 * @param field    The {@link java.lang.reflect.Field Field} containing the
	 *                 desired value.
	 * @param type     The {@link java.lang.Class Class} representing the type of
	 *                 the desired {@link java.lang.reflect.Field Field} value.
	 *
	 * @return the T value of the {@link java.lang.reflect.Field Field} for the
	 *         given instance.
	 *
	 * @throws NullPointerException     if the given {@link java.lang.reflect.Field
	 *                                  Field} is null.
	 * @throws IllegalArgumentException if the given type T is not assignable to the
	 *                                  actual type of the given
	 *                                  {@link java.lang.reflect.Field Field}.
	 * @throws IllegalAccessException   if the given {@link java.lang.reflect.Field
	 *                                  Field} cannot be accessed.
	 *
	 * @version 1.0.0
	 * @since 0.0.1
	 */
	public static <T> T getValue(final Object instance, final Field field, final Class<T> type)
			throws NullPointerException, IllegalArgumentException, IllegalAccessException {
		Preconditions.checkNotNull(field, "field cannot be null");

		if (type == null) {
			return (T) field.get(instance); // null or Object, T will be Object if null is passed in but we still have
											// to cast.
		} else if (field.getType().isAssignableFrom(type)) {
			Object obj = field.get(instance);

			return (obj == null) ? null : type.cast(obj); // null or T.
		} else {
			throw new IllegalArgumentException(
					String.format("The type %s is not assignable from the type of %s", type, field.getName()));
		}
	}

	/**
	 * Sets the value of the {@link java.lang.reflect.Field Field} with the given
	 * {@link java.lang.String String} name for the given instance.
	 *
	 * @param instance  The {@link java.lang.Object Object} instance containing the
	 *                  desired {@link java.lang.reflect.Field Field} to be
	 *                  modified.
	 * @param classPath The {@link java.lang.String String} path to the class
	 *                  conatining the desired {@link java.lang.reflect.Field Field}
	 *                  to be modified.
	 * @param fieldName The {@link java.lang.String String} name of the desired
	 *                  {@link java.lang.reflect.Field Field}.
	 * @param value     The new value of the desired {@link java.lang.reflect.Field
	 *                  Field}.
	 *
	 * @throws IllegalArgumentException if the given {@link java.lang.String String}
	 *                                  path contains an invalid path variable or if
	 *                                  the given value cannot be assigned to the
	 *                                  desired {@link java.lang.reflect.Field
	 *                                  Field}.
	 * @throws ClassNotFoundException   if the given {@link java.lang.String String}
	 *                                  path does not correspond with an existing
	 *                                  class.
	 * @throws NoSuchFieldException     if a {@link java.lang.reflect.Field Field}
	 *                                  with the given name does not exist.
	 * @throws SecurityException        if the desired
	 *                                  {@link java.lang.reflect.Field Field} cannot
	 *                                  be accessed due to insufficient permissions.
	 * @throws IllegalAccessException   if the desired
	 *                                  {@link java.lang.reflect.Field Field} cannot
	 *                                  be accessed.
	 *
	 * @version 1.0.0
	 * @since 0.0.1
	 */
	public static void setValue(final Object instance, final String classPath, final String fieldName,
			final Object value) throws IllegalArgumentException, ClassNotFoundException, NoSuchFieldException,
			SecurityException, IllegalAccessException {
		getField(getClass(classPath), fieldName).set(instance, value);
	}

	/**
	 * Sets the value of the {@link java.lang.reflect.Field Field} with the given
	 * {@link java.lang.String String} name for the given instance.
	 *
	 * @param instance  The {@link java.lang.Object Object} instance containing the
	 *                  desired {@link java.lang.reflect.Field Field} to be
	 *                  modified.
	 * @param clazz     The {@link java.lang.Class Class} containing the desired
	 *                  {@link java.lang.reflect.Field Field} to be modified.
	 * @param fieldName The {@link java.lang.String String} name of the desired
	 *                  {@link java.lang.reflect.Field Field}.
	 * @param value     The new value of the desired {@link java.lang.reflect.Field
	 *                  Field}.
	 *
	 * @throws NullPointerException     if the given {@link java.lang.Class Class}
	 *                                  is null.
	 * @throws NoSuchFieldException     if a {@link java.lang.reflect.Field Field}
	 *                                  with the given name does not exist.
	 * @throws SecurityException        if the desired
	 *                                  {@link java.lang.reflect.Field Field} cannot
	 *                                  be accessed due to insufficient permissions.
	 * @throws IllegalAccessException   if the desired
	 *                                  {@link java.lang.reflect.Field Field} cannot
	 *                                  be accessed.
	 * @throws IllegalArgumentException if the given value cannot be assigned to the
	 *                                  desired {@link java.lang.reflect.Field
	 *                                  Field}.
	 *
	 * @version 1.0.0
	 * @since 0.0.1
	 */
	public static void setValue(final Object instance, final Class<?> clazz, final String fieldName, final Object value)
			throws NullPointerException, NoSuchFieldException, SecurityException, IllegalAccessException,
			IllegalArgumentException {
		getField(clazz, fieldName).set(instance, value);
	}

	/**
	 * Sets the value of the given {@link java.lang.reflect.Field Field} for the
	 * given instance.
	 *
	 * @param instance The {@link java.lang.Object Object} instance containing the
	 *                 given {@link java.lang.reflect.Field Field} to be modified.
	 * @param field    The {@link java.lang.reflect.Field Field} to be modified.
	 * @param value    The new value of the desired {@link java.lang.reflect.Field
	 *                 Field}.
	 *
	 * @throws NullPointerException     if the given {@link java.lang.reflect.Field
	 *                                  Field} is null.
	 * @throws IllegalAccessException   if the desired
	 *                                  {@link java.lang.reflect.Field Field} cannot
	 *                                  be accessed.
	 * @throws IllegalArgumentException if the given value cannot be assigned to the
	 *                                  desired {@link java.lang.reflect.Field
	 *                                  Field}.
	 *
	 * @version 1.0.0
	 * @since 0.0.1
	 */
	public static void setValue(final Object instance, final Field field, final Object value)
			throws NullPointerException, IllegalAccessException, IllegalArgumentException {
		Preconditions.checkNotNull(field, "field cannot be null");

		field.set(instance, value);
	}

	/**
	 * Enum to easily represent path variables and hold their alias / actual values.
	 */
	private enum PathVariable {
		OBC("OBC", Bukkit.getServer().getClass().getPackage().getName()),
		NMS("NMS", OBC.actual.replace("org.bukkit.craftbukkit", "net.minecraft.server")),
		VERSION("VERSION", OBC.actual.substring(23));

		private static final Map<String, PathVariable> VARIABLE_MAP = new HashMap<String, PathVariable>();

		static {
			for (final PathVariable type : values()) {
				VARIABLE_MAP.put(type.alias, type);
			}
		}

		private String alias;
		private String actual;

		private PathVariable(final String alias, final String actual) {
			this.alias = alias.toUpperCase();
			this.actual = actual.toLowerCase();
		}

		public String getAlias() {
			return this.alias;
		}

		public String getActual() {
			return this.actual;
		}

		/**
		 * Determines if the {@link java.util.String String} alias corresponds with an
		 * existing {@link PathVariable PathVariable}.
		 *
		 * @param alias The {@link java.util.String String} alias of potential variable.
		 *
		 * @return true if the {@link java.util.String String} alias corresponds with an
		 *         existing {@link PathVariable PathVariable}, false otherwise.
		 *
		 * @version 1.0.0
		 * @since 0.0.1
		 */
		public static boolean isValidAlias(final String alias) {
			return VARIABLE_MAP.containsKey(alias.toUpperCase());
		}

		/**
		 * Returns the {@link PathVariable PathVariable} with the matching
		 * {@link java.util.String String} alias if it exists.
		 *
		 * @param alias The {@link java.util.String String} alias of the desired
		 *              variable.
		 *
		 * @return The {@link PathVariable PathVariable} with the matching
		 *         {@link java.util.String String} alias.
		 *
		 * @throws IllegalArgumentException if the given {@link java.util.String String}
		 *                                  alias does not correspond with a valid
		 *                                  {@link PathVariable PathVariable}.
		 *
		 * @version 1.0.0
		 * @since 0.0.1
		 */
		public static PathVariable fromAlias(final String alias) throws IllegalArgumentException {
			if (!isValidAlias(alias)) {
				throw new IllegalArgumentException(String.format("The alias %s is not a valid path variable!", alias));
			}

			return VARIABLE_MAP.get(alias.toUpperCase());
		}
	}
}