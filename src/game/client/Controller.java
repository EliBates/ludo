package game.client;

import game.Main;
import game.server.environment.Position;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.VBox;

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