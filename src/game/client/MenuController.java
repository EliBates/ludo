package game.client;

import game.Ludo;
import game.server.GameServer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Screen;
import javafx.stage.Stage;

import javax.swing.*;
import java.util.*;

/**
 * @author Eli
 * The gui controller class for the menu state
 * Holds a variety of references to the menu.FXML gui file
 */

public class MenuController {

    @FXML
    private VBox mainMenu, newGame;

    @FXML
    private VBox player1Options, player2Options, player3Options, player4Options;

    @FXML
    private BorderPane localLobby;

    @FXML
    private BorderPane multiplayerLobby;

    @FXML
    private TextArea lobbyTextArea;

    @FXML
    private CheckBox disablePlayer2, disablePlayer3, disablePlayer4;

    @FXML
    private TextField player1Name, player2Name, player3Name, player4Name, lobbyTextField;

    @FXML
    private Button startGame;

    @FXML
    private static final ObservableList<String> playerTypeOptions =
            FXCollections.observableArrayList(
                    "Human",
                    "AI"
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
    Circle player2Online, player3Online, player4Online;

    @FXML
    public void changeColorPlayer1() {
        Ludo.client.getConnection().sendUpdate("color:" + 0 + ":" + playerColorOptions.indexOf(player2Color.getValue()));
    }

    @FXML
    public void changeColorPlayer2() {
        Ludo.client.getConnection().sendUpdate("color:" + 1 + ":" + playerColorOptions.indexOf(player2Color.getValue()));
    }
    @FXML
    public void changeColorPlayer3() {
        Ludo.client.getConnection().sendUpdate("color:" + 2 + ":" + playerColorOptions.indexOf(player2Color.getValue()));
    }
    @FXML
    public void changeColorPlayer4() {
        Ludo.client.getConnection().sendUpdate("color:" + 3 + ":" + playerColorOptions.indexOf(player2Color.getValue()));
    }

    @FXML
    public void changePlayer2() {

    }

    @FXML
    public void changePlayer3() {

    }

    @FXML
    public void changePlayer4() {

    }

    public void receiveMessage(String message) {
        if (lobbyTextArea.getText().length() == 0) {
            lobbyTextArea.setText(message);
        } else {
            lobbyTextArea.setText( message + "\n" + lobbyTextArea.getText());
        }
    }

    public void sendMessage(String message) {
        Ludo.client.getConnection().sendUpdate("chatmessage:" + message);
    }


    public void setPlayerLobbyData(String data) {
        System.out.println(data);
        String[] dataSplit = data.split(":");
        System.out.println(dataSplit[0]);
        int playerId = Integer.parseInt(dataSplit[1]);
        int playerColor = Integer.parseInt(dataSplit[2]);

        switch(playerId) {
            case 0:
                player1Color.setValue(setPlayerColor(playerColor));
                player1Name.setText(dataSplit[3]);
                break;
            case 1:
                player2Color.setValue(setPlayerColor(playerColor));
                player2Name.setText(dataSplit[3]);
                player2Online.setFill(Color.GREEN);
                player2Type.setValue("Human");
                break;
            case 2:
                player3Color.setValue(setPlayerColor(playerColor));
                player3Name.setText(dataSplit[3]);
                player3Online.setFill(Color.GREEN);
                player3Type.setValue("Human");
                break;
            case 3:
                player4Color.setValue(setPlayerColor(playerColor));
                player4Name.setText(dataSplit[3]);
                player4Online.setFill(Color.GREEN);
                player4Type.setValue("Human");
                break;
        }
    }

    public String setPlayerColor(int color){
        if(color == 0){
            return "Red";
        }
        else if(color == 1){
            return "Green";
        }
        else if(color == 2){
            return "Yellow";
        }
        else{
            return "Blue";
        }
    }

    public void setPlayerType(int player, int type) {
        String selected = (type == 0 ? "Human" : "AI");
        switch (player) {
            case 1:
                player2Type.setValue(selected);;
                break;
            case 2:
                player3Type.setValue(selected);;
                break;
            case 3:
                player4Type.setValue(selected);;
                break;
        }
    }

    public void disableOtherOptions(int id){
        switch(id) {
            case 0:
                player1Options.setDisable(false);
                player2Options.setDisable(false);
                player3Options.setDisable(false);
                player4Options.setDisable(false);
                break;
            case 1:
                player1Options.setDisable(true);
                player2Options.setDisable(false);
                player3Options.setDisable(true);
                player4Options.setDisable(true);
                break;
            case 2:
                player1Options.setDisable(true);
                player2Options.setDisable(true);
                player3Options.setDisable(false);
                player4Options.setDisable(true);
                break;
            case 3:
                player1Options.setDisable(true);
                player2Options.setDisable(true);
                player3Options.setDisable(true);
                player4Options.setDisable(false);
                break;
        }
    }

    @FXML
    private void setDisablePlayer2() {
        if (disablePlayer2.isSelected()) {
            player2Name.setDisable(true);
            player2Type.setDisable(true);
            player2Color.setDisable(true);
        } else {
            player2Name.setDisable(false);
            player2Type.setDisable(false);
            player2Color.setDisable(false);
        }
    }

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
        lobbyTextField.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER)  {
                sendMessage(lobbyTextField.getText());
                lobbyTextField.setText("");
            }
        });
        player2Type.setValue("AI");
        player2Type.setItems(playerTypeOptions);
        player3Type.setValue("AI");
        player3Type.setItems(playerTypeOptions);
        player4Type.setValue("AI");
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
    private void newNetworkGame() {
        newGame.setVisible(false);
        multiplayerLobby.setVisible(true);
        if (Ludo.gameServer == null) {
            Ludo.client.setMenuController(this);
            Ludo.gameServer = new GameServer();
            connect();
            Ludo.client.getConnection().sendUpdate("network");
            setPlayerName();
        }
    }

    @FXML
    private void joinGame() {
        Ludo.client.setMenuController(this);
        connect();
        if (Ludo.client.isAlive()) {
            mainMenu.setVisible(false);
            startGame.setVisible(false);
            disablePlayer2.setVisible(false);
            disablePlayer3.setVisible(false);
            disablePlayer4.setVisible(false);
            multiplayerLobby.setVisible(true);
            setPlayerName();
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void newGame() {
        mainMenu.setVisible(false);
        newGame.setVisible(true);
    }

    @FXML
    private void backButton() {
        multiplayerLobby.setVisible(false);
        localLobby.setVisible(false);
        newGame.setVisible(false);
        mainMenu.setVisible(true);
    }

    @FXML
    private void connect() {
        Ludo.client.setConnection(new Connection(Ludo.client, "127.0.0.1", 43594));
        Ludo.client.getConnection().start();
        if (!Ludo.client.isAlive()) {
            Ludo.client.start();
        }
    }

    public boolean validColors() {
        List<Integer> colors = new ArrayList<>();

        colors.add(playerColorOptions.indexOf(player1Color.getValue()));
        colors.add(playerColorOptions.indexOf(player2Color.getValue()));

        if (!disablePlayer3.isSelected())
            colors.add(playerColorOptions.indexOf(player3Color.getValue()));
        if (!disablePlayer4.isSelected())
            colors.add(playerColorOptions.indexOf(player4Color.getValue()));

        Set<Integer> matchCheckSet = new HashSet<>(colors);
        return matchCheckSet.size() == colors.size();
    }

    private void setPlayerName() {
        TextInputDialog dialog = new TextInputDialog("Player");
        dialog.setTitle("Network Game");
        dialog.setHeaderText("Ludo");
        dialog.setContentText("Please enter your name: ");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(s -> Ludo.client.getConnection().sendUpdate("name:" + s));
    }

    @FXML
    private void setHostName(){
        Ludo.client.getConnection().sendUpdate("name:" + player1Name.getText());
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

    public void loadMultiGame() {
        Stage stage = Ludo.primaryStage;
        stage.setScene(Ludo.gameScene);
        stage.sizeToScene();
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((screenBounds.getWidth() - 1100) / 2);
        stage.setY((screenBounds.getHeight() - 750) / 2);
        stage.show();
    }

    @FXML
    public void startMultiGame() {
        System.out.println("made it");
        if (!validColors()) {
            JOptionPane.showMessageDialog(null, "More than one player cannot use the same color!");
            return;
        }
        Ludo.client.getConnection().sendUpdate("setup" + getPlayer1Data() + getPlayer2Data() + getPlayer3Data() + getPlayer4Data());
    }



    @FXML
    private void startServer() {
        if (!validColors()) {
            JOptionPane.showMessageDialog(null, "More than one player cannot use the same color!");
            return;
        }
        if (Ludo.gameServer == null) {
            MediaPlayer player = new MediaPlayer(Ludo.client.introSound);
            player.setVolume(.5);
            player.play();
            Ludo.gameServer = new GameServer();
            Stage stage = Ludo.primaryStage;
            stage.setScene(Ludo.gameScene);
            stage.sizeToScene();
            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            stage.setX((screenBounds.getWidth() - 1100) / 2);
            stage.setY((screenBounds.getHeight() - 750) / 2);
            stage.show();
            connect();
            System.out.println(getPlayer2Data());
            Ludo.client.getConnection().sendUpdate("setup" + getPlayer1Data() + getPlayer2Data() + getPlayer3Data() + getPlayer4Data());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            player = new MediaPlayer(Ludo.client.introVoice);
            player.setVolume(1);
            player.play();
        }
    }
}
