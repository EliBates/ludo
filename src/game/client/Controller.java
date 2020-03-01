package game.client;

import game.Main;
import game.server.environment.Position;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;

public class Controller {

    @FXML
    Canvas canvas;

    @FXML
    private void initialize() {
        Main.graphicsContext = canvas.getGraphicsContext2D();
    }

    public int getTileId(Position position) {
        return (position.getY() * 17 + position.getX()); // y * rowLength + x = tileID
    }


    @FXML
    protected void diceRoll() {
//        if (connection != null) {
//            connection.sendUpdate("dice:hello");
//        }
//        gameServer.recieveRollClick();
    }

    @FXML
    private void printOutput() {

    }
}