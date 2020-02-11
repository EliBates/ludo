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

    @Override
    public void start(Stage theStage) throws Exception {
//        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
//        primaryStage.setTitle("Test");
//        primaryStage.setScene(new Scene(root, 300, 275));
//        primaryStage.show();

        theStage.setTitle( "Canvas Example" );

        Group root = new Group();
        Scene theScene = new Scene( root );
        theStage.setScene( theScene );

        root.getChildren().add( canvas );
        drawImages();
        theStage.show();
    }

    Image board = new Image( "/ludo-board.png");
    Image red = new Image("/green-piece.png");

    public void drawImages() {
        GameGrid grid = new GameGrid();
        Tile tile2 = grid.getTile(50);
        gc.drawImage( board, 0, 0 );
        int drawX2 = (tile2.getLocation().getXPos() * 40) + 6;
        int drawY2 = (tile2.getLocation().getYPos() * 40) - 10;
        //gc.drawImage(red, drawX2, drawY2);

        Thread th = new Thread(new Render(grid));
        th.start();

    }

     class Render implements Runnable {

        GameGrid grid;
        public Render(GameGrid grid) {
            this.grid = grid;
            isRunning = true;
        }

        @Override
        public void run() {
            while (isRunning) {
                Tile tile = grid.getTile(index);
                int drawX = (tile.getLocation().getXPos()  * 40) + 6;
                int drawY = (tile.getLocation().getYPos() * 40) - 10;
                System.out.println("made");
                gc.drawImage(board, 0, 0);
                gc.drawImage(red, drawX, drawY);
                index++;
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (index == 52)
                    isRunning = false;
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
