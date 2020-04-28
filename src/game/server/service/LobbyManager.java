package game.server.service;

import game.server.GameServer;
import game.server.component.LobbySlot;
import game.server.net.Connection;

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
    }

    public LobbyManager(GameServer gameServer) {
        this.gameServer = gameServer;
        for (int i = 0; i < 4; i++) { //init the slots
            players[i] = new LobbySlot(null, i, true);
        }
    }

    public void setName(Connection c, String name) {
        c.getLobbyOptions().setName(name);
        gameServer.sendLobbyOptions();
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

    /**
     * Disable a user
     * @param c
     * @param id
     */
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
