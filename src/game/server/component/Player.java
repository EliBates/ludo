package game.server.component;

import game.util.Config;
import game.server.net.Connection;
import game.server.service.TileManager;

import java.util.ArrayList;

/**
 * @author Eli
 * Object representing a player of the game
 * Holds 4 GamePieces, can either be Human or AI
 * The player ID cooresponds to the color, Red-0/Green-1/Yellow-2/Blue-3
 */

public class Player {

    //The connection to the Client
    private Connection connection;

    //The color ID
    private int id;

    //The player type 0-Human, 1-AI
    private int type;

    //Gets the current score for the player
    public int getScore() {
        return score;
    }

    //The score (pieces that made it to the end)
    private int score;

    //The players name
    private String name;

    //the array of game pieces the player owns
    private GamePiece[] gamePiece;

    //The path the players game pieces use to move
    private Path path;

    public Player(int id, String name, int type) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.path = Config.pathForId(id);
        this.score = 0;
    }

    /**
     * Gets the connection to the client
     * @return the client connection thread
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * Sets the client connection for the player
     * @param connection the conn. to assign to the player
     */
    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    /**
     * Checks if all the players pieces are at the home
     * @return if all the players pieces are at their starting location
     */
    public boolean allAtStart() {
        for (int i = 0; i < 4; i++) {
            if (gamePiece[i].getPosition() != gamePiece[i].getOriginalPosition())
                return false;
        }
        return true;
    }

    /**
     * Increments a score for the player
     * @return the current score the player has
     */
    public int addScore() {
        score++;
        System.out.println(name + "'s score is: " +score);
        return score;
    }

    /**
     * initializes the game pieces based on the colorid
     * @param tm The tile manager instance
     */
    public void initGamePieces(TileManager tm) {
        gamePiece = new GamePiece[4];
        for (int i = 0; i < 4; i++) {
            int tileId = -1;
            switch (id) {
                case Config.RED:
                    tileId = Config.RED_STARTING[i];
                    break;
                case Config.GREEN:
                    tileId = Config.GREEN_STARTING[i];
                    break;
                case Config.YELLOW:
                    tileId = Config.YELLOW_STARTING[i];
                    break;
                case Config.BLUE:
                    tileId = Config.BLUE_STARTING[i];
                    break;
            }
            gamePiece[i] = new GamePiece(id);
            tm.occupyTile(tileId, gamePiece[i]);
        }
    }

    /**
     * Get all the available pieces that can be moved with the provided distance
     * @param distance the distance the player is attempting to move
     * @param tm the tile manager instance
     * @return the list of pieces that can be moved
     */
    public ArrayList<Integer> getAvailablePieces(int distance, TileManager tm) {
        ArrayList<Integer> moveablePieces = new ArrayList<>();
        for (GamePiece gp : gamePiece) {
            int tileAttempt = path.getDestinationId(gp.getTileId(), distance);
            if (tileAttempt == -1) {
                if (distance == 6 && gp.getPosition() == gp.getOriginalPosition() && !tm.tileIsBlocked(this, path.getStartPoint())) {
                    moveablePieces.add(gp.getTileId());
                }
            } else if (!tm.tileIsBlocked(this, tileAttempt)) {
                moveablePieces.add(gp.getTileId());
            }
        }
        return moveablePieces;
    }

    //Gets a message of the available pieces as a String...
    public String getAvailablePiecesMessage(int distance, TileManager tm) {
        ArrayList<Integer> availablePieces = getAvailablePieces(distance, tm);
        StringBuilder message = new StringBuilder("highlight:");
        for(int tileID : availablePieces) {
            message.append(tileID).append(",");
        }
        return message.toString();
    }

    //Returns the players name
    public String getName() {
        return name;
    }

    //Returns the players ID
    public int getId() {
        return id;
    }

    //Returns the players path
    public Path getPath() {
        return path;
    }

    //Returns an array of all the players game pieces
    public GamePiece[] getGamePieces() {
        return gamePiece;
    }

    //Returns whether the player is a Human-0 or AI-1
    public int getType() {
        return type;
    }
}
