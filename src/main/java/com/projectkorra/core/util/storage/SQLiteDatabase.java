package com.projectkorra.core.util.storage;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.projectkorra.core.ProjectKorra;

import org.bukkit.plugin.java.JavaPlugin;

public class SQLiteDatabase implements Database {

	private File file;

	public SQLiteDatabase(File file) {
		this.file = file;

	}

	@Override
	public Connection connect() {
		if (!this.file.getParentFile().exists()) {
			this.file.mkdirs();
		}

		if (!this.file.exists()) {
			try {
				this.file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		try {
			Class.forName("org.sqlite.JDBC");

			Connection connection = DriverManager.getConnection("jdbc:sqlite:" + this.file.getAbsolutePath());
			JavaPlugin.getPlugin(ProjectKorra.class).getLogger().info("Database connected!");

			return connection;
		} catch (final ClassNotFoundException e) {
			JavaPlugin.getPlugin(ProjectKorra.class).getLogger().warning("JDBC driver not found!");
			return null;
		} catch (final SQLException e) {
			JavaPlugin.getPlugin(ProjectKorra.class).getLogger().warning("SQLite exception during connection.");
			return null;
		}
	}

}
