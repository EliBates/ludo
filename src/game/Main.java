package game;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage theStage) throws Exception {
//        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
//        primaryStage.setTitle("Test");
//        primaryStage.setScene(new Scene(root, 300, 275));
//        primaryStage.show();

        theStage.setTitle( "Canvas Example" );
        SVGPath svgPath = new SVGPath();

        String path = "M 100 100 L 300 100 L 200 300 z";

        //Setting the SVGPath in the form of string
        svgPath.setContent(path);

        Group root = new Group(svgPath);
        Scene theScene = new Scene( root );
        theStage.setScene( theScene );

        Canvas canvas = new Canvas( 600, 600 );
        root.getChildren().add( canvas );

        GraphicsContext gc = canvas.getGraphicsContext2D();

        Image board = new Image( "/ludo-board.png");
        Image red = new Image("blue-piece.png");
        GameGrid grid = new GameGrid();

        Tile tile = grid.getTile(1);
        Tile tile2 = grid.getTile(50);



        gc.drawImage( board, 0, 0 );
        int drawX = (tile.getLocation().getxPos()  * 40) + 6;
        int drawY = (tile.getLocation().getyPos() * 40) - 10;
        gc.drawImage(red, drawX, drawY);

        int drawX2 = (tile2.getLocation().getxPos()  * 40) + 6;
        int drawY2 = (tile2.getLocation().getyPos() * 40) - 10;
        gc.drawImage(red, drawX2, drawY2);

        theStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
