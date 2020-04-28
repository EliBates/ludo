package game.server.service;

import game.server.GameServer;
import game.server.component.LobbySlot;
import game.server.net.Connection;
import javafx.fxml.FXML;

import java.util.Arrays;

public class LobbyManager {

    //reference to game server
    private GameServer gameServer;

    //This is the array of players in the lobby
    private LobbySlot[] players = new LobbySlot[4];

    public boolean inLobby = true;

    private int getFirstAvailableSlot() {
        return Arrays.stream(players)
                .filter(lobbySlot -> lobbySlot.isEnabled() && lobbySlot.getConnection() == null)
                .findFirst()
                .get()
                .getIndex();
    }

    public void removeSlotForConnection(Connection c) {
        Arrays.stream(players)
                .filter(lobbySlot -> lobbySlot.getConnection().getIndex() == c.getIndex())
                .findFirst()
                .get()
                .setConnection(null);
    }

    private void setPlayerSlot(Connection c, int slot) {
        players[slot].setConnection(c);
        gameServer.acceptingNewConnections = hasAvailableSlots();
    }

    public boolean hasAvailableSlots() {
        return Arrays.stream(players).anyMatch(lobbySlot -> lobbySlot.isEnabled() && lobbySlot.getConnection() == null);
    }

    public int requestSlot(Connection c) {
        if (hasAvailableSlots()) {
            int slot = getFirstAvailableSlot();
            if (slot >= 0 && slot <= 4) {
                setPlayerSlot(c, slot);
                System.out.println(c.getIndex() + " index for connection, has been assigned slot " + slot);
            }
            return slot;
        }
        return -1;
    }

    public void disableSlot(int slot) {
        Connection c = players[slot].getConnection();
        if (c != null)
            gameServer.removeConnection(c);
        setPlayerSlot(null, slot);
        players[slot].setEnabled(false);
        gameServer.acceptingNewConnections = hasAvailableSlots();
    }

    public void enableSlot(int slot) {
        if (slot > 0 && slot < 5) {
            players[slot].setEnabled(true);
        }
        gameServer.acceptingNewConnections = hasAvailableSlots();
    }

    public LobbyManager(GameServer gameServer) {
        this.gameServer = gameServer;
        for (int i = 0; i < 4; i++) { //init the slots
            players[i] = new LobbySlot(null, i, true);
        }
    }

    /**
     * A player setting their own name
     * @param c the client setting name
     * @param name the name to be set
     */
    public void setName(Connection c, String name) {
        c.getLobbyOptions().setName(name);
        sendLobbyOptions();
    }

    /**
     * A player setting their own color
     * @param c the client setting color
     * @param color the color code to set
     */
    public void setColor(Connection c, int slot, int color) {
        Connection change = players[slot].getConnection();
        if (change.getIndex() == c.getIndex()) {
            c.getLobbyOptions().setColor(color);
        } else if (change.getIndex() != c.getIndex() && c.isHost()) {
            change.getLobbyOptions().setColor(color);
        }
        sendLobbyOptions();
    }

    public void startGame(Connection c) {
        if (c.isHost()) {
            gameServer.sendMessage("startgame");
        }
    }

    public void sendLobbyOptions() {
        for (Connection c : gameServer.playerClients) {
            for (Connection c2 : gameServer.playerClients) {
                c.sendMessage("lobbyupdate"+":"+c2.getIndex()+":"+c2.getLobbyOptions().getOptions());
            }
        }
    }



}
