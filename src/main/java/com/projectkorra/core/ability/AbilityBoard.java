package com.projectkorra.core.ability;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.projectkorra.core.UserManager;
import com.projectkorra.core.ability.type.Bindable;
import com.projectkorra.core.entity.PlayerUser;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import net.md_5.bungee.api.ChatColor;

public class AbilityBoard {

	private static Map<PlayerUser, AbilityBoard> CACHE = new HashMap<>();
	private static Map<String, ChatColor> TRACKED = new HashMap<>();

	private static class BoardSlot {

		private Scoreboard board;
		private Objective obj;
		private int slot;
		private Team team;
		private String entry, tag;
		private Optional<BoardSlot> next = Optional.empty(), prev = Optional.empty();

		public BoardSlot(Scoreboard board, Objective obj, int slot) {
			this(board, obj, slot, "slot" + slot);
		}

		@SuppressWarnings("deprecation")
		public BoardSlot(Scoreboard board, Objective obj, int slot, String tag) {
			this.board = board;
			this.obj = obj;
			this.slot = slot + 1;
			this.tag = tag;

			this.team = board.registerNewTeam(this.tag);
			this.entry = ChatColor.values()[slot % 10] + "" + ChatColor.values()[slot % 16];

			team.addEntry(entry);
		}

		private void set() {
			obj.getScore(entry).setScore(-slot);
		}

		public void update(String prefix, String name) {
			team.setPrefix(prefix);
			team.setSuffix(name);
			set();
		}

		public void decreaseSlot() {
			--this.slot;
			board.resetScores(entry);
			this.set();
			prev.ifPresent(b -> {
				if (next.isPresent()) {
					b.setNext(next.get());
				}
			});
			next.ifPresent(BoardSlot::decreaseSlot);
		}

		public void clear() {
			board.resetScores(entry);
			team.unregister();
			prev.ifPresent(b -> {
				if (next.isPresent()) {
					b.setNext(next.get());
				}
			});
			next.ifPresent(BoardSlot::decreaseSlot);
		}

		private void setNext(BoardSlot slot) {
			if (next == null) {
				return;
			}

			this.next = Optional.of(slot);
			slot.prev = Optional.of(this);
		}
	}

	private BoardSlot[] slots = new BoardSlot[9];
	private BoardSlot miscTail = null;
	private Map<String, BoardSlot> misc = new HashMap<>();
	private PlayerUser user;
	private int oldSlot;
	private Scoreboard board;
	private Objective obj;

	private AbilityBoard(PlayerUser user) {
		this.user = user;
		this.oldSlot = user.getCurrentSlot();
		this.board = Bukkit.getScoreboardManager().getNewScoreboard();
		this.obj = board.registerNewObjective("pk_abilityboard", "dummy", "Ability Binds");
		this.obj.setDisplaySlot(DisplaySlot.SIDEBAR);
		for (int i = 0; i < 9; ++i) {
			slots[i] = new BoardSlot(board, obj, i);
		}
	}

	public void hide() {
		user.getEntity().setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
	}

	public void show() {
		user.getEntity().setScoreboard(board);
		this.update();
	}

	public AbilityBoard update() {
		int i = 0;
		for (Ability bind : user.getBinds()) {
			updateBind(i++, bind);
		}

		return this;
	}

	public void updateBind(int slot, Ability ability) {
		ChatColor color = slot == oldSlot ? ChatColor.WHITE : ChatColor.DARK_GRAY;
		slots[slot].update(color + "> ", ability == null ? color + "empty" : ability.getDisplay());
		if (ability != null && user.hasCooldown(ability)) {
			bindCooldown(slot, true);
		}
	}

	public void switchSlot(int newSlot) {
		int slot = oldSlot;
		oldSlot = newSlot;
		updateBind(slot, user.getBoundAbility(slot).orElse(null));
		updateBind(newSlot, user.getBoundAbility(newSlot).orElse(null));
	}

	public void cooldown(String tag, boolean added) {
		Optional<Ability> ability = AbilityManager.getAbility(tag);

		if (ability.isPresent()) {
			if (ability.get() instanceof Bindable) {
				for (int slot : user.getBinds().slotsOf(ability.get())) {
					bindCooldown(slot, added);
				}
				return;
			}
		}

		miscCooldown(tag, added);
	}

	public void bindCooldown(int slot, boolean added) {
		user.getBinds().get(slot).ifPresent((ability) -> {
			slots[slot].team.setSuffix(added ? ability.getDisplayColor() + (ChatColor.STRIKETHROUGH + ability.getName()) : ability.getDisplay());
		});
	}

	public void miscCooldown(String tag, boolean added) {
		if (added) {
			if (misc.containsKey(tag)) {
				return;
			}

			BoardSlot slot = new BoardSlot(board, obj, misc.size() + 10, tag);
			misc.put(tag, slot);
			ChatColor color = TRACKED.getOrDefault(tag, ChatColor.WHITE);
			if (AbilityManager.getAbility(tag).isPresent()) {
				color = AbilityManager.getAbility(tag).get().getDisplayColor();
			}

			if (miscTail != null) {
				miscTail.setNext(slot);
			}

			obj.getScore("-- Extras --").setScore(-10);
			slot.team.setSuffix(color + "" + ChatColor.STRIKETHROUGH + tag);
			slot.set();
		} else if (misc.containsKey(tag)) {
			if (miscTail == misc.get(tag) && misc.get(tag).prev.isPresent()) {
				miscTail = misc.get(tag).prev.get();
			}

			misc.get(tag).clear();
			misc.remove(tag);
			if (misc.isEmpty()) {
				board.resetScores("-- Extras --");
			}
		}
	}

	public static Optional<AbilityBoard> from(PlayerUser player) {
		// return empty if disabled or world is disabled
		return Optional.of(CACHE.computeIfAbsent(player, AbilityBoard::new).update());
	}

	public static Optional<AbilityBoard> from(Player player) {
		if (UserManager.from(player) == null) {
			return Optional.empty();
		}

		return from(UserManager.from(player).getAs(PlayerUser.class));
	}
}
