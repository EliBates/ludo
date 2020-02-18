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
            if (Config.GamePiece.values()[gp.getId()].getLastTile() == gp.getIndex()) { // moved to final tile
                if (moveAmount > gp.getHomeRunIndex()) {
                    break;
                }
                switch (gp.getId()) {
                    case Config.RED:
                        gp.setHomeRunIndex(gp.getHomeRunIndex() - 1, new Location(gp.getLocation().getXPos() + 1, gp.getLocation().getYPos()));
                        break;
                    case Config.BLUE:
                        gp.setHomeRunIndex(gp.getHomeRunIndex() - 1, new Location(gp.getLocation().getXPos(), gp.getLocation().getYPos() - 1));
                        break;
                    case Config.GREEN:
                        gp.setHomeRunIndex(gp.getHomeRunIndex() - 1, new Location(gp.getLocation().getXPos(), gp.getLocation().getYPos() + 1));
                        break;
                    case Config.YELLOW:
                        gp.setHomeRunIndex(gp.getHomeRunIndex() - 1, new Location(gp.getLocation().getXPos() - 1, gp.getLocation().getYPos()));
                        break;
                }
            } else if (gp.getIndex() == 51) { // dont fall off the array
                gp.setIndex(0, grid);
            } else {
                gp.setIndex(gp.getIndex()+1, grid);
            }
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
