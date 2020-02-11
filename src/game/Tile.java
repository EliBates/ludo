package game;

public class Tile {

    private int column, row;

    private Location location;

    private GamePiece[] gamePieces = new GamePiece[4];

    public Tile(Location location) {
        this.location = location;
    }

}
