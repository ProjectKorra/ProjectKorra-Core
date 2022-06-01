package com.projectkorra.core;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;

import com.projectkorra.core.ability.AbilityManager;
import com.projectkorra.core.collision.CollisionManager;
import com.projectkorra.core.command.CommandRegistry;
import com.projectkorra.core.game.BendingBlocks;
import com.projectkorra.core.listener.ActivationListener;
import com.projectkorra.core.listener.PlayerListener;
import com.projectkorra.core.listener.TempListener;
import com.projectkorra.core.temporary.TempBlock;
import com.projectkorra.core.util.storage.DBConnection;
import com.projectkorra.core.util.storage.SQLiteDatabase;

public class ProjectKorra extends JavaPlugin {

	private static DBConnection database;
	private static ProjectKorra instance;

	@Override
	public void onEnable() {
		instance = this;
		AbilityManager.init(this);
		CollisionManager.init(this);
		TempBlock.init(this);
		this.setupDatabase();
		CommandRegistry.init(this);
		UserManager.init(this);
		this.registerListeners();

		BendingBlocks.init(this);
		AbilityManager.registerFrom(this, "com.projectkorra.core.game");

		for (Player player : Bukkit.getOnlinePlayers()) {
			UserManager.load(player);
		}
	}

	@Override
	public void onDisable() {
		TempBlock.clear();
		for (Player player : Bukkit.getOnlinePlayers()) {
			AbilityManager.removeAll(UserManager.from(player));
			UserManager.save(player);
		}
	}

	public ProjectKorra() {
		super();
	}

	protected ProjectKorra(JavaPluginLoader loader, PluginDescriptionFile description, File dataFolder, File file) {
		super(loader, description, dataFolder, file);
	}

	public static ProjectKorra plugin() {
		return instance;
	}

	public static void messageConsole(String msg) {
		instance.getLogger().info(msg);
	}

	public static void warnConsole(String msg) {
		instance.getLogger().warning(msg);
	}

	private void registerListeners() {
		this.getServer().getPluginManager().registerEvents(new ActivationListener(), this);
		this.getServer().getPluginManager().registerEvents(new PlayerListener(), this);
		this.getServer().getPluginManager().registerEvents(new TempListener(), this);
	}

	private void setupDatabase() {
		database = new DBConnection(new SQLiteDatabase(new File(this.getDataFolder(), "storage.db")));
		database.modify("CREATE TABLE IF NOT EXISTS t_pk_player (uuid TEXT)");
		database.modify("CREATE TABLE IF NOT EXISTS t_pk_player_skills (uuid TEXT, skill_name TEXT, toggled NUMBER, PRIMARY KEY (uuid, skill_name))");
		database.modify("CREATE TABLE IF NOT EXISTS t_pk_player_binds (uuid TEXT, bound_slot NUMBER, ability_name TEXT, PRIMARY KEY (uuid, bound_slot))");
	}

	public static DBConnection database() {
		return database;
	}
}
