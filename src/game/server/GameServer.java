package game.server;

import game.client.Client;
import game.server.service.GameManager;

public class GameServer extends Thread implements Runnable{

    private boolean isRunning = false;

    private GameManager gameManager;

    private Client client;

    public GameServer() {
        //TODO all the networking stuff later
        initialize();
        start();
    }

    @Override
    public void run() {
        super.run();
        try {
            while (isRunning) {
                sleep(100);
                if (gameManager.requestClientUpdate) {
                    client.sendGamePieceUpdate();
                    client.sendRollUpdate(gameManager.getPlayerManager().getActiveDiceRoll());
                    client.sendPlayerUpdate(gameManager.getPlayerManager().getActivePlayer().getId());
                    //System.out.println("Sending client update!");
                    gameManager.requestClientUpdate = false;
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void recieveTileClick(int tileId) {
        //System.out.println("Recieved click: " +tileId);
        gameManager.getPlayerManager().handleMoveIntent(gameManager.getPlayerManager().getActivePlayer(), tileId);
    }

    public void recieveRollClick() {
        gameManager.getPlayerManager().conductRoll();
    }

    public String sendPieceInfo() {
        return gameManager.getPlayerManager().getPlayerData();
    }

    public void acceptClientConnection(Client c) {
        this.client = c;
    }

    private void initialize() {
        isRunning = true;
        gameManager = new GameManager();
        gameManager.start();
        System.out.println("Game is running");
    }

    private void process() {
        while (isRunning) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void shutdown() {
        isRunning = false;
    }

    public void restart() {
        shutdown();
        initialize();
    }

    public Client getClient() {
        return client;
    }
}
