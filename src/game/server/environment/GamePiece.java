package game.server.environment;

public class GamePiece {

    private int colorId;

    public Position getOriginalPosition() {
        return originalPosition;
    }

    private Position originalPosition;
    private Position position;


    public GamePiece(int colorId) {
        this.colorId = colorId;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        if (this.originalPosition == null)
            this.originalPosition = position;
        this.position = position;
    }

    public int getColorId() {
        return colorId;
    }

    public void sendBackToHome() {
        position = originalPosition;
    }

    public void printPosition() {
        System.out.println("GamePiece: " + getColorId() + " X: " + position.getX() + " Y: " + position.getY());
    }
}
