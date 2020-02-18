package game;

public class GamePiece {

    private Location location;

    private int index, homeRunIndex, id;

    public GamePiece(int id) {
        this.id = id;
        index = -1;
        homeRunIndex = 6;
    }

   /* public void draw(GraphicsContext g, GameGrid grid) {
        if(location != null) {
            Tile tile = grid.getTile(index);
            int drawX = (tile.getLocation().getXPos()  * 40) + 6;
            int drawY = (tile.getLocation().getYPos() * 40) - 10;
            g.drawImage(GraphicsController., drawX, drawY);
        }
    }*/

    public int getIndex() {
        return index;
    }

    public void setIndex(int index, GameGrid grid) {
        this.index = index;
        Tile tile = grid.getTile(index);
        location = tile.getLocation();
    }

    public void setHomeRunIndex(int index, Location location) {
        this.homeRunIndex = index;
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public int getId() {
        return id;
    }

    public int getHomeRunIndex() {
        return homeRunIndex;
    }
}
