package game.client;

import game.Main;
import game.server.environment.Position;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Controller {

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
        Main.client.getConnection().sendUpdate("shutdown");
        Main.client.getConnection().dispose();
        Stage stage = Main.primaryStage;
        stage.setScene(Main.menuScene);
        stage.show();
        Main.gameServer = null;
    }

    @FXML
    private void resetGame(){
        Main.client.getConnection().sendUpdate("restart");
    }

    @FXML
    private void initialize() {
        Main.graphicsContext = canvas.getGraphicsContext2D();
        canvas.setOnMouseClicked(event -> {
            int x = (int)(event.getX() / 40);
            int y = (int)(event.getY() / 40);
            Main.client.getConnection().sendUpdate("click:"+getTileId(new Position(x, y)));
        });
    }

    public int getTileId(Position position) {
        return (position.getY() * 17 + position.getX()); // y * rowLength + x = tileID
    }

    @FXML
    protected void diceRoll() { //TODO implement dice
        if (Main.client.getConnection() != null) {
            Main.client.getConnection().sendUpdate("dice");
        }
    }

}