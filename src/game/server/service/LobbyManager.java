package game.server.service;

import game.server.GameServer;
import game.server.net.Connection;

public class LobbyManager {

    private GameServer gameServer;

    public LobbyManager(GameServer gameServer) {
        this.gameServer = gameServer;
    }

    public void changeColor(Connection c, String color) {
        gameServer.sendMessage("changecolor:" + c.getIndex() + ":" + color);
    }

    public void changeName(Connection c, String name) {
        gameServer.sendMessage("changename:" + c.getIndex() + ":" + name);
    }

    public void changePlayerType(Connection c, int id, int type) {
        if (c.isHost()) {
            gameServer.sendMessage("changetype:" + id + ":" + type);
        }
    }

    public void disable(Connection c, int id) {
        if (c.isHost()) {
            gameServer.sendMessage("disable:" + id);

        }
    }

    public void enable(Connection c, int id) {
        if (c.isHost()) {
            gameServer.sendMessage("enable:" + id);
        }
    }



}
