package game.client;

import game.Ludo;
import game.server.component.Position;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

public class GameController {

    @FXML
    Canvas canvas;

    @FXML
    VBox chatbox;

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

}