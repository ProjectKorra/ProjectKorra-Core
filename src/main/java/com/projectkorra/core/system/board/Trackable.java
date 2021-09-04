package com.projectkorra.core.system.board;

public class Trackable {
    
    public String tag, display;
    
    public Trackable(String tag, String display) {
        this.tag = tag;
        this.display = display;
    }

    public String getTag() {
        return tag;
    }

    public String getDisplay() {
        return display;
    }
}
