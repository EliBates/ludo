package game;

public class MoveAnimation implements Runnable {

    private GamePiece gp;
    private int moveAmount;
    private GameGrid grid;

    public MoveAnimation(GamePiece gamePiece, int moveAmount, GameGrid grid) {
        this.gp = gamePiece;
        this.moveAmount = moveAmount;
        this.grid = grid;
    }

    @Override
    public void run() {
        Main.animating = true;
        while (moveAmount != 0) {
            gp.setIndex(gp.getIndex()+1, grid);
            moveAmount--;
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Main.animating = false;
    }
}
