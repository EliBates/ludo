package game;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class GraphicsController extends Thread {

    private GraphicsContext gc;

    private GameGrid grid;

    private static Image board;

    private static Image[] pieceImages;

    public GraphicsController(GraphicsContext gc, GameGrid grid) {
        this.grid = grid;
        this.gc = gc;
        loadImages();
        this.start();
    }

    @Override
    public void run() {
        while (Main.isRunning) {
            gc.drawImage(board, 0, 0);
            drawPlayers();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void drawPlayers() {
        for (Player player : PlayerManager.players) {
            GamePiece[] gamePieces = player.getGamePieces();
            for (GamePiece gamePiece : gamePieces) {
                drawGamePiece(gamePiece);
            }
        }
    }

    private void drawGamePiece(GamePiece gamePiece) {
        if(gamePiece.getLocation() != null) {
            int drawX = (gamePiece.getLocation().getXPos()  * 40) + 6;
            int drawY = (gamePiece.getLocation().getYPos() * 40) - 10;
            gc.drawImage(pieceImages[gamePiece.getId()], drawX, drawY);
        }
    }

    private void loadImages() {
        board = new Image("/assets/ludo-board.png");
        pieceImages = new Image[4];
        for (int i = 0; i < 4; i++) {
            pieceImages[i] = Config.GamePiece.values()[i].getImage();
        }
    }
}
