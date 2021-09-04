package com.projectkorra.core.system.board;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import com.projectkorra.core.system.ability.Ability;
import com.projectkorra.core.system.ability.Cooldown;
import com.projectkorra.core.system.entity.PlayerUser;
import com.projectkorra.core.system.entity.UserManager;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import net.md_5.bungee.api.ChatColor;

public class BendingBoard {

    private static final Function<Ability, String> COOLDOWN = (ability) -> ChatColor.STRIKETHROUGH + ability.getName();
    private static final Map<String, Trackable> TRACKING = new HashMap<>();
    private static final Map<PlayerUser, BendingBoard> CACHE = new HashMap<>(50);

    //configurable stuff
    private static final String PREFIX = "> ";
    private static final ChatColor MAIN = ChatColor.WHITE, ALT = ChatColor.DARK_GRAY;
    private static final String EMPTY_SLOT = ChatColor.DARK_GRAY + "Empty Slot";
    private static final String TITLE = ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "Abilities";
    private static final String MISC_SEPARATOR = "- Miscellaneous -";
    
    private PlayerUser user;
    private Scoreboard board;
    private Objective obj;
    private int activeSlot;

    private BoardSlot[] binds = new BoardSlot[9];
    private Map<String, BoardSlot> misc = new HashMap<>();
    private BoardSlot miscTail = null;

    private BendingBoard(PlayerUser user) {
        this.user = user;
        makeBoard();

        for (int i = 0; i < 9; ++i) {
            binds[i] = new BoardSlot(board, obj, i);
        }
    }

    public void update() {

    }

    public void updateActive() {
        updateActive(user.getCurrentSlot());
    }

    public void updateActive(int updateSlot) {
        if (!isVisible()) {
            return;
        }
        
        binds[activeSlot].updatePrefix(ALT + PREFIX);
        binds[updateSlot].updatePrefix(MAIN + PREFIX);
        activeSlot = updateSlot;
    }

    public void updateAbility(Ability ability, Function<Cooldown, String> cooldown) {
        if (!isVisible() || ability == null || cooldown == null) {
            return;
        }

        StringBuilder builder = new StringBuilder(ability.getDisplayColor().toString());
        

        builder.append(ability.getName());
    }

    public void updateMiscellaneous(String tag) {
        if (!isVisible()) {
            return;
        }

        if (misc.containsKey(tag.toLowerCase())) {
            misc.get(tag.toLowerCase()).clear();
            misc.remove(tag.toLowerCase());

            if (misc.isEmpty()) {
                miscTail = null;
                board.resetScores(MISC_SEPARATOR);
            }
        } else if (TRACKING.containsKey(tag.toLowerCase())) {
            BoardSlot next = new BoardSlot(board, obj, 10 + misc.size());
            if (miscTail != null) {
                miscTail.setNext(next);
            }
            miscTail = next;
            obj.getScore(MISC_SEPARATOR).setScore(-10);
        }
    }

    public void setVisible(boolean visible) {
        user.getEntity().setScoreboard(visible ? board : Bukkit.getScoreboardManager().getMainScoreboard());
    }

    public boolean isVisible() {
        return user.getEntity().getScoreboard().equals(board);
    }

    private void makeBoard() {
        this.board = Bukkit.getScoreboardManager().getNewScoreboard();
        this.obj = board.registerNewObjective("projectkorra", "dummy", TITLE); //config
        this.obj.setDisplaySlot(DisplaySlot.SIDEBAR);
    }

    public static void addTrackable(Trackable trackable) {
        if (trackable == null || trackable.getTag() == null || trackable.getDisplay() == null) {
            return;
        } else if (TRACKING.containsKey(trackable.getTag().toLowerCase())) {
            return;
        }

        TRACKING.put(trackable.getTag().toLowerCase(), trackable);
    }

    public static BendingBoard from(PlayerUser user) {
        return CACHE.computeIfAbsent(user, BendingBoard::newBoard);
    }

    public static BendingBoard from(Player player) {
        return CACHE.computeIfAbsent(UserManager.playerUser(player), BendingBoard::newBoard);
    }

    private static BendingBoard newBoard(PlayerUser user) {
        if (user == null) {
            return null;
        }

        return new BendingBoard(user);
    }
}
