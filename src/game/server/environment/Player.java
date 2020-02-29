package game.server.environment;

import game.server.Config;
import game.server.service.TileManager;

public class Player {

    private int id;
    private String name;
    private GamePiece[] gamePiece;
    private Path path;

    public Player(int id, String name) {
        this.id = id;
        this.name = name;
        this.path = Config.pathForId(id);
    }

    public boolean allAtStart() {
        for (int i = 0; i < 4; i++) {
            if (gamePiece[i].getPosition() != gamePiece[i].getOriginalPosition())
                return false;
        }
        return true;
    }

    public void initGamePieces(TileManager tm) {
        gamePiece = new GamePiece[4];
        for (int i = 0; i < 4; i++) {
            int tileId = -1;
            switch (id) {
                case Config.RED:
                    tileId = Config.RED_STARTING[i];
                    break;
                case Config.GREEN:
                    tileId = Config.GREEN_STARTING[i];
                    break;
                case Config.YELLOW:
                    tileId = Config.YELLOW_STARTING[i];
                    break;
                case Config.BLUE:
                    tileId = Config.BLUE_STARTING[i];
                    break;
            }
            gamePiece[i] = new GamePiece(id);
            gamePiece[i].setPosition(tm.getTile(tileId).getPosition());
            //System.out.println("Attempting to occupy " +tileId);
            tm.occupyTile(tileId, gamePiece[i]);
        }
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public Path getPath() {
        return path;
    }

    public GamePiece[] getGamePieces() {
        return gamePiece;
    }
}
