package game.server;

import game.server.net.Connection;
import game.server.net.Listener;
import game.server.service.GameManager;

import java.util.Vector;

public class GameServer extends Thread implements Runnable{

    public boolean isRunning = false, needsSetup = true;

    private GameManager gameManager;

    private Vector<Connection> playerClients = new Vector<>();

    public boolean acceptingNewConnections = true;

    private Listener listener;

    private String setupString;

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
            setupString = packet.substring(packet.indexOf(':') +1);
            gameManager.buildPlayers(setupString);
            gameManager.start();
            acceptingNewConnections = false;
        } else {
            if (packet.startsWith("click")) {
                int tileId = Integer.parseInt(packet.substring(packet.indexOf(':') + 1));
                if (!gameManager.getPlayerManager().computerMoving) // the computer AI is moving not the player
                    gameManager.getPlayerManager().handleMoveIntent(gameManager.getPlayerManager().getActivePlayer(), tileId);
            }
            if (packet.startsWith("dice")) {
                if (!gameManager.getPlayerManager().computerMoving) // the computer AI is rolling not the player
                    gameManager.getPlayerManager().conductRoll();
            }
            if (packet.startsWith("shutdown")) {
                shutdown();
            }
            if (packet.startsWith("restart")) {
                restart();
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
                        c.sendMessage("active" + gameManager.getPlayerManager().getActivePlayer().getId());
                        c.sendMessage("roll" + gameManager.getPlayerManager().getActiveDiceRoll());
                        c.sendMessage("name" + gameManager.getPlayerManager().getPlayerNames());
                    }
                    gameManager.requestClientUpdate = false;
                }
            }
            gameManager.isRunning = false;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void initialize() {
        listener = new Listener(this, 43594);
        listener.start();
        isRunning = true;
        gameManager = new GameManager(this);
    }

    public void shutdown() {
        for (Connection c : playerClients) {
            c.dispose();
        }
        listener.dispose();
        isRunning = false;
    }

    public void restart() {
        gameManager.isRunning = false;
        gameManager = new GameManager(this);
        gameManager.buildPlayers(setupString);
        gameManager.start();
    }
}
