package game.server;

import game.server.io.Connection;
import game.server.io.Listener;
import game.server.service.GameManager;

import java.util.Vector;

public class GameServer extends Thread implements Runnable{

    private boolean isRunning = false, needsSetup = true;

    private GameManager gameManager;

    private Vector<Connection> playerClients = new Vector<>();

    public boolean acceptingNewConnections = true;

    public GameServer() {
        //TODO all the networking stuff later
        initialize();
        start();
    }

    public boolean needsSetup() {
        return needsSetup;
    }

    public void addConnection(Connection connection) {
        playerClients.add(connection);
    }

    public void processPacket(Connection c, String packet) { //TODO handle packets by connection instance for multiplayer
        //System.out.println(packet);

        if (packet.startsWith("setup") && needsSetup) {
            gameManager.buildPlayers(packet.substring(packet.indexOf(':') +1));
            gameManager.start();
            acceptingNewConnections = false;
        } else {
            if (packet.startsWith("click")) {
                int tileId = Integer.parseInt(packet.substring(packet.indexOf(':') + 1));
                gameManager.getPlayerManager().handleMoveIntent(gameManager.getPlayerManager().getActivePlayer(), tileId);
            }
            if (packet.startsWith("dice")) {
                gameManager.getPlayerManager().conductRoll();
            }
        }
    }

    @Override
    public void run() {
        try {
            while (isRunning) {
                sleep(100);
                if (gameManager.requestClientUpdate) { // update all the clients
                    for (Connection c : playerClients) {
                        c.sendMessage(gameManager.getPlayerManager().getPlayerData());
                    }
                    gameManager.requestClientUpdate = false;
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void initialize() {
        Listener listener = new Listener(this, 43594);
        listener.start();
        isRunning = true;
        gameManager = new GameManager();
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
}
