package game;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    Canvas canvas = new Canvas( 600, 600 );
    GraphicsContext gc = canvas.getGraphicsContext2D();
    int index = 0;
    public boolean isRunning = false;
    public static boolean animating = false;
    GameGrid grid = new GameGrid();
    Player player;

    @Override
    public void start(Stage theStage) throws Exception {
//        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
//        primaryStage.setTitle("Test");
//        primaryStage.setScene(new Scene(root, 300, 275));
//        primaryStage.show();

        theStage.setTitle( "Ludo Version 0.0.1" );
        player = new Player(0, "Dom");
        Group root = new Group();
        Scene theScene = new Scene(root);
        theStage.setScene(theScene);
        root.getChildren().add(canvas);
        initGameLoop();
        theStage.show();
    }

    public static final Image BOARD = new Image("/assets/ludo-board.png");
    public static final Image GREEN_PIECE = new Image("/assets/green-piece.png");

    public void initGameLoop() {
        conductGame();
        render();
    }

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
                    System.out.println(player.getName() + " rolled a " + roll + "!");
                    player.movePiece(roll, grid);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        th.start();
    }

    public void render() {
        Thread th = new Thread(() -> {
            while (isRunning) {
                gc.drawImage(BOARD, 0, 0);
                player.drawPieces(gc, grid);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        isRunning = true;
        th.start();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
