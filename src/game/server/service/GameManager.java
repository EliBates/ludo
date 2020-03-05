package game.server.service;

import game.server.GameServer;
import game.server.component.Player;

public class GameManager extends Thread {

    private TileManager tileManager;

    private GameServer gameServer;

    private PlayerManager playerManager;

    public boolean requestClientUpdate = false;

    public boolean isRunning = true;

    public GameManager(GameServer gameServer) {
        this.gameServer = gameServer;
        tileManager = new TileManager();
        playerManager = new PlayerManager(this);
    }

    public void buildPlayers(String playerData) { //TODO add connection later for multiplayer  TODO move this call to the packet manager and add this method in playermanager
        String[] info = playerData.split(":"); // divide all the players up
        playerManager.turnOrder = new int[info.length];
        for (int i = 0; i < info.length; i++) { // iterate through each players info
            String[] buildData = info[i].split(","); // divide the players info up
            System.out.println("Building player ID: " + Integer.parseInt(buildData[0]) + " Name: " +  buildData[1] + " Type: " + buildData[2] );
            Player player = new Player(
                    Integer.parseInt(buildData[0]), // color (playerID)
                    buildData[1],                   // player name
                    1
//                   Integer.parseInt(buildData[2])  // player type (Human / AI)
            );
            playerManager.turnOrder[i] = player.getId();
            playerManager.addPlayer(player, tileManager);
        }
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
