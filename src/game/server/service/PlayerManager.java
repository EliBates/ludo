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
            Thread.sleep(seconds * 250);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void conductRoll() {
        if (hasRolledAlready || !gameManager.isRunning)
            return;
        hasRolledAlready = true;
        activeDiceRoll = Rand.getDiceRoll();
        System.out.print(" : They rolled a " + activeDiceRoll + "\n");
        gameManager.requestClientUpdate = true;
        pause(1);
        if (allPiecesStuckAtStart() || noAvailableBoardPieces()) {
            resetTurn();
        } else {
            if (getActivePlayer().getType() == 1) { // Continue to the movement section for AI
                ArrayList<Integer> tiles = getActivePlayer().getAvailablePieces(activeDiceRoll, gameManager.getTileManager());
                if (tiles.size() > 0) {
                    handlePlayerMoveIntent(getActivePlayer(), tiles.get(Rand.getRandom(tiles.size())));
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

    public void handlePlayerMoveIntent(Player p, int tileId) {
        if (validTurn(p)) { // you are allowed to move pieces
            if (gameManager.getTileManager().playerOnTile(p, tileId)) { // Check if you clicked your own piece
                int destinationId = p.getPath().getDestinationId(tileId, activeDiceRoll); // get the applicable destination for the piece you clicked
                attemptMoveBoardPiece(p, tileId, destinationId);
            }
        }
    }

    private void attemptMoveStartingPiece(Player p, int tileId) {
        if (canMoveStartingPiece(p, tileId)) {
            gameManager.getTileManager().moveGamePieces(tileId, p.getPath().getStartPoint());
        } else {
            System.out.println("You cant move your starting piece");
            return;
        }
        resetTurn();
    }

    private void attemptMoveBoardPiece(Player p, int tileId, int destinationId) {
        if (canMoveToTile(p, destinationId)) {
            if (p.getPath().getEndPoint() == destinationId) { //todo there is the fucking problem this is not being called
                pause(20);
                if (p.addScore() == 4) {
                    gameManager.isRunning = false;
                    System.out.println(p.getName() + " has won the game!");
                }
            } else {
                System.out.println("End point: " + p.getPath().getEndPoint() + "   destID: " + destinationId);
            }
            gameManager.getTileManager().moveGamePieces(tileId, destinationId);
            resetTurn();
        } else {
            attemptMoveStartingPiece(p, tileId);
        }
    }

    private boolean noAvailableBoardPieces() {
        return (getActivePlayer().getAvailablePieces(activeDiceRoll, gameManager.getTileManager()).size() < 1);
    }

    private boolean allPiecesStuckAtStart() {
        return false;
       // return (activeDiceRoll != 6 && getActivePlayer().allAtStart());
    }

    private boolean canMoveStartingPiece(Player p, int tileId) {
        boolean t1 = !p.getPath().contains(tileId),
                t2 = activeDiceRoll == 6,
                t3 = !gameManager.getTileManager().tileIsBlocked(p, p.getPath().getStartPoint());
        System.out.println(t1 + " " + t2 + " " + t3);
        return (!p.getPath().contains(tileId) && activeDiceRoll == 6 && !gameManager.getTileManager().tileIsBlocked(p, p.getPath().getStartPoint()));
    }

    private boolean canMoveToTile(Player p, int tileId) {
        return ((tileId > 0) && (!gameManager.getTileManager().tileIsBlocked(p, tileId)));
    }

    private boolean validTurn(Player p) {
        return ((p.getId() == turnOrder[activePlayerIndex]) && (hasRolledAlready));
    }

    private void giveSecondTurn() {
        secondTurn = true;
        hasRolledAlready = false;
        gameManager.requestClientUpdate = true;
        if (getActivePlayer().getType() == 1) {  // The player is AI so we roll for them again
            conductRoll();
        }
    }

    public void resetTurn() {
        if (activeDiceRoll == 6 && !secondTurn) {
            giveSecondTurn();
            return;
        }
        hasRolledAlready = false;
        secondTurn = false;
        conductingTurn = false;
        computerMoving = false;
        gameManager.requestClientUpdate = true;
    }
}
