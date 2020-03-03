package game.server.service;

import game.util.Rand;
import game.server.component.GamePiece;
import game.server.component.Player;
import game.server.component.Position;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class PlayerManager {

    private GameManager gameManager;

    private LinkedHashMap<Integer, Player> players;
    public int[] turnOrder;

    protected int activePlayerIndex = -1;

    public int getActiveDiceRoll() {
        return activeDiceRoll;
    }

    protected int activeDiceRoll = -1;

    protected boolean conductingTurn = false;
    private boolean hasRolledAlready = false;

    public PlayerManager(GameManager gameManager) {
        this.gameManager = gameManager;
        players = new LinkedHashMap<>();
    }

    public String getPlayerNames() {
        StringBuilder output = new StringBuilder();
        for (Map.Entry<Integer, Player> integerPlayerEntry : players.entrySet()) {
            output.append(players.get(integerPlayerEntry.getKey()).getName()).append(":");
        }
        return output.toString();
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
        gameManager.requestClientUpdate=true;
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
        System.out.print("Starting a turn for player " + turnOrder[activePlayerIndex]);

        if (getActivePlayer().getType() == 1) { // the player is an AI so we will move for him
            computerMoving = true;
            conductRoll();
        }
    }

    private void pause(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void conductRoll() {
        if (hasRolledAlready)
            return;
        hasRolledAlready = true;
        activeDiceRoll = Rand.getDiceRoll();
        gameManager.requestClientUpdate = true;

        pause(2);
        if (activeDiceRoll != 6 && getActivePlayer().allAtStart()) {
            System.out.print(" : They rolled a " + activeDiceRoll + "\n");
            resetTurn();
        } else {

            System.out.println(" : The roll was a " + activeDiceRoll);
            if (getActivePlayer().getType() == 1) { // Continue to the movement section for AI
                ArrayList<Integer> tiles = getActivePlayer().getAvailablePieces(activeDiceRoll, gameManager.getTileManager());
                if (tiles.size() > 0) {
                    handleMoveIntent(getActivePlayer(), tiles.get(Rand.getRandom(tiles.size())));
                } else {
                    System.out.println("no available moves");
                    resetTurn();
                }
            }
        }
        gameManager.requestClientUpdate = true;
    }

    private boolean secondTurn = false;
    public boolean computerMoving = false;

    public void handleMoveIntent(Player p, int tileId) {
        if (!hasRolledAlready) {
            System.out.println("You haven't rolled yet!");
            return;
        }
//        System.out.println("Move attempt: " + tileId);
        if (p.getId() == turnOrder[activePlayerIndex]) { //The active player is the only one that can move a piece
//            System.out.println("player is active");
            if (gameManager.getTileManager().getTile(tileId).getOccupantColorId() == p.getId()) { // The tile contains a gamePiece the player owns
//                System.out.println("piece is owned by player");
//                System.out.println("Attempting to find path for " + tileId + " + " +activeDiceRoll);
                int destinationId = p.getPath().getDestinationId(tileId, activeDiceRoll); //check if the amount of the roll can be navigated to on the path
                if (destinationId != -1) { // The player can actually move the gamepiece according to the dice roll
//                   System.out.println("Destination is valid");
                    if (!gameManager.getTileManager().moveGamePieces(tileId, destinationId)) {
//                       System.out.println("unable to move piece");
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
                        if (computerMoving) {
                            conductRoll();
                        }
                        return;
                    }
                } else {
                    System.out.println("destination invalid");
                    return;
                }
                resetTurn();
            } else {
                System.out.println("Tile "+ tileId + " is currently not occupied by your piece!");
            }
        }
    }

    public void resetTurn() {
        hasRolledAlready = false;
        secondTurn = false;
        conductingTurn = false;
        computerMoving = false;
        gameManager.requestClientUpdate = true;
    }
}
