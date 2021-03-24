package com.projectkorra.core;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;

import org.bukkit.event.player.PlayerJoinEvent;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ProjectKorraTest {
	private ServerMock server;
	private ProjectKorra plugin;

	@BeforeEach
	public void setUp() {
		server = MockBukkit.mock();
		plugin = (ProjectKorra) MockBukkit.load(ProjectKorra.class);
	}

	@Test
	public void testPlayerCanJoin() {
		// This is a test to ensure MockBukkit it set up correctly. In the future, we will utilize this to a greater extent.
		server.addPlayer();
		server.getPluginManager().assertEventFired(PlayerJoinEvent.class);
	}

	@AfterEach
	public void tearDown() {
		MockBukkit.unmock();
	}
}
