package game.server.service;

import game.server.component.GamePiece;
import game.server.component.GameTile;
import game.server.component.Player;
import game.server.component.Position;

import java.util.ArrayList;
import java.util.HashMap;

public class TileManager {

    private HashMap<Integer, GameTile> gameTiles;

    public TileManager() {
        gameTiles = new HashMap<>();
        initialize();
    }

    private void initialize() {
        int id = 0;
        for (int y = 0; y < 17; y++) {
            for (int x = 0; x < 17; x++) {
                gameTiles.put(id, new GameTile(new Position(x,y)));
                id++;
            }
        }
    }

    public boolean tileIsBlocked(Player p, int tileId) {
        GameTile tile = getTile(tileId);
        return tile.occupiedByFriendly(p.getId()) && p.getPath().getEndPoint() != tileId;
    }

    public boolean playerOnTile(Player p, int tileId) {
        GameTile tile = getTile(tileId);
        return tile.occupiedByFriendly(p.getId());
    }

    public GameTile getTile(int tileId) {
        GameTile tile = gameTiles.get(tileId);
        if (tile == null) {
            System.out.println("Tile does not exist in the system");
            return null;
        }
        return gameTiles.get(tileId);
    }

    public static int getTileId(Position position) {
        // System.out.println("Position " + position.getX() + ", " + position.getY() + " translated to id: " + id);
        return (position.getY() * 17 + position.getX()); // x * rowLength + y = tileID
    }

    public boolean moveGamePieces(int oldId, int newId) {
        GameTile oldTile = getTile(oldId);

        ArrayList<GamePiece> gamePieces = oldTile.getGamePieces();
        //System.out.println("The size of gamePieces " +gamePieces.size());

        for (GamePiece gp : gamePieces) {
            if (occupyTile(newId, gp)) {
                oldTile.reset();
                return true;
            }
            //System.out.println(gp.getColorId() + " has occupied " + newId);
        }
        return false;
    }

    public boolean occupyTile(int tileId, GamePiece gamePiece) {
        GameTile tile = getTile(tileId);
        if (tile.isOccupied()) { // Someone is on the tile
            System.out.println("attempting to occupy tile");
            if (!tile.occupiedByFriendly(gamePiece.getColorId())) { // The enemy is here
                System.out.println("enemy found on tile");
                ArrayList<GamePiece> enemyPieces = tile.getGamePieces(); // get all the enemies
                for (GamePiece enemy : enemyPieces) {
                    enemy.setOriginalLocation(); // send them back home (set their Position)
                    GameTile newEnemyTile = getTile(getTileId(enemy.getOriginalPosition()));
                    newEnemyTile.getGamePieces().add(enemy); // add them to the Tiles occupancy list
                    System.out.println(enemy.getColorId() + " has had a piece sent back to the start!");
                    tile.reset();
                }
            }
        }
        //System.out.println("This is the set we are attempting...  Piece: " + gamePiece.getPosition().print() + " Tile: " + tile.getPosition().print());
        gamePiece.setPosition(tile.getPosition());
        tile.getGamePieces().add(gamePiece); //finally add yourself to the tile
        return true;
    }
}
