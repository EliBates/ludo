package game.client;

import game.Ludo;
import game.server.component.Position;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

public class GameController {

    @FXML
    Canvas canvas;

    @FXML
    VBox chatbox;

    @FXML
    public HBox player1DiceBG, player2DiceBG, player3DiceBG, player4DiceBG;

    @FXML
    ImageView player1Dice, player2Dice, player3Dice, player4Dice;

    Image[] diceImages = new Image[6];

    @FXML
    Label player1Name, player2Name, player3Name, player4Name;

    @FXML
    private void showChatBox(){
       chatbox.setVisible(!chatbox.isVisible());
    }

    @FXML
    private void newGame(){
        Ludo.client.getConnection().sendUpdate("shutdown");
        Ludo.client.getConnection().dispose();
        Stage stage = Ludo.primaryStage;
        stage.setScene(Ludo.menuScene);
        stage.show();
        Ludo.gameServer = null;
    }

    @FXML
    private void resetGame(){
        Ludo.client.getConnection().sendUpdate("restart");
    }

    @FXML
    private void initialize() {
        Ludo.graphicsContext = canvas.getGraphicsContext2D();
        canvas.setOnMouseClicked(event -> {
            int x = (int)(event.getX() / 40);
            int y = (int)(event.getY() / 40);
            Ludo.client.getConnection().sendUpdate("click:"+getTileId(new Position(x, y)));
        });
        getImages();

    }

    public int getTileId(Position position) {
        return (position.getY() * 17 + position.getX()); // y * rowLength + x = tileID
    }



    @FXML
    protected void diceRoll() { //TODO implement dice
        if (Ludo.client.getConnection() != null) {
            MediaPlayer player = new MediaPlayer(Ludo.client.roll);
            Ludo.client.getConnection().sendUpdate("dice");
            player.play();
        }

    }

    @FXML
    public void startGame() {
        if(Ludo.client.getGc()==null){
            Ludo.client.setGameController(this);
        }
    }

    public void setActivePlayer(int id) {
        clearActivePlayers();
        switch(id) {
            case 0:
                player1DiceBG.setVisible(true);
                break;
            case 1:
                player2DiceBG.setVisible(true);
                break;
            case 2:
                player3DiceBG.setVisible(true);
                break;
            case 3:
                player4DiceBG.setVisible(true);
                break;
        }
    }

    public void clearActivePlayers() {
        player1DiceBG.setVisible(false);
        player2DiceBG.setVisible(false);
        player3DiceBG.setVisible(false);
        player4DiceBG.setVisible(false);
    }

    public void getImages() {
        diceImages[0] = new Image("assets/dice1.png");
        diceImages[1] = new Image("assets/dice2.png");
        diceImages[2] = new Image("assets/dice3.png");
        diceImages[3] = new Image("assets/dice4.png");
        diceImages[4] = new Image("assets/dice5.png");
        diceImages[5] = new Image("assets/dice6.png");
    }

    public void updateDiceImage(int id, int dice) {
        Image image = diceImages[dice-1];
        switch(id) {
            case 0:
                player1Dice.setImage(image);
                break;
            case 1:
                player2Dice.setImage(image);
                break;
            case 2:
                player3Dice.setImage(image);
                break;
            case 3:
                player4Dice.setImage(image);
                break;
        }
    }

}