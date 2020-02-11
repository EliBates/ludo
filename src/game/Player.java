package game;

import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.Random;

public class Player {

    private int id;
    private String name;
    private GamePiece[] gamePieces;

    public Player(int id, String name) {
        this.id = id;
        this.name = name;
        gamePieces = new GamePiece[4];
        for (int i = 0; i < gamePieces.length; i++) {
            gamePieces[i] = new GamePiece(id);
        }
    }

    public void drawPieces(GraphicsContext g, GameGrid grid) {
        for (GamePiece piece : gamePieces) {
            piece.draw(g, grid);
        }
    }

    public void movePiece(int roll, GameGrid grid) {
        if (roll == 6) {
            GamePiece gp = getDefaultPiece();
            if (gp != null) {
                gp.setIndex(GameGrid.RED_START, grid);
                return;
            }
        }
            GamePiece gp = getBoardPiece();
            if (gp != null) {
                MoveAnimation moveAnimation = new MoveAnimation(gp, roll, grid);
                moveAnimation.run();
            }
    }

    private GamePiece getBoardPiece() {
        ArrayList<GamePiece> gp = new ArrayList<>();
        for (int i = 0; i < 4; i++) { // find all the pieces that are on board
            if (gamePieces[i].getIndex() != -1)
                gp.add(gamePieces[i]);
        }
        if (gp.size() > 0) { // if there are some board pieces we will random choose one to move
            Random random = new Random();
            int index = random.nextInt(gp.size());
            return gp.get(index);
        }
        return null;
    }

    private GamePiece getDefaultPiece() {
        for (GamePiece gamePiece : gamePieces) {
            if (gamePiece.getIndex() == -1)
                return gamePiece;
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public GamePiece[] getGamePieces() {
        return gamePieces;
    }

    public int getId() {
        return id;
    }
}