package game.client;

import game.client.Client;
import game.server.GameServer;
import game.server.environment.Position;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ComboBox;

public class Controller {

    private GameServer gameServer;

    private Client client;

    @FXML
    Canvas canvas;

    public Controller() {

    }

    @FXML
    private void initialize() {
        gameServer = new GameServer();
        GraphicsContext gc = canvas.getGraphicsContext2D();
        client = new Client(gc, gameServer);
        canvas.setOnMouseClicked(event -> {
            int x = (int)(event.getX() / 40);
            int y = (int)(event.getY() / 40);
            //System.out.println("Position: " + x + ", " + y);
            gameServer.recieveTileClick(getTileId(new Position(x, y)));
        });
    }

    public int getTileId(Position position) {
        return (int)(position.getY() * 17 + position.getX()); // y * rowLength + x = tileID
    }


    @FXML
    protected void diceRoll() {
        gameServer.recieveRollClick();
    }

    @FXML
    private void printOutput() {

    }
}