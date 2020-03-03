package game;

import game.client.Client;
import game.server.GameServer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;

public class Ludo extends Application {

    public static Parent root, game;

    public static Stage primaryStage;

    public static Scene menuScene;

    public static Scene gameScene;

    public static GameServer gameServer;

    public static GraphicsContext graphicsContext;

    public static Client client;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Ludo.primaryStage = primaryStage;
        root = FXMLLoader.load(getClass().getResource("menu.fxml"));
        game = FXMLLoader.load(getClass().getResource("game.fxml"));
        menuScene = new Scene(root, 600, 450);
        gameScene = new Scene(game, 1100, 750);
        primaryStage.setTitle("ISTE-121 Ludo Project");
        primaryStage.setScene(menuScene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
