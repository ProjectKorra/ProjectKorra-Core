package com.projectkorra.core.system.board;

import java.util.Optional;

import org.bukkit.ChatColor;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public final class BoardSlot {

    private Scoreboard board;
    private Objective obj;
    private int slot;
    private Team team;
    private String entry;
    private Optional<BoardSlot> next = Optional.empty();

    public BoardSlot(Scoreboard board, Objective obj, int slot) {
        this.board = board;
        this.obj = obj;
        this.slot = slot + 1;
        this.team = board.registerNewTeam("slot" + this.slot);
        this.entry = ChatColor.values()[slot % 10].asBungee() + "" + ChatColor.values()[slot % 16].asBungee();
        team.addEntry(entry);
    }

    private void update() {
        obj.getScore(entry).setScore(-slot);
    }

    public void update(String prefix, String display) {
        team.setPrefix(prefix);
        team.setSuffix(display);
        update();
    }

    public void updatePrefix(String prefix) {
        team.setPrefix(prefix);
        update();
    }

    public void updateDisplay(String display) {
        team.setSuffix(display);
        update();
    }

    public void setSlot(int slot) {
        this.slot = slot + 1;
        update();
    }

    public void decrementSlot() {
        this.setSlot(--slot);
        next.ifPresent(BoardSlot::decrementSlot);
    }

    public void clear() {
        board.resetScores(entry);
        team.unregister();
        next.ifPresent(BoardSlot::decrementSlot);
    }

    void setNext(BoardSlot slot) {
        this.next = Optional.ofNullable(slot);
    }
}