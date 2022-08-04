package com.projectkorra.core.util;

import com.projectkorra.core.ProjectKorra;

import org.apache.commons.lang.Validate;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

/**
 * A utility class to provide simple and uniform ways to schedule
 * {@link java.lang.Runnable Runnables} and
 * {@link org.bukkit.scheduler.BukkitRunnable BukkitRunnables}.
 */
public class Threads {
	private static final ProjectKorra PLUGIN = JavaPlugin.getPlugin(ProjectKorra.class);

	private Threads() {
	}

	/**
	 * Schedules the given {@link org.bukkit.scheduler.BukkitRunnable
	 * BukkitRunnable} to run <b>synchronously</b> starting on next tick. Wraps
	 * {@link org.bukkit.scheduler.BukkitRunnable#runTask BukkitRunnable#runTask()}.
	 *
	 * @param runnable The {@link org.bukkit.scheduler.BukkitRunnable
	 *                 BukkitRunnable} being scheduled.
	 *
	 * @return A {@link org.bukkit.scheduler.BukkitTask BukkitTask} representing the
	 *         given {@link org.bukkit.scheduler.BukkitRunnable BukkitRunnable}
	 *         being executed by the scheduler.
	 *
	 * @throws NullPointerException  if the given
	 *                               {@link org.bukkit.scheduler.BukkitRunnable
	 *                               BukkitRunnable} is null.
	 * @throws IllegalStateException if the given
	 *                               {@link org.bukkit.scheduler.BukkitRunnable
	 *                               BukkitRunnable} has already been scheduled.
	 *
	 * @version 1.0.0
	 * @since 0.0.1
	 */
	public static BukkitTask runTask(BukkitRunnable runnable) throws NullPointerException, IllegalStateException {
		Validate.notNull(runnable, "runnable cannot be null");
		return runnable.runTask(PLUGIN);
	}

	/**
	 * Schedules the given {@link java.lang.Runnable Runnable} to run
	 * <b>synchronously</b> starting on next tick. Wraps
	 * {@link org.bukkit.scheduler.BukkitScheduler#runTask
	 * BukkitScheduler#runTask()}.
	 *
	 * @param runnable The {@link java.lang.Runnable Runnable} being scheduled.
	 *
	 * @return A {@link org.bukkit.scheduler.BukkitTask BukkitTask} representing the
	 *         given {@link java.lang.Runnable Runnable} being executed by the
	 *         scheduler.
	 *
	 * @throws NullPointerException  if the given {@link java.lang.Runnable
	 *                               Runnable} is null.
	 * @throws IllegalStateException if the given {@link java.lang.Runnable
	 *                               Runnable} has already been scheduled.
	 *
	 * @version 1.0.0
	 * @since 0.0.1
	 */
	public static BukkitTask runTask(Runnable runnable) throws NullPointerException, IllegalStateException {
		Validate.notNull(runnable, "runnable cannot be null");
		return PLUGIN.getServer().getScheduler().runTask(PLUGIN, runnable);
	}

	/**
	 * Schedules the given {@link org.bukkit.scheduler.BukkitRunnable
	 * BukkitRunnable} to run <b>asynchronously</b> starting on next tick. Wraps
	 * {@link org.bukkit.scheduler.BukkitRunnable#runTaskAsynchronously
	 * BukkitRunnable#runTaskAsynchronously()}.
	 *
	 * @param runnable The {@link org.bukkit.scheduler.BukkitRunnable
	 *                 BukkitRunnable} being scheduled.
	 *
	 * @return A {@link org.bukkit.scheduler.BukkitTask BukkitTask} representing the
	 *         given {@link org.bukkit.scheduler.BukkitRunnable BukkitRunnable}
	 *         being executed by the scheduler.
	 *
	 * @throws NullPointerException  if the given
	 *                               {@link org.bukkit.scheduler.BukkitRunnable
	 *                               BukkitRunnable} is null.
	 * @throws IllegalStateException if the given
	 *                               {@link org.bukkit.scheduler.BukkitRunnable
	 *                               BukkitRunnable} has already been scheduled.
	 *
	 * @version 1.0.0
	 * @since 0.0.1
	 */
	public static BukkitTask runTaskAsynchronously(BukkitRunnable runnable) throws NullPointerException, IllegalStateException {
		Validate.notNull(runnable, "runnable cannot be null");
		return runnable.runTaskAsynchronously(PLUGIN);
	}

	/**
	 * Schedules the given {@link java.lang.Runnable Runnable} to run
	 * <b>asynchronously</b> starting on next tick. Wraps
	 * {@link org.bukkit.scheduler.BukkitScheduler#runTaskAsynchronously
	 * BukkitScheduler#runTaskAsynchronously()}.
	 *
	 * @param runnable The {@link java.lang.Runnable Runnable} being scheduled.
	 *
	 * @return A {@link org.bukkit.scheduler.BukkitTask BukkitTask} representing the
	 *         given {@link java.lang.Runnable Runnable} being executed by the
	 *         scheduler.
	 *
	 * @throws NullPointerException  if the given {@link java.lang.Runnable
	 *                               Runnable} is null.
	 * @throws IllegalStateException if the given {@link java.lang.Runnable
	 *                               Runnable} has already been scheduled.
	 *
	 * @version 1.0.0
	 * @since 0.0.1
	 */
	public static BukkitTask runTaskAsynchronously(Runnable runnable) throws NullPointerException, IllegalStateException {
		Validate.notNull(runnable, "runnable cannot be null");
		return PLUGIN.getServer().getScheduler().runTaskAsynchronously(PLUGIN, runnable);
	}

	/**
	 * Schedules the given {@link org.bukkit.scheduler.BukkitRunnable
	 * BukkitRunnable} to run <b>synchronously</b> after the given tick delay
	 * elapses. Wraps {@link org.bukkit.scheduler.BukkitRunnable#runTaskLater
	 * BukkitRunnable#runTaskLater()}.
	 *
	 * @param runnable The {@link org.bukkit.scheduler.BukkitRunnable
	 *                 BukkitRunnable} being scheduled.
	 * @param delay    The number of <u>ticks</u> the scheduler should wait before
	 *                 beginning execution of the given
	 *                 {@link org.bukkit.scheduler.BukkitRunnable BukkitRunnable}.
	 *
	 * @return A {@link org.bukkit.scheduler.BukkitTask BukkitTask} representing the
	 *         given {@link org.bukkit.scheduler.BukkitRunnable BukkitRunnable}
	 *         being executed by the scheduler.
	 *
	 * @throws NullPointerException     if the given
	 *                                  {@link org.bukkit.scheduler.BukkitRunnable
	 *                                  BukkitRunnable} is null.
	 * @throws IllegalStateException    if the given
	 *                                  {@link org.bukkit.scheduler.BukkitRunnable
	 *                                  BukkitRunnable} has already been scheduled.
	 * @throws IllegalArgumentException if the given delay is negative.
	 *
	 * @version 1.0.0
	 * @since 0.0.1
	 */
	public static BukkitTask runDelayedTask(BukkitRunnable runnable, long delay) throws NullPointerException, IllegalStateException, IllegalArgumentException {
		Validate.notNull(runnable, "runnable cannot be null");
		Validate.isTrue(delay >= 0, "delay cannot be negative");
		return runnable.runTaskLater(PLUGIN, delay);
	}

	/**
	 * Schedules the given {@link java.lang.Runnable Runnable} to run
	 * <b>synchronously</b> after the given tick delay elapses. Wraps
	 * {@link org.bukkit.scheduler.BukkitScheduler#runTaskLater
	 * BukkitScheduler#runTaskLater()}.
	 *
	 * @param runnable The {@link java.lang.Runnable Runnable} being scheduled.
	 * @param delay    The number of <u>ticks</u> the scheduler should wait before
	 *                 beginning execution of the given {@link java.lang.Runnable
	 *                 Runnable}.
	 *
	 * @return A {@link org.bukkit.scheduler.BukkitTask BukkitTask} representing the
	 *         given {@link java.lang.Runnable Runnable} being executed by the
	 *         scheduler.
	 *
	 * @throws NullPointerException     if the given {@link java.lang.Runnable
	 *                                  Runnable} is null.
	 * @throws IllegalStateException    if the given {@link java.lang.Runnable
	 *                                  Runnable} has already been scheduled.
	 * @throws IllegalArgumentException if the given delay is negative.
	 *
	 * @version 1.0.0
	 * @since 0.0.1
	 */
	public static BukkitTask runDelayedTask(Runnable runnable, long delay) throws NullPointerException, IllegalStateException, IllegalArgumentException {
		Validate.notNull(runnable, "runnable cannot be null");
		Validate.isTrue(delay >= 0, "delay cannot be negative");
		return PLUGIN.getServer().getScheduler().runTaskLater(PLUGIN, runnable, delay);
	}

	/**
	 * Schedules the given {@link org.bukkit.scheduler.BukkitRunnable
	 * BukkitRunnable} to run <b>asynchronously</b> after the given tick delay
	 * elapses. Wraps
	 * {@link org.bukkit.scheduler.BukkitRunnable#runTaskLaterAsynchronously
	 * BukkitRunnable#runTaskLaterAsynchronously()}.
	 *
	 * @param runnable The {@link org.bukkit.scheduler.BukkitRunnable
	 *                 BukkitRunnable} being scheduled.
	 * @param delay    The number of <u>ticks</u> the scheduler should wait before
	 *                 beginning execution of the given
	 *                 {@link org.bukkit.scheduler.BukkitRunnable BukkitRunnable}.
	 *
	 * @return A {@link org.bukkit.scheduler.BukkitTask BukkitTask} representing the
	 *         given {@link org.bukkit.scheduler.BukkitRunnable BukkitRunnable}
	 *         being executed by the scheduler.
	 *
	 * @throws NullPointerException     if the given
	 *                                  {@link org.bukkit.scheduler.BukkitRunnable
	 *                                  BukkitRunnable} is null.
	 * @throws IllegalStateException    if the given
	 *                                  {@link org.bukkit.scheduler.BukkitRunnable
	 *                                  BukkitRunnable} has already been scheduled.
	 * @throws IllegalArgumentException if the given delay is negative.
	 *
	 * @version 1.0.0
	 * @since 0.0.1
	 */
	public static BukkitTask runDelayedTaskAsynchronously(BukkitRunnable runnable, long delay) throws NullPointerException, IllegalStateException, IllegalArgumentException {
		Validate.notNull(runnable, "runnable cannot be null");
		Validate.isTrue(delay >= 0, "delay cannot be negative");
		return runnable.runTaskLaterAsynchronously(PLUGIN, delay);
	}

	/**
	 * Schedules the given {@link java.lang.Runnable Runnable} to run
	 * <b>asynchronously</b> after the given tick delay elapses. Wraps
	 * {@link org.bukkit.scheduler.BukkitScheduler#runTaskLaterAsynchronously
	 * BukkitScheduler#runTaskLaterAsynchronously()}.
	 *
	 * @param runnable The {@link java.lang.Runnable Runnable} being scheduled.
	 * @param delay    The number of <u>ticks</u> the scheduler should wait before
	 *                 beginning execution of the given {@link java.lang.Runnable
	 *                 Runnable}.
	 *
	 * @return A {@link org.bukkit.scheduler.BukkitTask BukkitTask} representing the
	 *         given {@link java.lang.Runnable Runnable} being executed by the
	 *         scheduler.
	 *
	 * @throws NullPointerException     if the given {@link java.lang.Runnable
	 *                                  Runnable} is null.
	 * @throws IllegalStateException    if the given {@link java.lang.Runnable
	 *                                  Runnable} has already been scheduled.
	 * @throws IllegalArgumentException if the given delay is negative.
	 *
	 * @version 1.0.0
	 * @since 0.0.1
	 */
	public static BukkitTask runDelayedTaskAsynchronously(Runnable runnable, long delay) throws NullPointerException, IllegalStateException, IllegalArgumentException {
		Validate.notNull(runnable, "runnable cannot be null");
		Validate.isTrue(delay >= 0, "delay cannot be negative");
		return PLUGIN.getServer().getScheduler().runTaskLaterAsynchronously(PLUGIN, runnable, delay);
	}

	/**
	 * Schedules the given {@link org.bukkit.scheduler.BukkitRunnable
	 * BukkitRunnable} to run <b>synchronously</b> every tick until manually
	 * cancelled. Wraps {@link org.bukkit.scheduler.BukkitRunnable#runTaskTimer
	 * BukkitRunnable#runTaskTimer()}.
	 *
	 * @param runnable The {@link org.bukkit.scheduler.BukkitRunnable
	 *                 BukkitRunnable} being scheduled.
	 *
	 * @return A {@link org.bukkit.scheduler.BukkitTask BukkitTask} representing the
	 *         given {@link org.bukkit.scheduler.BukkitRunnable BukkitRunnable}
	 *         being executed by the scheduler.
	 *
	 * @throws NullPointerException  if the given
	 *                               {@link org.bukkit.scheduler.BukkitRunnable
	 *                               BukkitRunnable} is null.
	 * @throws IllegalStateException if the given
	 *                               {@link org.bukkit.scheduler.BukkitRunnable
	 *                               BukkitRunnable} has already been scheduled.
	 *
	 * @version 1.0.0
	 * @since 0.0.1
	 */
	public static BukkitTask runRepeatingTask(BukkitRunnable runnable) throws NullPointerException, IllegalStateException {
		Validate.notNull(runnable, "runnable cannot be null");
		return runRepeatingTask(runnable, 0L, 0L);
	}

	/**
	 * Schedules the given {@link java.lang.Runnable Runnable} to run
	 * <b>synchronously</b> every tick until manually cancelled. Wraps
	 * {@link org.bukkit.scheduler.BukkitScheduler#runTaskTimer
	 * BukkitScheduler#runTaskTimer()}.
	 *
	 * @param runnable The {@link java.lang.Runnable Runnable} being scheduled.
	 *
	 * @return A {@link org.bukkit.scheduler.BukkitTask BukkitTask} representing the
	 *         given {@link java.lang.Runnable Runnable} being executed by the
	 *         scheduler.
	 *
	 * @throws NullPointerException  if the given {@link java.lang.Runnable
	 *                               Runnable} is null.
	 * @throws IllegalStateException if the given {@link java.lang.Runnable
	 *                               Runnable} has already been scheduled.
	 *
	 * @version 1.0.0
	 * @since 0.0.1
	 */
	public static BukkitTask runRepeatingTask(Runnable runnable) throws NullPointerException, IllegalStateException {
		Validate.notNull(runnable, "runnable cannot be null");
		return runRepeatingTask(runnable, 0L, 0L);
	}

	/**
	 * Schedules the given {@link org.bukkit.scheduler.BukkitRunnable
	 * BukkitRunnable} to run <b>synchronously</b> after the given tick delay
	 * elapses and then again repeatedly each tick until manually cancelled. Wraps
	 * {@link org.bukkit.scheduler.BukkitRunnable#runTaskTimer
	 * BukkitRunnable#runTaskTimer()}.
	 *
	 * @param runnable The {@link org.bukkit.scheduler.BukkitRunnable
	 *                 BukkitRunnable} being scheduled.
	 * @param delay    The number of <u>ticks</u> the scheduler should wait before
	 *                 beginning execution of the given
	 *                 {@link org.bukkit.scheduler.BukkitRunnable BukkitRunnable}.
	 *
	 * @return A {@link org.bukkit.scheduler.BukkitTask BukkitTask} representing the
	 *         given {@link org.bukkit.scheduler.BukkitRunnable BukkitRunnable}
	 *         being executed by the scheduler.
	 *
	 * @throws NullPointerException     if the given
	 *                                  {@link org.bukkit.scheduler.BukkitRunnable
	 *                                  BukkitRunnable} is null.
	 * @throws IllegalStateException    if the given
	 *                                  {@link org.bukkit.scheduler.BukkitRunnable
	 *                                  BukkitRunnable} has already been scheduled.
	 * @throws IllegalArgumentException if the given delay is negative.
	 *
	 * @version 1.0.0
	 * @since 0.0.1
	 */
	public static BukkitTask runRepeatingTask(BukkitRunnable runnable, long delay) throws NullPointerException, IllegalStateException, IllegalArgumentException {
		Validate.notNull(runnable, "runnable cannot be null");
		Validate.isTrue(delay >= 0, "delay cannot be negative");
		return runRepeatingTask(runnable, delay, 0L);
	}

	/**
	 * Schedules the given {@link java.lang.Runnable Runnable} to run
	 * <b>synchronously</b> after the given tick delay elapses and then again
	 * repeatedly each tick until manually cancelled. Wraps
	 * {@link org.bukkit.scheduler.BukkitScheduler#runTaskTimer
	 * BukkitScheduler#runTaskTimer()}.
	 *
	 * @param runnable The {@link java.lang.Runnable Runnable} being scheduled.
	 * @param delay    The number of <u>ticks</u> the scheduler should wait before
	 *                 beginning execution of the given {@link java.lang.Runnable
	 *                 Runnable}.
	 *
	 * @return A {@link org.bukkit.scheduler.BukkitTask BukkitTask} representing the
	 *         given {@link java.lang.Runnable Runnable} being executed by the
	 *         scheduler.
	 *
	 * @throws NullPointerException     if the given {@link java.lang.Runnable
	 *                                  Runnable} is null.
	 * @throws IllegalStateException    if the given {@link java.lang.Runnable
	 *                                  Runnable} has already been scheduled.
	 * @throws IllegalArgumentException if the given delay is negative.
	 *
	 * @version 1.0.0
	 * @since 0.0.1
	 */
	public static BukkitTask runRepeatingTask(Runnable runnable, long delay) throws NullPointerException, IllegalStateException, IllegalArgumentException {
		Validate.notNull(runnable, "runnable cannot be null");
		Validate.isTrue(delay >= 0, "delay cannot be negative");
		return runRepeatingTask(runnable, delay, 0L);
	}

	/**
	 * Schedules the given {@link org.bukkit.scheduler.BukkitRunnable
	 * BukkitRunnable} to run <b>synchronously</b> after the given tick delay
	 * elapses and then again repeatedly after the given period elapses until
	 * manually cancelled. Wraps
	 * {@link org.bukkit.scheduler.BukkitRunnable#runTaskTimer
	 * BukkitRunnable#runTaskTimer()}.
	 *
	 * @param runnable The {@link org.bukkit.scheduler.BukkitRunnable
	 *                 BukkitRunnable} being scheduled.
	 * @param delay    The number of <u>ticks</u> the scheduler should wait before
	 *                 beginning execution of the given
	 *                 {@link org.bukkit.scheduler.BukkitRunnable BukkitRunnable}.
	 * @param period   The number of <u>ticks</u> the scheduler should wait before
	 *                 each subsequent execution of the given
	 *                 {@link org.bukkit.scheduler.BukkitRunnable BukkitRunnable}.
	 *
	 * @return A {@link org.bukkit.scheduler.BukkitTask BukkitTask} representing the
	 *         given {@link org.bukkit.scheduler.BukkitRunnable BukkitRunnable}
	 *         being executed by the scheduler.
	 *
	 * @throws NullPointerException     if the given
	 *                                  {@link org.bukkit.scheduler.BukkitRunnable
	 *                                  BukkitRunnable} is null.
	 * @throws IllegalStateException    if the given
	 *                                  {@link org.bukkit.scheduler.BukkitRunnable
	 *                                  BukkitRunnable} has already been scheduled.
	 * @throws IllegalArgumentException if the given delay is negative or if the
	 *                                  given period is negative.
	 *
	 * @version 1.0.0
	 * @since 0.0.1
	 */
	public static BukkitTask runRepeatingTask(BukkitRunnable runnable, long delay, long period) throws NullPointerException, IllegalStateException, IllegalArgumentException {
		Validate.notNull(runnable, "runnable cannot be null");
		Validate.isTrue(delay >= 0, "delay cannot be negative");
		Validate.isTrue(period >= 0, "period cannot be negative");
		return runnable.runTaskTimer(PLUGIN, delay, period);
	}

	/**
	 * Schedules the given {@link java.lang.Runnable Runnable} to run
	 * <b>synchronously</b> after the given tick delay elapses and then again
	 * repeatedly after the given period elapses until manually cancelled. Wraps
	 * {@link org.bukkit.scheduler.BukkitScheduler#runTaskTimer
	 * BukkitScheduler#runTaskTimer()}.
	 *
	 * @param runnable The {@link java.lang.Runnable Runnable} being scheduled.
	 * @param delay    The number of <u>ticks</u> the scheduler should wait before
	 *                 beginning execution of the given {@link java.lang.Runnable
	 *                 Runnable}.
	 * @param period   The number of <u>ticks</u> the scheduler should wait before
	 *                 each subsequent execution of the given
	 *                 {@link java.lang.Runnable Runnable}.
	 *
	 * @return A {@link org.bukkit.scheduler.BukkitTask BukkitTask} representing the
	 *         given {@link java.lang.Runnable Runnable} being executed by the
	 *         scheduler.
	 *
	 * @throws NullPointerException     if the given {@link java.lang.Runnable
	 *                                  Runnable} is null.
	 * @throws IllegalStateException    if the given {@link java.lang.Runnable
	 *                                  Runnable} has already been scheduled.
	 * @throws IllegalArgumentException if the given delay is negative or if the
	 *                                  given period is negative.
	 *
	 * @version 1.0.0
	 * @since 0.0.1
	 */
	public static BukkitTask runRepeatingTask(Runnable runnable, long delay, long period) throws NullPointerException, IllegalStateException, IllegalArgumentException {
		Validate.notNull(runnable, "runnable cannot be null");
		Validate.isTrue(delay >= 0, "delay cannot be negative");
		Validate.isTrue(period >= 0, "period cannot be negative");
		return PLUGIN.getServer().getScheduler().runTaskTimer(PLUGIN, runnable, delay, period);
	}

	/**
	 * Schedules the given {@link org.bukkit.scheduler.BukkitRunnable
	 * BukkitRunnable} to run <b>asynchronously</b> every tick until manually
	 * cancelled. Wraps
	 * {@link org.bukkit.scheduler.BukkitRunnable#runTaskTimerAsynchronously
	 * BukkitRunnable#runTaskTimerAsynchronously​()}.
	 *
	 * @param runnable The {@link org.bukkit.scheduler.BukkitRunnable
	 *                 BukkitRunnable} being scheduled.
	 *
	 * @return A {@link org.bukkit.scheduler.BukkitTask BukkitTask} representing the
	 *         given {@link org.bukkit.scheduler.BukkitRunnable BukkitRunnable}
	 *         being executed by the scheduler.
	 *
	 * @throws NullPointerException  if the given
	 *                               {@link org.bukkit.scheduler.BukkitRunnable
	 *                               BukkitRunnable} is null.
	 * @throws IllegalStateException if the given
	 *                               {@link org.bukkit.scheduler.BukkitRunnable
	 *                               BukkitRunnable} has already been scheduled.
	 *
	 * @version 1.0.0
	 * @since 0.0.1
	 */
	public static BukkitTask runRepeatingTaskAsynchronously(BukkitRunnable runnable) throws NullPointerException, IllegalStateException {
		Validate.notNull(runnable, "runnable cannot be null");
		return runRepeatingTaskAsynchronously(runnable, 0L, 0L);
	}

	/**
	 * Schedules the given {@link java.lang.Runnable Runnable} to run
	 * <b>asynchronously</b> every tick until manually cancelled. Wraps
	 * {@link org.bukkit.scheduler.BukkitScheduler#runTaskTimerAsynchronously
	 * BukkitScheduler#runTaskTimerAsynchronously​()}.
	 *
	 * @param runnable The {@link java.lang.Runnable Runnable} being scheduled.
	 *
	 * @return A {@link org.bukkit.scheduler.BukkitTask BukkitTask} representing the
	 *         given {@link java.lang.Runnable Runnable} being executed by the
	 *         scheduler.
	 *
	 * @throws NullPointerException  if the given {@link java.lang.Runnable
	 *                               Runnable} is null.
	 * @throws IllegalStateException if the given {@link java.lang.Runnable
	 *                               Runnable} has already been scheduled.
	 *
	 * @version 1.0.0
	 * @since 0.0.1
	 */
	public static BukkitTask runRepeatingTaskAsynchronously(Runnable runnable) throws NullPointerException, IllegalStateException {
		Validate.notNull(runnable, "runnable cannot be null");
		return runRepeatingTaskAsynchronously(runnable, 0L, 0L);
	}

	/**
	 * Schedules the given {@link org.bukkit.scheduler.BukkitRunnable
	 * BukkitRunnable} to run <b>asynchronously</b> after the given tick delay
	 * elapses and then again repeatedly each tick until manually cancelled. Wraps
	 * {@link org.bukkit.scheduler.BukkitRunnable#runTaskTimerAsynchronously
	 * BukkitRunnable#runTaskTimerAsynchronously​()}.
	 *
	 * @param runnable The {@link org.bukkit.scheduler.BukkitRunnable
	 *                 BukkitRunnable} being scheduled.
	 * @param delay    The number of <u>ticks</u> the scheduler should wait before
	 *                 beginning execution of the given
	 *                 {@link org.bukkit.scheduler.BukkitRunnable BukkitRunnable}.
	 *
	 * @return A {@link org.bukkit.scheduler.BukkitTask BukkitTask} representing the
	 *         given {@link org.bukkit.scheduler.BukkitRunnable BukkitRunnable}
	 *         being executed by the scheduler.
	 *
	 * @throws NullPointerException     if the given
	 *                                  {@link org.bukkit.scheduler.BukkitRunnable
	 *                                  BukkitRunnable} is null.
	 * @throws IllegalStateException    if the given
	 *                                  {@link org.bukkit.scheduler.BukkitRunnable
	 *                                  BukkitRunnable} has already been scheduled.
	 * @throws IllegalArgumentException if the given delay is negative.
	 *
	 * @version 1.0.0
	 * @since 0.0.1
	 */
	public static BukkitTask runRepeatingTaskAsynchronously(BukkitRunnable runnable, long delay) throws NullPointerException, IllegalStateException, IllegalArgumentException {
		Validate.notNull(runnable, "runnable cannot be null");
		Validate.isTrue(delay >= 0, "delay cannot be negative");
		return runRepeatingTaskAsynchronously(runnable, delay, 0L);
	}

	/**
	 * Schedules the given {@link java.lang.Runnable Runnable} to run
	 * <b>asynchronously</b> after the given tick delay elapses and then again
	 * repeatedly each tick until manually cancelled. Wraps
	 * {@link org.bukkit.scheduler.BukkitScheduler#runTaskTimerAsynchronously
	 * BukkitScheduler#runTaskTimerAsynchronously​()}.
	 *
	 * @param runnable The {@link java.lang.Runnable Runnable} being scheduled.
	 * @param delay    The number of <u>ticks</u> the scheduler should wait before
	 *                 beginning execution of the given {@link java.lang.Runnable
	 *                 Runnable}.
	 *
	 * @return A {@link org.bukkit.scheduler.BukkitTask BukkitTask} representing the
	 *         given {@link java.lang.Runnable Runnable} being executed by the
	 *         scheduler.
	 *
	 * @throws NullPointerException     if the given {@link java.lang.Runnable
	 *                                  Runnable} is null.
	 * @throws IllegalStateException    if the given {@link java.lang.Runnable
	 *                                  Runnable} has already been scheduled.
	 * @throws IllegalArgumentException if the given delay is negative.
	 *
	 * @version 1.0.0
	 * @since 0.0.1
	 */
	public static BukkitTask runRepeatingTaskAsynchronously(Runnable runnable, long delay) throws NullPointerException, IllegalStateException, IllegalArgumentException {
		Validate.notNull(runnable, "runnable cannot be null");
		Validate.isTrue(delay >= 0, "delay cannot be negative");
		return runRepeatingTaskAsynchronously(runnable, delay, 0L);
	}

	/**
	 * Schedules the given {@link org.bukkit.scheduler.BukkitRunnable
	 * BukkitRunnable} to run <b>asynchronously</b> after the given tick delay
	 * elapses and then again repeatedly after the given period elapses until
	 * manually cancelled. Wraps
	 * {@link org.bukkit.scheduler.BukkitRunnable#runTaskTimerAsynchronously
	 * BukkitRunnable#runTaskTimerAsynchronously​()}.
	 *
	 * @param runnable The {@link org.bukkit.scheduler.BukkitRunnable
	 *                 BukkitRunnable} being scheduled.
	 * @param delay    The number of <u>ticks</u> the scheduler should wait before
	 *                 beginning execution of the given
	 *                 {@link org.bukkit.scheduler.BukkitRunnable BukkitRunnable}.
	 * @param period   The number of <u>ticks</u> the scheduler should wait before
	 *                 each subsequent execution of the given
	 *                 {@link org.bukkit.scheduler.BukkitRunnable BukkitRunnable}.
	 *
	 * @return A {@link org.bukkit.scheduler.BukkitTask BukkitTask} representing the
	 *         given {@link org.bukkit.scheduler.BukkitRunnable BukkitRunnable}
	 *         being executed by the scheduler.
	 *
	 * @throws NullPointerException     if the given
	 *                                  {@link org.bukkit.scheduler.BukkitRunnable
	 *                                  BukkitRunnable} is null.
	 * @throws IllegalStateException    if the given
	 *                                  {@link org.bukkit.scheduler.BukkitRunnable
	 *                                  BukkitRunnable} has already been scheduled.
	 * @throws IllegalArgumentException if the given delay is negative or if the
	 *                                  given period is negative.
	 *
	 * @version 1.0.0
	 * @since 0.0.1
	 */
	public static BukkitTask runRepeatingTaskAsynchronously(BukkitRunnable runnable, long delay, long period) throws NullPointerException, IllegalStateException, IllegalArgumentException {
		Validate.notNull(runnable, "runnable cannot be null");
		Validate.isTrue(delay >= 0, "delay cannot be negative");
		Validate.isTrue(period >= 0, "period cannot be negative");
		return runnable.runTaskTimerAsynchronously(PLUGIN, delay, period);
	}

	/**
	 * Schedules the given {@link java.lang.Runnable Runnable} to run
	 * <b>asynchronously</b> after the given tick delay elapses and then again
	 * repeatedly after the given period elapses until manually cancelled. Wraps
	 * {@link org.bukkit.scheduler.BukkitScheduler#runTaskTimerAsynchronously
	 * BukkitScheduler#runTaskTimerAsynchronously​()}.
	 *
	 * @param runnable The {@link java.lang.Runnable Runnable} being scheduled.
	 * @param delay    The number of <u>ticks</u> the scheduler should wait before
	 *                 beginning execution of the given {@link java.lang.Runnable
	 *                 Runnable}.
	 * @param period   The number of <u>ticks</u> the scheduler should wait before
	 *                 each subsequent execution of the given
	 *                 {@link java.lang.Runnable Runnable}.
	 *
	 * @return A {@link org.bukkit.scheduler.BukkitTask BukkitTask} representing the
	 *         given {@link java.lang.Runnable Runnable} being executed by the
	 *         scheduler.
	 *
	 * @throws NullPointerException     if the given {@link java.lang.Runnable
	 *                                  Runnable} is null.
	 * @throws IllegalStateException    if the given {@link java.lang.Runnable
	 *                                  Runnable} has already been scheduled.
	 * @throws IllegalArgumentException if the given delay is negative or if the
	 *                                  given period is negative.
	 *
	 * @version 1.0.0
	 * @since 0.0.1
	 */
	public static BukkitTask runRepeatingTaskAsynchronously(Runnable runnable, long delay, long period) throws NullPointerException, IllegalStateException, IllegalArgumentException {
		Validate.notNull(runnable, "runnable cannot be null");
		Validate.isTrue(delay >= 0, "delay cannot be negative");
		Validate.isTrue(period >= 0, "period cannot be negative");
		return PLUGIN.getServer().getScheduler().runTaskTimerAsynchronously(PLUGIN, runnable, delay, period);
	}
}