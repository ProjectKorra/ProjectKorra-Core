package com.projectkorra.core.util.text;

import net.md_5.bungee.api.ChatColor;

public class DisplayVariant {

	private String noun, verb, user;
	private ChatColor color;

	public DisplayVariant(String noun, String verb, String user, ChatColor color) {
		this.noun = noun;
		this.verb = verb;
		this.user = user;
		this.color = color;
	}

	public String getNoun() {
		return noun;
	}

	public String getVerb() {
		return verb;
	}

	public String getUser() {
		return user;
	}

	public ChatColor getColor() {
		return color;
	}

	public String getColoredNoun() {
		return color + noun;
	}

	public String getColoredVerb() {
		return color + verb;
	}

	public String getColoredUser() {
		return color + user;
	}

	public boolean isValid() {
		return noun != null && verb != null && user != null && color != null;
	}
}
