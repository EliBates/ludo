package game.server.service;

import game.server.Roll;
import game.server.environment.GamePiece;
import game.server.environment.Player;
import game.server.environment.Position;
import java.util.LinkedHashMap;
import java.util.Map;

public class PlayerManager {

    private GameManager gameManager;

    private LinkedHashMap<Integer, Player> players;
    public int[] turnOrder;

    protected int activePlayerIndex = -1;
    protected int activeDiceRoll = -1;

    protected boolean conductingTurn = false;
    private boolean hasRolledAlready = false;

    public PlayerManager(GameManager gameManager) {
        this.gameManager = gameManager;
        players = new LinkedHashMap<>();
    }

    public String getPlayerData() {
        StringBuilder output = new StringBuilder();
        output.append("pieceupdate");
        for (Map.Entry<Integer, Player> integerPlayerEntry : players.entrySet()) {
            output.append(integerPlayerEntry.getKey());
            for (GamePiece gamePiece : integerPlayerEntry.getValue().getGamePieces()) {
                Position pos = gamePiece.getPosition();
                output.append(",").append(pos.getX()).append("/").append(pos.getY());
            }
            output.append("-");
        }
        return output.toString();
    }

    public void addPlayer(Player player, TileManager tm) {
        if (players.size() < 4 && !playerExists(player.getId())) {
            players.put(player.getId(), player);
            player.initGamePieces(tm);
        }
    }

    public void removePlayer(Player player) {
        players.remove(player.getId());
    }

    private boolean playerExists(int playerId) {
        return players.containsKey(playerId);
    }

    public Player getActivePlayer() {
        return players.get(turnOrder[activePlayerIndex]);
    }

    protected void nextTurn() {
        gameManager.requestClientUpdate = true;
        conductingTurn = true; // lock the game loop from calling this while we conduct turn
        activePlayerIndex++;
        if (activePlayerIndex > turnOrder.length -1) {
            activePlayerIndex = 0;
        }
        System.out.println("Starting a turn for player " + turnOrder[activePlayerIndex]);
    }

    public void conductRoll() {
        if (hasRolledAlready)
            return;
        hasRolledAlready = true;
        activeDiceRoll = Roll.getDiceRoll();
        if (activeDiceRoll != 6 && getActivePlayer().allAtStart()) {
            hasRolledAlready = false;
            conductingTurn = false;
        } else {
            System.out.println("The roll was a " +activeDiceRoll);
        }
        gameManager.requestClientUpdate = true;
    }

    private boolean secondTurn = false;
    public void handleMoveIntent(Player p, int tileId) {
        if (!hasRolledAlready)
            return;
       // System.out.println(gameManager.getTileManager().getTile(tileId).getPosition().toString());
        if (p.getId() == turnOrder[activePlayerIndex]) { //The active player is the only one that can move a piece
            if (gameManager.getTileManager().getTile(tileId).getOccupantColorId() == p.getId()) { // The tile contains a gamePiece the player owns
                int destinationId = p.getPath().getDestinationId(tileId, activeDiceRoll); //check if the amount of the roll can be navigated to on the path
                if (destinationId != -1) { // The player can actually move the gamepiece according to the dice roll
                    if (!gameManager.getTileManager().moveGamePieces(tileId, destinationId)) {
                        return;
                    }
                } else if (activeDiceRoll == 6) {
                    if (!gameManager.getTileManager().moveGamePieces(tileId, p.getPath().getStartPoint())) {
                        return;
                    }
                    if (!secondTurn) { // you get to roll again!
                        hasRolledAlready = false;
                        gameManager.requestClientUpdate = true;
                        secondTurn = true;
                        return;
                    }
                } else {
                    return;
                }
                hasRolledAlready = false;
                secondTurn = false;
                conductingTurn = false;
                gameManager.requestClientUpdate = true;
            } else {
                System.out.println("Tile "+ tileId + " is currently not occupied by your piece!");
            }
        }
    }
}
