package game.server.service;

import game.server.environment.Player;

public class GameManager extends Thread {

    private TileManager tileManager;

    private PlayerManager playerManager;

    public boolean requestClientUpdate = false;

    public GameManager() {
        tileManager = new TileManager();
        playerManager = new PlayerManager(this);
    }

    public void buildPlayers(String playerData) { //TODO add connection later for multiplayer
        String[] info = playerData.split(":"); // divide all the players up
        for (String playerInfo : info) { // iterate through each players info
            String[] buildData = playerInfo.split(","); // divide the players info up
            System.out.println("Building player ID: " + Integer.parseInt(buildData[0]) + " Name: " +  buildData[1] + " Type: " + buildData[2] );
            playerManager.addPlayer(
                    new Player(
                            Integer.parseInt(buildData[0]), // color (playerID)
                            buildData[1],                   // player name
                            Integer.parseInt(buildData[2])  // player type (Human / AI)
                    ),
                    tileManager
            );
        }
    }

    @Override
    public synchronized void start() {
        super.start();
    }

    @Override
    public void run() {
        super.run();
        while (true) {
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
