package game.server.component;

import java.util.ArrayList;

public class GameTile {

    private ArrayList<GamePiece> gamePieces;

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

    public boolean occupiedByFriendly(int colorId) {
        for (GamePiece gp : gamePieces) {
            if (gp.getColorId() == colorId) {
                return true;
            }
        }
        return false;
    }

    public void reset() {
        gamePieces = new ArrayList<>();
    }

    public int getOccupantColorId() {
        for (GamePiece gp : gamePieces) {
            if (gp != null) {
                return gp.getColorId();
            }
        }
        return -1;
    }

    public Position getPosition() {
        return position;
    }
}
