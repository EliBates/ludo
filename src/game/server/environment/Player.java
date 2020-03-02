package game.server.environment;

import game.server.Config;
import game.server.io.Connection;
import game.server.service.TileManager;

import java.util.ArrayList;

public class Player {

    private Connection connection;
    private int id;
    private int type;
    private String name;
    private GamePiece[] gamePiece;
    private Path path;

    public Player(int id, String name, int type) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.path = Config.pathForId(id);
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
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

    public ArrayList<Integer> getMoveablePieces(int distance, TileManager tm) {
        ArrayList<Integer> moveablePieces = new ArrayList<>();
        for (GamePiece gp : gamePiece) {
            int tileAttempt = path.getDestinationId(gp.getTileId(), distance);
            if (tileAttempt == -1) {
                if (distance == 6 && gp.getPosition() == gp.getOriginalPosition() && !tm.tileIsBlocked(this, path.getStartPoint())) {
                    moveablePieces.add(gp.getTileId());
//                    System.out.println("moveable tile: " + gp.getTileId());
                }
            } else if (tm.tileIsBlocked(this, tileAttempt)) {

            } else {
//                System.out.println("moveable tile: " + gp.getTileId());
                moveablePieces.add(gp.getTileId());
            }
        }
        return moveablePieces;
    }

    public String getMoveablePiecesMessage(int distance, TileManager tm) {
        ArrayList<Integer> moveablePieces = getMoveablePieces(distance, tm);
        StringBuilder message = new StringBuilder("highlight:");
        for(int tileID : moveablePieces) {
            message.append(tileID).append(",");
        }
        return message.toString();
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

    public int getType() {
        return type;
    }
}
