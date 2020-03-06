package game.server.service;

import game.server.GameServer;
import game.server.component.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.IntStream;

/**
 * @author Eli
 * Handles the entire management of the game, creation, shutting down and all playerManager/tileManager services
 */
public class GameManager extends Thread {

    //The Tile manager instance
    private TileManager tileManager;

    //The game Server reference
    private GameServer gameServer;

    //The player Manager instance
    private PlayerManager playerManager;

    //When this is set to true the client will be pushed an update
    public boolean requestClientUpdate = false;

    //determines if the gamemanager needs to continue to run
    public boolean isRunning = true;

    public GameManager(GameServer gameServer) {
        this.gameServer = gameServer;
        tileManager = new TileManager();
        playerManager = new PlayerManager(this);
    }

    /**
     * builds all the players with the command sent from the client
     * @param playerData the data containing what players to make
     */
    public void buildPlayers(String playerData) { //TODO add connections later for multiplayer  TODO move this call to the packet manager and add this method in playermanager
        String[] info = playerData.split(":"); // divide all the players up
        playerManager.turnOrder = new int[info.length];
        Player[] players = new Player[info.length];
        for (int i = 0; i < info.length; i++) { // iterate through each players info
            String[] buildData = info[i].split(","); // divide the players info up
            System.out.println("Building player ID: " + Integer.parseInt(buildData[0]) + " Name: " +  buildData[1] + " Type: " + buildData[2] );
            Player player = new Player(
                    Integer.parseInt(buildData[0]), // color (playerID)
                    buildData[1],                   // player name
                    Integer.parseInt(buildData[2])  // player type (Human / AI)
            );
            players[player.getId()] = player;
            playerManager.turnOrder[player.getId()] = player.getId();
            //playerManager.addPlayer(player, tileManager);
        }
        Arrays.sort(playerManager.turnOrder); // sort the players from low-high
        Arrays.stream(players).forEach(p -> playerManager.addPlayer(p, tileManager)); // add each player to the player manager
    }

    @Override
    public void run() {
        while (isRunning || gameServer.isAlive()) {
            if (!playerManager.conductingTurn) {
                playerManager.nextTurn();
            } else {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    protected TileManager getTileManager() {
        return tileManager;
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }
}
