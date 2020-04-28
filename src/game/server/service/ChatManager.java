package game.server.service;

import game.server.GameServer;
import game.server.net.Connection;

public class ChatManager {

    private GameServer gameServer;

    public ChatManager(GameServer gameServer) {
        this.gameServer = gameServer;
    }

    public void relayMessage(Connection c, String message) {
        gameServer.sendMessage("chatmessage:"
                + gameServer.getLobbyManager().inLobby + ":" + c.getIndex() + "-> "
                + message);
    }

    public void receiveMessage(Connection c, String message) {
        relayMessage(c, message);
    }
}
