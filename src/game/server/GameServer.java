package game.server;

import game.server.net.Connection;
import game.server.net.Listener;
import game.server.service.ChatManager;
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

    public LobbyManager getLobbyManager() {
        return lobbyManager;
    }

    private LobbyManager lobbyManager;

    private ChatManager chatManager;

    //the list of client connections
    public Vector<Connection> playerClients = new Vector<>();

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

        if (gameType == Config.GameType.NETWORK) {
            if (lobbyManager.requestSlot(connection) == -1) {
                removeConnection(connection); // the lobby is full
                connection.dispose();
            }
        }
    }

    public void removeConnection(Connection connection) {
        System.out.println("Connection ID: " + connection.getIndex() + " removed from the list");
        playerClients.remove(connection);
        if (gameType == Config.GameType.NETWORK) {
            lobbyManager.removeSlotForConnection(connection);
        }
    }

    /**
     * Handles incoming packets to the server
     *
     * @param c      connection sending packet
     * @param packet packet data as string
     */
    public void processPacket(Connection c, String packet) { //TODO handle packets by connection instance for multiplayer
        //System.out.println(packet);
        final String[] args = packet.split(":");

        if (packet.startsWith("chatmessage") && gameType == Config.GameType.NETWORK) {
            chatManager.receiveMessage(c, args[1]);
        }

        if (packet.startsWith("name")) {
            if (lobbyManager != null && gameType == Config.GameType.NETWORK && lobbyManager.inLobby) {
                lobbyManager.setName(c, args[1]);
            }
        }

        if (packet.startsWith("color")) {
            if (lobbyManager != null && gameType == Config.GameType.NETWORK && lobbyManager.inLobby) {
                lobbyManager.setColor(c, Integer.parseInt(args[1]));
            }
        }

        if (packet.startsWith("enable") && c.isHost()) {
            if (lobbyManager != null && gameType == Config.GameType.NETWORK) {
                lobbyManager.enableSlot(Integer.parseInt(args[1]));
            }
        }

        if (packet.startsWith("disable") && c.isHost()) {
            if (lobbyManager != null && gameType == Config.GameType.NETWORK) {
                lobbyManager.disableSlot(Integer.parseInt(args[1]));
            }
        }

        if (packet.startsWith("network") && needsSetup && c.isHost()) {
            System.out.println("The server is now running in NETWORK MODE.");
            gameType = Config.GameType.NETWORK;
            lobbyManager = new LobbyManager(this);
            chatManager = new ChatManager(this);
            if (lobbyManager.requestSlot(c) == -1) {
                removeConnection(c); // the lobby is full
            }
        }

        if (packet.startsWith("local") && needsSetup && c.isHost()) {
            System.out.println("The server is now running in LOCAL MODE.");
            gameType = Config.GameType.LOCAL;
            acceptingNewConnections = false;
        }

        if (packet.startsWith("setup") && needsSetup && c.isHost()) {
            setupString = packet.substring(packet.indexOf(':') + 1);
            gameManager.buildPlayers(setupString);
            gameManager.start();
            lobbyManager.inLobby = false;
            acceptingNewConnections = false;
            if (gameType.equals(Config.GameType.NETWORK)) {
                sendMessage("startmulti"); // tells all the clients we are starting the multiplayer game
            }
        }

        if (packet.startsWith("click")) {
            int tileId = Integer.parseInt(args[1]);
            System.out.println("TileId: " + tileId + " was clicked by client " + c.getIndex());
            if (!gameManager.getPlayerManager().computerMoving && gameManager.getPlayerManager().getActivePlayer().getId() == c.getIndex()) {// make sure correct client is playing
                gameManager.getPlayerManager().handlePlayerMoveIntent(gameManager.getPlayerManager().getActivePlayer(), tileId);
                System.out.println("Attempting to move piece (Client:" + c.getIndex() + ") " + gameManager.getPlayerManager().getActivePlayer().getId() + " is active player!");
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
