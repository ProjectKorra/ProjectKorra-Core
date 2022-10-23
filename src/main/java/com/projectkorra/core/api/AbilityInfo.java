package com.projectkorra.core.api;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;

import com.projectkorra.core.ProjectKorra;
import com.projectkorra.core.api.skills.Skill;

public abstract class AbilityInfo {
    public final List<Skill> skills;
    public final String author;
    public final String version;
    public final String name;
    public final boolean bindable;
    public final boolean needsMovement;
    public final String configPath;
    public int priority = 1;

    public AbilityInfo(String author, String version, String name, boolean bindable,
            Skill... skills) {
        this(author, version, name, "DEFAULT", bindable, false, skills);
    }

    public AbilityInfo(String author, String version, String name, boolean bindable, boolean needsMovement,
            Skill... skills) {
        this(author, version, name, "DEFAULT", bindable, needsMovement, skills);
    }

    public AbilityInfo(String author, String version, String name, String configPath, boolean bindable,
            boolean needsMovement,
            Skill... skills) {
        this.author = author;
        this.version = version;
        this.name = name;
        this.bindable = bindable;
        this.needsMovement = needsMovement;
        this.skills = Collections.unmodifiableList(Arrays.asList(skills));
        this.configPath = configPath;
        if (AbilityManager.infos.stream().filter(i -> i.name == this.name).toList().size() < 1) {
            AbilityManager.infos.add(this);
            this.load();
        } else {
            // TODO: add appropriate method later
            ProjectKorra.plugin.getLogger().log(Level.SEVERE,
                    this.name + "'s info is already registered! Please use *INSERT APPROPRIATE METHOD");
        }
    }

    public abstract Sequence<AbilityInstance> getActivationSequence();

    public abstract Sequence<List<AbilityInstance>> getActionSequence();

    public abstract void load();

    public abstract AbilityInstance createInstance(User user);

    public boolean needsMovement() {
        return needsMovement;
    }
}
