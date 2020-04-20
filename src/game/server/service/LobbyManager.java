package game.server.service;

import game.server.GameServer;
import game.server.net.Connection;

public class LobbyManager {

    private GameServer gameServer;

    public LobbyManager(GameServer gameServer) {
        this.gameServer = gameServer;
    }

    public void changeOptions(Connection c, int id, String data) {
        if (c.getIndex() != id && !c.isHost()) // a player other than the host attempted to change another players settings
            return;
        Connection playerToModify = gameServer.getConnection(id);
        if (playerToModify != null) {
            playerToModify.getLobbyOptions().modifyOptions(data);
            updateOptions(playerToModify);
        }
    }

    public void updateOptions(Connection updated) {
        gameServer.sendMessage("optionsupdate:"+updated.getIndex()+":"+updated.getLobbyOptions().getOptions());
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

    public void startGame(Connection c) {
        if (c.isHost()) {
            gameServer.sendMessage("startgame");
        }
    }



}
