package com.projectkorra.core;

import java.io.File;

import com.projectkorra.core.ability.AbilityManager;
import com.projectkorra.core.command.CommandRegistry;
import com.projectkorra.core.game.airbending.gust.GustAbility;
import com.projectkorra.core.game.firebending.blazingarc.BlazingArcAbility;
import com.projectkorra.core.game.firebending.fireball.FireballAbility;
import com.projectkorra.core.game.firebending.firejet.FireJetAbility;
import com.projectkorra.core.game.firebending.passives.SunlightPassive;
import com.projectkorra.core.game.lightningbending.bolt.BoltAbility;
import com.projectkorra.core.game.lightningbending.redirection.RedirectionPassive;
import com.projectkorra.core.game.physique.landing.LandingPassive;
import com.projectkorra.core.game.physique.wellbeing.WellbeingPassive;
import com.projectkorra.core.listener.ActivationListener;
import com.projectkorra.core.listener.PlayerListener;
import com.projectkorra.core.util.storage.DBConnection;
import com.projectkorra.core.util.storage.SQLiteDatabase;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;

public class ProjectKorra extends JavaPlugin {

	private static DBConnection database;

	@Override
	public void onEnable() {
		AbilityManager.init(this);
		this.setupDatabase();
		CommandRegistry.init(this);
		UserManager.init(this);
		this.registerListeners();
		AbilityManager.register(new GustAbility());
		AbilityManager.register(new FireballAbility());
		AbilityManager.register(new SunlightPassive());
		AbilityManager.register(new FireJetAbility());
		AbilityManager.register(new BlazingArcAbility());
		AbilityManager.register(new BoltAbility());
		AbilityManager.register(new WellbeingPassive());
		AbilityManager.register(new RedirectionPassive());
		AbilityManager.register(new LandingPassive());

		for (Player player : Bukkit.getOnlinePlayers()) {
			UserManager.load(player);
		}
	}

	@Override
	public void onDisable() {
		for (Player player : Bukkit.getOnlinePlayers()) {
			AbilityManager.removeAll(UserManager.get(player));
			UserManager.save(player);
		}
	}

	public ProjectKorra() {
		super();
	}

	protected ProjectKorra(JavaPluginLoader loader, PluginDescriptionFile description, File dataFolder, File file) {
		super(loader, description, dataFolder, file);
	}

	private void registerListeners() {
		this.getServer().getPluginManager().registerEvents(new ActivationListener(), this);
		this.getServer().getPluginManager().registerEvents(new PlayerListener(), this);
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
