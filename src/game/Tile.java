package game;

public class Tile {

    private Location location;

    private GamePiece[] gamePieces = new GamePiece[4];

    public Tile(Location location) {
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }

    public void addGamePiece() {
        //checkIfOccupied
            //set gamePieces[0] to the gamePiece that landed on this tile
    }

    private void checkOccupied() {
        //if occupied by other player
            //boot the player back to jail and clear the gamePieces array
        //if occupied by the same/current player
            //then increment the gamePieces count and add to the next avail position in array.
    }
}
