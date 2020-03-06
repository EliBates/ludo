package game.client;

import javafx.application.Platform;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.media.Media;

import java.net.URISyntaxException;

/**
 * @author Eli / Jeff
 * The purpose of this class is to draw graphics, handle client sided logic, and handle lgoic based on the connecction to the server
 */

public class Client extends Thread implements Runnable {

    // The connection to the server
    private Connection connection;

    //graphics context
    private GraphicsContext gtx;

    //The images of game pieces all colors
    private Image[] gamePieceImage;

    //The image of the game board
    private Image ludoBoard;

    //The instances of the game pieces
    private GamePiece[] gamePieces;

    //Some sound effects.. will be changed later on to have a separate media player
    public Media introSound, introVoice, roll;

    //The id of the active player (received from server)
    public int activePlayer;

    //the current dice roll (received from server)
    public int diceRoll;

    //The Game controller gui reference
    private GameController gc;

    //sets the connection reference
    public void setConnection(Connection c) {
        this.connection = c;
    }

    /**
     * Handles commands received from the game server
     * @param update The update pushed to the client in the form of command. Holds extra data also describing the command function
     */
    public void receiveUpdate(String update) {
        //System.out.println("Client Received: " + update);
        if (update.startsWith("pieceupdate")) { // uppdates gamepiece locations for drawing
            receiveGamePieceUpdate(update.substring(update.indexOf("pieceupdate") + 11));
        }
        if (update.startsWith("active")) { // gets the active player
            if(gc != null) {
                receiveActivePlayer(update.substring(update.indexOf("active") + 6));
            }
        }
        if (update.startsWith("roll")) { // gets the current roll
            if(gc != null) {
                receiveDiceRoll(update.substring(update.indexOf("roll") + 4));
            }
        }
        if (update.startsWith("name")) { // gets the players names
            if(gc != null) {
                Platform.runLater(() -> gc.setPlayerNames(update.substring(update.indexOf("name") + 4)));
            }
        }
        if (update.startsWith("score")) { // gets the current score for each player (to draw score graphics)
            if(gc != null) {
                parseScore(update.substring(update.indexOf("score") + 5));
            }
        }

    }

    public Client(GraphicsContext gtx) {
        this.gtx = gtx;
        this.ludoBoard = new Image("ludo-board.png");
        gamePieces = new GamePiece[16];
        for (int i = 0; i < 16; i++) {
            gamePieces[i] = new GamePiece(0, 0, 0);
        }
        gamePieceImage = new Image[4];
        gamePieceImage[0] = new Image("assets/red-piece.png");
        gamePieceImage[1] = new Image("assets/green-piece.png");
        gamePieceImage[2] = new Image("assets/yellow-piece.png");
        gamePieceImage[3] = new Image("assets/blue-piece.png");
        try {
            introSound = new Media(getClass().getResource("/audio/introsound.mp3").toURI().toString());
            introVoice = new Media(getClass().getResource("/audio/welcome.mp3").toURI().toString());
            roll = new Media(getClass().getResource("/audio/dice.mp3").toURI().toString());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    /**
     * Positions player instances to the cooresponding positions on the server
     * @param update the positions of the players
     */
    public void receiveGamePieceUpdate(String update) {
        int index = 0;

        if (update != null) {
            String[] players = update.split("-");
            for (String data : players) {
                //System.out.println(data);
                String[] playerData = data.split(",");
                int id = Integer.parseInt(playerData[0]);
                for (int i = 1; i < playerData.length; i++) {
                    String[] pos = playerData[i].split("/");
                    GamePiece gamePiece = new GamePiece(id, (Integer.parseInt(pos[0]) * 40 + 5), Integer.parseInt(pos[1]) * 40 - 8);
                    //System.out.println("Client game piece: " + id + " X: " + gamePiece.getDrawX()/40 + " Y: " + gamePiece.getDrawY()/40);
                    gamePieces[index] = gamePiece;
                    index++;
                }
            }
        }
    }

    /**
     * Parses the active player command sent from server
     * @param id the string containing the active player id
     */
    public void receiveActivePlayer(String id){
        if(id !=null) {
            activePlayer = Integer.parseInt(id);
            gc.setActivePlayer(activePlayer);
        }
    }

    /**
     * Parses the active dice command sent from server
     * @param id the string containing the active dice id
     */
    public void receiveDiceRoll(String id){
        if(id !=null) {
            diceRoll = Integer.parseInt(id);
            gc.updateDiceImage(activePlayer, diceRoll);
        }
    }

    public void setGameController(GameController gc) {
        this.gc = gc;
    }

    /**
     * Parses the score command sent from server
     * @param id the string containing the scores for all players
     */
    public void parseScore(String score) {
        if (score != null) {
            String[] scoreArray = score.split(":");
            for(int i=0; i<scoreArray.length;i++){
                String[] data = scoreArray[i].split(",");
                setScore(Integer.parseInt(data[0]), Integer.parseInt(data[1]));
            }
        }
    }

    /**
     * Sets the score for the players
     * @param id the player id
     * @param score the score to set
     */
    public void setScore(int id, int score) {
        switch(id) {
            case 0:
                gc.setRedScore(score);
                break;
            case 1:
                gc.setGreenScore(score);
                break;
            case 2:
                gc.setYellowScore(score);
                break;
            case 3:
                gc.setBlueScore(score);
                break;
        }
    }

    @Override
    public void run() { // This is the threads run method, it handles all of the drawing. It re-draws every 100ms
        try {
            super.run();
            Thread.sleep(1000);
            while (connection != null) {
                gtx.drawImage(ludoBoard, 0, 0);
                for (GamePiece gamePiece : gamePieces) {
                    if (gamePiece != null)
                        if (gamePiece.getDrawX() != 0 && gamePiece.getDrawY() != 0)
                            gtx.drawImage(gamePieceImage[gamePiece.getId()], gamePiece.getDrawX(), gamePiece.getDrawY());
                }
                Thread.sleep(100);
            }
        } catch (InterruptedException e) {
            System.out.println("The client has been killed");
        }
    }

    public Connection getConnection() {
        return connection;
    }
}
