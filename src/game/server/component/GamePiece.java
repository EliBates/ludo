package game.server.component;

import game.server.service.TileManager;

/**
 * @author Eli
 * Object representing a game piece
 */

public class GamePiece {

    //The color of the piece 0-red, 1-green 2-yellow 3-blue
    private int colorId;

    //returns the pieces original position where it was first instantiated
    public Position getOriginalPosition() {
        return originalPosition;
    }

    //the original pos the piece was instantiated to
    private Position originalPosition;

    //the current pos of the piece
    private Position position;

    public GamePiece(int colorId) {
        this.colorId = colorId;
    }

    public Position getPosition() {
        return position;
    }

    //Sets the pieces current position, if the piece does not have an original position sets that as well
    public void setPosition(Position position) {
        if (this.originalPosition == null)
            this.originalPosition = position;
        this.position = position;
    }

    public int getColorId() {
        return colorId;
    }

    //sets the location to its original location
    public void setOriginalLocation() {
        position = originalPosition;
    }

    public void printPosition() {
        System.out.println("GamePiece: " + getColorId() + " X: " + position.getX() + " Y: " + position.getY());
    }

    //Gets the tileId for the pieces position
    public int getTileId() {
        return TileManager.getTileId(position);
    }
}
