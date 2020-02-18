package game;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.awt.*;

public class Main extends Application {

    Canvas canvas = new Canvas( 600, 600 );
    GraphicsContext gc = canvas.getGraphicsContext2D();

    public static boolean isRunning = false, gameStarted = false, gameOver = false, animating = false;

    GameController controller;
    GraphicsController graphics;
    GameGrid gameGrid;

    @Override
    public void start(Stage theStage) throws Exception {
//        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
//        primaryStage.setTitle("Test");
//        primaryStage.setScene(new Scene(root, 300, 275));
//        primaryStage.show();

        theStage.setTitle( "Ludo Version 0.0.1" );
        Group root = new Group();
        Scene theScene = new Scene(root);
        theStage.setScene(theScene);
        root.getChildren().add(canvas);
        isRunning = true;
        gameGrid = new GameGrid();
        controller = new GameController(gameGrid);
        graphics = new GraphicsController(gc, gameGrid);
        theStage.show();
    }

    public void initGameLoop() {
        //conductGame();
        //render();
    }
/*
    public void conductGame() { // just for testing out some logic / animations
        Thread th = new Thread(() -> {
            while (true) {
                if (animating) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    Thread.sleep(2000);
                    Dice dice = new Dice();
                    int roll = dice.roll();
                    System.out.println(PlayerManager.activePlayer.getName() + " rolled a " + roll + "!");
                    PlayerManager.activePlayer.movePiece(roll, grid);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        th.start();
    }
*/
   /* public void render() {
        Thread th = new Thread(() -> {
            while (isRunning) {
                gc.drawImage(BOARD, 0, 0);
                PlayerManager.activePlayer.drawPieces(gc, grid);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        isRunning = true;
        th.start();
    }*/


    public static void main(String[] args) {
        launch(args);
    }
}
