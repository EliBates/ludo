package game.server.component;

/**
 * @author Eli
 * Object representing a game position
 */
public class Position {

    //The x, y values of the position
    private int x, y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * @return The X position
     */
    public int getX() {
        return x;
    }

    /**
     * @return The Y position
     */
    public int getY() {
        return y;
    }

    /**
     * @return the message of the position coordinates
     */
    public String print() {
        return "X: " + x + " Y: " + y;
    }
}
