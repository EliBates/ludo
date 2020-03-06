package game.server;

import game.server.net.Connection;
import game.server.net.Listener;
import game.server.service.GameManager;

import java.util.Vector;

/**
 * @author Eli / Jeff
 * The game server it the parent class to the game manager, it handles all incoming/outgoing message traffic for the clients
 * Stores a list of player clients conneccted... for right now this is just 1 because its local only... but can easily be modified to allow others from the network to play.
 */
public class GameServer extends Thread implements Runnable{

    public boolean isRunning = false, needsSetup = true;

    //The game manager instance
    private GameManager gameManager;

    //the list of client connections
    private Vector<Connection> playerClients = new Vector<>();

    //set to false to stop accepting new connections (player limit reached)
    public boolean acceptingNewConnections = true;

    //Listener instance
    private Listener listener;

    //saves the setup string for restarting the game with same settings
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

    /**
     * Handles incoming packeets to the server
     * @param c connection sending packet
     * @param packet packet data as string
     */
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
                    gameManager.getPlayerManager().handlePlayerMoveIntent(gameManager.getPlayerManager().getActivePlayer(), tileId);
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
                        c.sendMessage("score" + gameManager.getPlayerManager().getScoreData());
                    }
                    gameManager.requestClientUpdate = false;
                }
            }
            gameManager.isRunning = false;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //initialize the game manager and the conn. listener
    private void initialize() {
        listener = new Listener(this, 43594);
        listener.start();
        isRunning = true;
        gameManager = new GameManager(this);
    }

    //shutdown the game server, kill all connectioons
    public void shutdown() {
        for (Connection c : playerClients) {
            c.dispose();
        }
        listener.dispose();
        isRunning = false;
    }

    //restart the game server
    public void restart() {
        gameManager.isRunning = false;
        gameManager = new GameManager(this);
        gameManager.buildPlayers(setupString);
        gameManager.start();
    }
}
