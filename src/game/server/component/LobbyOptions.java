package game.server.component;

import game.util.Config;

public class LobbyOptions {

    public boolean updated = false;

    public Config.Color getColor() {
        return color;
    }

    public void setColor(Config.Color color) {
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void modifyOptions(String data) {

    }

    public String getOptions() {
        return getColor()+":"+getName();
    }

    private Config.Color color;
    private String name;

    public LobbyOptions(Config.Color color, String name) {
        this.color = color;
        this.name = name;
    }
}
