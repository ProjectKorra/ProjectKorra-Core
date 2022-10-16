package com.projectkorra.core.game;

import com.projectkorra.core.skill.Skill;

public class AvatarSkills {

	public static final Skill AIRBENDING = Skill.of("airbending");                 // ChatColor.of("#fcdc7b"));
	public static final Skill EARTHBENDING = Skill.of("eartbending");              // ChatColor.of("#47b44f"));
	public static final Skill FIREBENDING = Skill.of("firebending");               // ChatColor.of("#a10000"));
	public static final Skill WATERBENDING = Skill.of("waterbending");             // ChatColor.of("#509bcd"));

	public static final Skill CHIBLOCKING = Skill.of("chiblocking");               // ChatColor.GRAY);
	public static final Skill ENERGYBENDING = Skill.of("energybending");           // ChatColor.DARK_PURPLE);

	public static final Skill FLYING = Skill.of("flying");                          // ChatColor.GRAY, AIRBENDING);
	public static final Skill SPIRITUAL = Skill.of("spiritualism");                 // ChatColor.of("#fff2cc"), AIRBENDING);

	public static final Skill LAVABENDING = Skill.of("lavabending");               // ChatColor.of("#f74900"), EARTHBENDING);
	public static final Skill METALBENDING = Skill.of("metalbending");             // ChatColor.of("#999999"), EARTHBENDING);
	public static final Skill SANDBENDING = Skill.of("sandbending");               // ChatColor.of("#ffe599"), EARTHBENDING);

	public static final Skill COMBUSTIONBENDING = Skill.of("combustionbending");  // ChatColor.DARK_RED, FIREBENDING);
	public static final Skill LIGHTNINGBENDING = Skill.of("lightningbending");    // ChatColor.of("#71d9de"), FIREBENDING);
	public static final Skill BLUEFIREBENDING = Skill.of("bluefirebending");      // ChatColor.BLUE, FIREBENDING);

	public static final Skill BLOODBENDING = Skill.of("bloodbending");            // ChatColor.of("#e06666"), WATERBENDING);
	public static final Skill SPIRITWATERS = Skill.of("spiritwaters");            // ChatColor.of("#00ffff"), WATERBENDING);
	public static final Skill PLANTBENDING = Skill.of("plantbending");            // ChatColor.of("#93c47d"), WATERBENDING);

	public static final Skill MUDBENDING = Skill.of("mudbending");                // ChatColor.of("#783f04"), EARTHBENDING, WATERBENDING);
	public static final Skill PHYSIQUE = Skill.of("physique");                    // ChatColor.of("#a64d79"), AIRBENDING, EARTHBENDING, FIREBENDING, WATERBENDING);

}
