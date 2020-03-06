package game.server.component;

import java.util.ArrayList;

/**
 * @author Eli
 * Object representing a game tile on the grid
 * Top left corner is 0 goes horizontally row by row incrementing by 1
 */

public class GameTile {

    //list of the game pieces currently on the tile
    private ArrayList<GamePiece> gamePieces;

    //the position of the tile on the game grid. assigned by the tile manager
    private Position position;

    public GameTile(Position position) {
        this.position = position;
        gamePieces = new ArrayList<>();
    }

    public ArrayList<GamePiece> getGamePieces() {
        return gamePieces;
    }

    public boolean isOccupied() {
        return gamePieces.size() > 0;
    }

    /**
     * Checks whether the provided colorId is occupying the game tile
     * @param colorId The color id to check
     * @return if the colorId is present on the tile
     */
    public boolean occupiedByFriendly(int colorId) {
        for (GamePiece gp : gamePieces) {
            if (gp.getColorId() == colorId) {
                return true;
            }
        }
        return false;
    }

    //Resets the list for the game tile
    public void reset() {
        gamePieces = new ArrayList<>();
    }

    /**
     * Get the id of the occupant on the tile
     * @return the id of the occupant on the tile
     */
    public int getOccupantColorId() {
        for (GamePiece gp : gamePieces) {
            if (gp != null) {
                return gp.getColorId();
            }
        }
        return -1;
    }

    /**
     * @return the position of the tile on the game grid
     */
    public Position getPosition() {
        return position;
    }
}
