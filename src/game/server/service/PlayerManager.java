package game.server.service;

import game.util.Rand;
import game.server.component.GamePiece;
import game.server.component.Player;
import game.server.component.Position;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Eli
 * Most of the player/game logic is handled inside this class as well as the instances of all the players
 */
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

    /**
     * Returns string of all the player names
     * @return string of all the player names
     */
    public String getPlayerNames() {
        StringBuilder output = new StringBuilder();
        for (Map.Entry<Integer, Player> integerPlayerEntry : players.entrySet()) {
            output.append(players.get(integerPlayerEntry.getKey()).getName()).append(":");
        }
        return output.toString();
    }

    /**
     * returns string of all the player scores
     * @return string of all the player scores
     */
    public String getScoreData() {
        StringBuilder output = new StringBuilder();
        for (Map.Entry<Integer, Player> integerPlayerEntry : players.entrySet()) {
            output.append(integerPlayerEntry.getKey())
                    .append(",")
                    .append(integerPlayerEntry.getValue().getScore())
                    .append(":");
        }
        return output.toString();
    }

    /**
     * Returns string of all the player data
     * @return string of all the player data (ID,posX,posY)
     */
    public String getPlayerData() {
        StringBuilder output = new StringBuilder();
        output.append("pieceupdate");
        for (Map.Entry<Integer, Player> integerPlayerEntry : players.entrySet()) {
            output.append(integerPlayerEntry.getKey());
            for (GamePiece gamePiece : integerPlayerEntry.getValue().getGamePieces()) {
                Position pos = gamePiece.getPosition();
                output.append(",")
                        .append(pos.getX())
                        .append("/")
                        .append(pos.getY());
            }
            output.append("-");
        }
        return output.toString();
    }

    //adds player to the game
    public void addPlayer(Player player, TileManager tm) {
        if (players.size() < 4 && !playerExists(player.getId())) {
            players.put(player.getId(), player);
            player.initGamePieces(tm);
        }
        gameManager.requestClientUpdate=true;
    }

    //checks if player exists in the game
    private boolean playerExists(int playerId) {
        return players.containsKey(playerId);
    }

    //gets the reference to the active player
    public Player getActivePlayer() {
        return players.get(turnOrder[activePlayerIndex]);
    }

    /**
     * handles the turn order/processing of turns for humans/ai
     */
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

    //Sleep method
    private void pause(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * initiate server side dice roll.
     */
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

    /**
     * process a move intent (player clicks on a piece)
     * @param p player
     * @param tileId the tile clicked
     */
    public void handlePlayerMoveIntent(Player p, int tileId) {
        if (validTurn(p)) { // you are allowed to move pieces
            if (gameManager.getTileManager().playerOnTile(p, tileId)) { // Check if you clicked your own piece
                int destinationId = p.getPath().getDestinationId(tileId, activeDiceRoll); // get the applicable destination for the piece you clicked
                attemptMoveBoardPiece(p, tileId, destinationId);
            }
        }
    }

    /**
     * Attemtps to move a starting piece
     * @param p player instance
     * @param tileId tileID to move
     */
    private void attemptMoveStartingPiece(Player p, int tileId) {
        if (canMoveStartingPiece(p, tileId)) {
            gameManager.getTileManager().moveGamePieces(tileId, p.getPath().getStartPoint());
        } else {
            System.out.println("You cant move your starting piece");
            return;
        }
        resetTurn();
    }

    /**
     * attempts to move a board piece (piece that already left the start)
     * @param p player instance
     * @param tileId tile id clicked
     * @param destinationId computed destination, if bad dest. will be -1
     */
    private void attemptMoveBoardPiece(Player p, int tileId, int destinationId) {
        if (canMoveToTile(p, destinationId)) {
            if (p.getPath().getEndPoint() == destinationId) {
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

    /**
     *
     * @return if the player has no available board pieces to move
     */
    private boolean noAvailableBoardPieces() {
        return (getActivePlayer().getAvailablePieces(activeDiceRoll, gameManager.getTileManager()).size() < 1);
    }

    /**
     * @return if the player has no available starting pieces to move
     */
    private boolean allPiecesStuckAtStart() {
       return (activeDiceRoll != 6 && getActivePlayer().allAtStart());
    }

    /**
     * Check if the tile can be moved
     * @param p player instance
     * @param tileId tile clicked
     * @return if the player can move the starting piece
     */
    private boolean canMoveStartingPiece(Player p, int tileId) {
        boolean t1 = !p.getPath().contains(tileId),
                t2 = activeDiceRoll == 6,
                t3 = !gameManager.getTileManager().tileIsBlocked(p, p.getPath().getStartPoint());
        System.out.println(t1 + " " + t2 + " " + t3); // for troubleshooting
        return (!p.getPath().contains(tileId) && activeDiceRoll == 6 && !gameManager.getTileManager().tileIsBlocked(p, p.getPath().getStartPoint()));
    }

    /**
     * Checks if the player can move to the given tile
     * @param p player inst
     * @param tileId tile to check
     * @return if the tile can be moved to
     */
    private boolean canMoveToTile(Player p, int tileId) {
        return ((tileId > 0) && (!gameManager.getTileManager().tileIsBlocked(p, tileId)));
    }

    /**
     * checks if the player can conduct a turn
     * @param p player inst
     * @return if the player is the active player, and has rolled already
     */
    private boolean validTurn(Player p) {
        return ((p.getId() == turnOrder[activePlayerIndex]) && (hasRolledAlready));
    }

    // give the player a second turn if they roll a 6
    private void giveSecondTurn() {
        secondTurn = true;
        hasRolledAlready = false;
        gameManager.requestClientUpdate = true;
        if (getActivePlayer().getType() == 1) {  // The player is AI so we roll for them again
            conductRoll();
        }
    }

    //resets all the turn variables for the next player
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
