package game;

import game.client.Client;
import game.server.GameServer;
import game.util.Config;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * @author Eli
 * Main run application, loads fxml files for guis and client instance
 */

public class Ludo extends Application {

    public static Parent root, game;

    public static Stage primaryStage;

    public static Scene menuScene;

    public static Scene gameScene;

    public static GameServer gameServer;

    public static GraphicsContext graphicsContext;

    public static Client client;

    @Override
    public void start(Stage primaryStage) throws Exception {

        Config.loadProperties();

        client = new Client();
        Ludo.primaryStage = primaryStage;
        game = FXMLLoader.load(getClass().getResource("game.fxml"));
        root = FXMLLoader.load(getClass().getResource("menu.fxml"));

        gameScene = new Scene(game, 1100, 750);
        menuScene = new Scene(root, 600, 550);
        
        primaryStage.setTitle("ISTE-121 Ludo Project");
        primaryStage.setScene(menuScene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
