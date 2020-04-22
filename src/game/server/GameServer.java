package game.server;

import game.server.net.Connection;
import game.server.net.Listener;
import game.server.service.GameManager;
import game.server.service.LobbyManager;
import game.util.Config;

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

    private LobbyManager lobbyManager;

    //the list of client connections
    private Vector<Connection> playerClients = new Vector<>();

    //set to false to stop accepting new connections (player limit reached)
    public boolean acceptingNewConnections = true;

    //Listener instance
    private Listener listener;

    //saves the setup string for restarting the game with same settings
    private String setupString;

    private Config.GameType gameType = Config.GameType.UNASSIGNED;

    public GameServer() {
        initialize();
        start();
    }

    public int totalConnections() {
        return playerClients.size();
    }

    public Connection getConnection(int index) {
        return playerClients.get(index);
    }

    public void addConnection(Connection connection) {
        System.out.println("Added a new connection to the server! ID: " + connection.getIndex());
        playerClients.add(connection);
    }

    public void removeConnection(Connection connection) {
        System.out.println("Connection ID: " + connection.getIndex() + " removed from the list");
        playerClients.remove(connection);
    }

    /**
     * Handles incoming packets to the server
     * @param c connection sending packet
     * @param packet packet data as string
     */
    public void processPacket(Connection c, String packet) { //TODO handle packets by connection instance for multiplayer
        //System.out.println(packet);
        if (packet.startsWith("network") && needsSetup && c.isHost()) {
            System.out.println("The server is now running in NETWORK MODE.");
            gameType = Config.GameType.NETWORK;
            lobbyManager = new LobbyManager(this);
        }

        if (packet.startsWith("local") && needsSetup && c.isHost()) {
            System.out.println("The server is now running in LOCAL MODE.");
            gameType = Config.GameType.LOCAL;
            acceptingNewConnections = false;
        }

        if (packet.startsWith("setup") && needsSetup && c.isHost()) {
            setupString = packet.substring(packet.indexOf(':') +1);
            gameManager.buildPlayers(setupString);
            gameManager.start();
            acceptingNewConnections = false;
            if (gameType.equals(Config.GameType.NETWORK)) {
                sendMessage("startmulti");
            }

        } else {
            if (packet.startsWith("click")) {
                int tileId = Integer.parseInt(packet.substring(packet.indexOf(':') + 1));
                System.out.println("TileId: " + tileId + " was clicked by client " + c.getIndex());
                if (!gameManager.getPlayerManager().computerMoving && gameManager.getPlayerManager().getActivePlayer().getId() == c.getIndex()) {// make sure correct client is playing
                    gameManager.getPlayerManager().handlePlayerMoveIntent(gameManager.getPlayerManager().getActivePlayer(), tileId);
                    System.out.println("Attempting to move piece (Client:"+c.getIndex()+") " + gameManager.getPlayerManager().getActivePlayer().getId() + " is active player!");
                }
            }
            if (packet.startsWith("dice")) {
                if (!gameManager.getPlayerManager().computerMoving && gameManager.getPlayerManager().getActivePlayer().getId() == c.getIndex()) // the computer AI is rolling not the player
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

    /**
     * Sends a global message to all clients
     * @param message the message to send
     */
    public void sendMessage(String message) {
        playerClients.forEach(client -> client.sendMessage(message));
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

    //shutdown the game server, kill all connections
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
