package game;

import javafx.scene.image.Image;

public class Config {

    public static final int RED = 0, YELLOW = 1, BLUE = 2, GREEN = 3;

    public enum GamePiece {
        RED(0, "/assets/red-piece.png", 1, 51),
        YELLOW(1,"/assets/yellow-piece.png", 27, 25),
        BLUE(2, "/assets/blue-piece.png", 40, 38),
        GREEN(3, "/assets/green-piece.png", 14, 12);

        private int id, startTile, lastTile;
        private String imagePath;

        GamePiece(int id, String imagePath, int startTile, int lastTile) {
            this.id = id;
            this.imagePath = imagePath;
            this.startTile = startTile;
            this.lastTile = lastTile;
        }

        public int getStartTile() {
            return startTile;
        }

        public int getLastTile() {
            return lastTile;
        }

        public Image getImage() {
            return new Image(imagePath);
        }
    }
}
