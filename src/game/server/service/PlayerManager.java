package game.server.service;

import game.server.Roll;
import game.server.environment.GamePiece;
import game.server.environment.Player;
import game.server.environment.Position;

import java.util.ArrayList;

public class PlayerManager {

    private GameManager gameManager;

    private ArrayList<Player> players;

    protected int activePlayerIndex = -1;

    public int getActiveDiceRoll() {
        return activeDiceRoll;
    }

    protected int activeDiceRoll = -1;

    protected boolean conductingTurn = false;

    public PlayerManager(Player[] newPlayers, GameManager gameManager) {
        this.gameManager = gameManager;
        players = new ArrayList<>();
        for (Player p : newPlayers) {
            if (p != null)
                addPlayer(p);
        }
    }

    public String getPlayerData() {
        String output = "";
        for (Player p : players) {
            output = output +p.getId();
            for (GamePiece gamePiece : p.getGamePieces()) {
                Position pos = gamePiece.getPosition();
                output = output +"," + (int)pos.getX() + "/" + (int)pos.getY();
            }
            output = output + "-";
            //System.out.println(output);
        }
        return output;
    }

    public void addPlayer(Player player) {
        if (players.size() < 4 && !containsPlayer(player.getId())) {
            players.add(player);
        }
    }

    public void removePlayer(Player player) {
        players.remove(player);
    }

    private boolean containsPlayer(int playerId) {
        for (Player p : players) {
            if (p.getId() == playerId) {
                return true;
            }
        }
        return false;
    }

    public Player getActivePlayer() {
        for (Player p : players) {
            if (p.getId() == activePlayerIndex) {
                return p;
            }
        }
        System.out.println("NULL PLAYER");
        return null;
    }

    protected void nextTurn() {
        gameManager.requestClientUpdate = true;
        conductingTurn = true; // lock the game loop from calling this while we conduct turn
        activePlayerIndex++;
        if (activePlayerIndex > players.size() -1) {
            activePlayerIndex = 0;
        }
        System.out.println("Starting a turn for player " + activePlayerIndex);
    }

    private boolean hasRolledAlready = false;

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

    public void handleMoveIntent(Player p, int tileId) {
        if (!hasRolledAlready)
            return;
       // System.out.println(gameManager.getTileManager().getTile(tileId).getPosition().toString());
        if (p.getId() == activePlayerIndex) { //The active player is the only one that can move a piece
            if (gameManager.getTileManager().getTile(tileId).getOccupantColorId() == p.getId()) { // The tile contains a gamePiece the player owns
                int destinationId = p.getPath().getDestinationId(tileId, activeDiceRoll); //check if the amount of the roll can be navigated to on the path
                if (destinationId != -1) { // The player can actually move the gamepiece according to the dice roll
                    gameManager.getTileManager().moveGamePieces(tileId, destinationId);
                } else if (activeDiceRoll == 6) {
                    gameManager.getTileManager().moveGamePieces(tileId, p.getPath().getStartPoint());
                } else {
                    return;
                }
                hasRolledAlready = false;
                conductingTurn = false;
                gameManager.requestClientUpdate = true;
            } else {
                System.out.println("Tile "+ tileId + " is currently not occupied by your piece!");
            }
        }
    }
}
