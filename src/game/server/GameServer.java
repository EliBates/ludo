package game.server;

import game.client.Client;
import game.server.environment.Player;
import game.server.io.Connection;
import game.server.io.Listener;
import game.server.service.GameManager;

import java.util.Vector;

public class GameServer extends Thread implements Runnable{

    private boolean isRunning = false;

    private GameManager gameManager;

    private Client client;

    private Vector<Connection> playerClients = new Vector<>();

    public GameServer() {
        //TODO all the networking stuff later
        initialize();
        start();
    }

    public void addConnection(Connection connection) {
        playerClients.add(connection);
    }

    @Override
    public void run() {
        super.run();
        try {
            while (isRunning) {
                sleep(100);
                if (gameManager.requestClientUpdate) {
                    for (Connection connection : playerClients) {
                        connection.sendMessage("Active Player Is: "+gameManager.getPlayerManager().getActivePlayer().getId() + " and the Dice Roll Was: "+gameManager.getPlayerManager().getActiveDiceRoll());
                    }
                    //client.sendGamePieceUpdate();
                    client.sendRollUpdate(gameManager.getPlayerManager().getActiveDiceRoll());
                    client.sendPlayerUpdate(gameManager.getPlayerManager().getActivePlayer().getId());
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
        Listener listener = new Listener(this, 43594);
        listener.start();
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
