package game.client;

/**
 * @author Eli
 * Simple game piece client sided representation for holding its draw location and id
 */
public class GamePiece {

    private int id;

    public int getDrawX() {
        return drawX;
    }

    public int getDrawY() {
        return drawY;
    }

    private int drawX, drawY;

    public GamePiece(int id, int drawX, int drawY) {
        this.id = id;
        this.drawX = drawX;
        this.drawY = drawY;
    }

    public int getId() {
        return id;
    }
}
