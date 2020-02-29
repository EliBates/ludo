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

    private GraphicsContext gc;

    private ClientConnection connection;

    @FXML
    Canvas canvas;

    public Controller() {

    }

    @FXML
    private void initialize() {
        gc = canvas.getGraphicsContext2D();
    }

    @FXML
    private void startServer() {
        gameServer = new GameServer();
        client = new Client(gc, gameServer);

        canvas.setOnMouseClicked(event -> {
            int x = (int)(event.getX() / 40);
            int y = (int)(event.getY() / 40);
            //System.out.println("Position: " + x + ", " + y);
            gameServer.recieveTileClick(getTileId(new Position(x, y)));
        });
    }

    @FXML
    private void connect() {
        connection = new ClientConnection(client, "127.0.0.1", 43594);
        connection.start();
    }

    public int getTileId(Position position) {
        return (position.getY() * 17 + position.getX()); // y * rowLength + x = tileID
    }


    @FXML
    protected void diceRoll() {
        if (connection != null) {
            connection.sendUpdate("dice:hello");
        }
        gameServer.recieveRollClick();
    }

    @FXML
    private void printOutput() {

    }
}