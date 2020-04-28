package game.server.component;

import game.util.Config;

public class LobbyOptions {

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOptions() {
        return getColor()+":"+getName();
    }

    private int color;
    private String name;

    public LobbyOptions(int color, String name) {
        this.color = color;
        this.name = name;
    }
}
