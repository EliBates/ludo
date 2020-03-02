package game.client;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Client extends Thread implements Runnable {

    private ClientConnection connection;

    private GraphicsContext gtx;

    private Image[] gamePieceImage;

    private Image ludoBoard;

    private GamePiece[] gamePieces;

    public void setConnection(ClientConnection c) {
        this.connection = c;
    }

    public void recieveUpdate(String update) {
        //System.out.println("Client Received: " + update);

        if (update.startsWith("pieceupdate")) {
            receiveGamePieceUpdate(update.substring(update.indexOf("pieceupdate") + 11));
        }
    }

    public Client(GraphicsContext gtx) {
        this.gtx = gtx;
        this.ludoBoard = new Image("assets/ludo-board.png");
        gamePieces = new GamePiece[16];
        for (int i = 0; i < 16; i++) {
            gamePieces[i] = new GamePiece(0, 0, 0);
        }
        gamePieceImage = new Image[4];
        gamePieceImage[0] = new Image("assets/red-piece.png");
        gamePieceImage[1] = new Image("assets/green-piece.png");
        gamePieceImage[2] = new Image("assets/yellow-piece.png");
        gamePieceImage[3] = new Image("assets/blue-piece.png");
        this.start();
    }

    public void receiveGamePieceUpdate(String update) {
        int index = 0;

        if (update != null) {
            String[] players = update.split("-");
            for (String data : players) {
                //System.out.println(data);
                String[] playerData = data.split(",");
                int id = Integer.parseInt(playerData[0]);
                for (int i = 1; i < playerData.length; i++) {
                    String[] pos = playerData[i].split("/");
                    GamePiece gamePiece = new GamePiece(id, (Integer.parseInt(pos[0]) * 40) +15, Integer.parseInt(pos[1]) * 40);
                    //System.out.println("Client game piece: " + id + " X: " + gamePiece.getDrawX()/40 + " Y: " + gamePiece.getDrawY()/40);
                    gamePieces[index] = gamePiece;
                    index++;
                }
            }
        }
    }

    @Override
    public void run() {
        try {
            super.run();
            Thread.sleep(1000);
            while (connection != null) {
                gtx.drawImage(ludoBoard, 0, 0);
                for (GamePiece gamePiece : gamePieces) {
                    if (gamePiece != null)
                        if (gamePiece.getDrawX() != 0 && gamePiece.getDrawY() != 0)
                            gtx.drawImage(gamePieceImage[gamePiece.getId()], gamePiece.getDrawX(), gamePiece.getDrawY());
                }
                Thread.sleep(100);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public ClientConnection getConnection() {
        return connection;
    }
}
