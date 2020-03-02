package game.client;

import game.Main;
import game.server.GameServer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javax.swing.*;
import java.util.*;

public class MenuController {

    private ClientConnection connection;

    @FXML
    private Pane gameMenu;

    @FXML
    private VBox mainMenu, newGame;

    @FXML
    private BorderPane localLobby;

    @FXML
    private Button newGameButton, joinGameButton, newLocalGame, localButton, startGame;

    @FXML
    private CheckBox disablePlayer3, disablePlayer4;

    @FXML
    private TextField player1Name, player2Name, player3Name, player4Name;

    @FXML
    private static final ObservableList<String> playerTypeOptions =
            FXCollections.observableArrayList(
                    "Human",
                    "Ludo Bot"
            );

    @FXML
    private static final ObservableList<String> playerColorOptions =
            FXCollections.observableArrayList(
                    "Red",
                    "Green",
                    "Yellow",
                    "Blue"
            );

    @FXML
    private ComboBox<String> player2Type;
    @FXML
    private ComboBox<String> player3Type;
    @FXML
    private ComboBox<String> player4Type;

    @FXML
    private ComboBox<String> player1Color;
    @FXML
    private ComboBox<String> player2Color;
    @FXML
    private ComboBox<String> player3Color;
    @FXML
    private ComboBox<String> player4Color;

    @FXML
    private void setDisablePlayer3() {
        if (disablePlayer3.isSelected()) {
            player3Name.setDisable(true);
            player3Type.setDisable(true);
            player3Color.setDisable(true);
        } else {
            player3Name.setDisable(false);
            player3Type.setDisable(false);
            player3Color.setDisable(false);
        }
    }

    @FXML
    private void setDisablePlayer4() {
        if (disablePlayer4.isSelected()) {
            player4Name.setDisable(true);
            player4Type.setDisable(true);
            player4Color.setDisable(true);
        } else {
            player4Name.setDisable(false);
            player4Type.setDisable(false);
            player4Color.setDisable(false);
        }
    }

    @FXML
    private void initialize() {
        player2Type.setValue("Ludo Bot");
        player2Type.setItems(playerTypeOptions);
        player3Type.setValue("Ludo Bot");
        player3Type.setItems(playerTypeOptions);
        player4Type.setValue("Ludo Bot");
        player4Type.setItems(playerTypeOptions);

        player1Color.setValue("Red");
        player2Color.setValue("Green");
        player3Color.setValue("Yellow");
        player4Color.setValue("Blue");
        player1Color.setItems(playerColorOptions);
        player2Color.setItems(playerColorOptions);
        player3Color.setItems(playerColorOptions);
        player4Color.setItems(playerColorOptions);

    }

    @FXML
    private void newLocalGame() {
        newGame.setVisible(false);
        localLobby.setVisible(true);
    }

    @FXML
    private void joinGame() {
        Main.client = new Client(Main.graphicsContext);
        Stage stage = Main.primaryStage;
        stage.setScene(new Scene(Main.game, 1800, 1000));
        //stage.sizeToScene();
        stage.show();
        connect();
    }

    @FXML
    private void newGame() {
        mainMenu.setVisible(false);
        newGame.setVisible(true);
    }

    @FXML
    private void connect() {
        if (connection != null) {
            return;
        }
        connection = new ClientConnection(Main.client, "127.0.0.1", 43594);
        connection.start();
    }

    public boolean validColors() {
        List<Integer> colors = new ArrayList<>();

        colors.add(playerColorOptions.indexOf(player1Color.getValue()));
        colors.add(playerColorOptions.indexOf(player2Color.getValue()));

        if (!disablePlayer3.isSelected())
            colors.add(playerColorOptions.indexOf(player3Color.getValue()));
        if (!disablePlayer4.isSelected())
            colors.add(playerColorOptions.indexOf(player4Color.getValue()));

        Set<Integer> matchCheckSet = new HashSet<Integer>(colors);
        return matchCheckSet.size() == colors.size();
    }

    private String getPlayer1Data() {
        return ":" +   playerColorOptions.indexOf(player1Color.getValue()) + "," +
                player1Name.getText() + "," +
                0;
    }

    private String getPlayer2Data() {
        return ":" +   playerColorOptions.indexOf(player2Color.getValue()) + "," +
                player2Name.getText() + "," +
                playerTypeOptions.indexOf(player2Type.getValue());
    }

    private String getPlayer3Data() {
        return disablePlayer3.isSelected() ? "" :
                ":" +   playerColorOptions.indexOf(player3Color.getValue()) + "," +
                        player3Name.getText() + "," +
                        playerTypeOptions.indexOf(player3Type.getValue());
    }

    private String getPlayer4Data() {
        return disablePlayer4.isSelected() ? "" :
                ":" +   playerColorOptions.indexOf(player4Color.getValue()) + "," +
                        player4Name.getText() + "," +
                        playerTypeOptions.indexOf(player4Type.getValue());
    }

    @FXML
    private void startServer() {
        if (connection != null) {
            //connection.dispose();
            connection = null;
        }
        if (!validColors()) {
            JOptionPane.showMessageDialog(null, "One or more players are trying to use the same color!");
            return;
        }
        if (Main.gameServer == null) {
            Main.client = new Client(Main.graphicsContext);
            Main.gameServer = new GameServer();
            Stage stage = Main.primaryStage;
            stage.setScene(Main.gameScene);
            stage.sizeToScene();
            stage.show();
            connect();
            connection.sendUpdate("setup" + getPlayer1Data() + getPlayer2Data() + getPlayer3Data() + getPlayer4Data());
        }
    }
}
