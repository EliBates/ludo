package game;

import javafx.scene.canvas.GraphicsContext;

public class GamePiece {

    private Location location;

    private int index;

    private int color;

    public GamePiece(int color) {
        this.color = color;
        index = -1;
    }

    public void draw(GraphicsContext g, GameGrid grid) {
        if(location != null) {
            Tile tile = grid.getTile(index);
            int drawX = (tile.getLocation().getXPos()  * 40) + 6;
            int drawY = (tile.getLocation().getYPos() * 40) - 10;
            g.drawImage(Main.GREEN_PIECE, drawX, drawY);
        }
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index, GameGrid grid) {
        this.index = index;
        Tile tile = grid.getTile(index);
        location = tile.getLocation();
    }
}
