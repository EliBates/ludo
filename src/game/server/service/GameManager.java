package game.server.service;

import game.server.environment.Player;

public class GameManager extends Thread {

    private TileManager tileManager;

    private PlayerManager playerManager;

    public boolean requestClientUpdate = false;

    public GameManager() {
        tileManager = new TileManager();
        Player[] players = new Player[4];
        players[0] = new Player(0, "Eli");
        players[1] = new Player(1, "Jeff");
        players[2] = new Player(2, "Dom");
        players[3] = new Player(3, "Maria");
        for (Player p : players) {
            if (p != null)
                p.initGamePieces(tileManager);
        }
        playerManager = new PlayerManager(players, this);
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
