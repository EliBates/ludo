package game;

import java.util.HashMap;

public class GameGrid {

    private static final int RED_START = 0, GREEN_START = 13, YELLOW_START = 26, BLUE_START = 39;
    private static final int LAST_RED = 50, LAST_GREEN = 11, LAST_YELLOW = 24, LAST_BLUE = 37;

    private HashMap<Integer, Tile> grid = new HashMap<Integer, Tile>();

    public Tile getTile(int index) {
        return grid.get(index);
    }

    public GameGrid() {
        grid.put(0, new Tile(new Location(0, 6)));
        grid.put(1, new Tile(new Location(1, 6)));
        grid.put(2, new Tile(new Location(2, 6)));
        grid.put(3, new Tile(new Location(3, 6)));
        grid.put(4, new Tile(new Location(4, 6)));
        grid.put(5, new Tile(new Location(5, 6)));

        grid.put(6, new Tile(new Location(6, 5)));
        grid.put(7, new Tile(new Location(6, 4)));
        grid.put(8, new Tile(new Location(6, 3)));
        grid.put(9, new Tile(new Location(6, 2)));
        grid.put(10, new Tile(new Location(6, 1)));
        grid.put(11, new Tile(new Location(6, 0)));

        grid.put(12, new Tile(new Location(7, 0)));

        grid.put(13, new Tile(new Location(8, 0)));
        grid.put(14, new Tile(new Location(8, 1)));
        grid.put(15, new Tile(new Location(8, 2)));
        grid.put(16, new Tile(new Location(8, 3)));
        grid.put(17, new Tile(new Location(8, 4)));
        grid.put(18, new Tile(new Location(8, 5)));

        grid.put(19, new Tile(new Location(9, 6)));
        grid.put(20, new Tile(new Location(10, 6)));
        grid.put(21, new Tile(new Location(11, 6)));
        grid.put(22, new Tile(new Location(12, 6)));
        grid.put(23, new Tile(new Location(13, 6)));
        grid.put(24, new Tile(new Location(14, 6)));

        grid.put(25, new Tile(new Location(14, 7)));

        grid.put(26, new Tile(new Location(14, 8)));
        grid.put(27, new Tile(new Location(13, 8)));
        grid.put(28, new Tile(new Location(12, 8)));
        grid.put(29, new Tile(new Location(11, 8)));
        grid.put(30, new Tile(new Location(10, 8)));
        grid.put(31, new Tile(new Location(9, 8)));

        grid.put(32, new Tile(new Location(8, 9)));
        grid.put(33, new Tile(new Location(8, 10)));
        grid.put(34, new Tile(new Location(8, 11)));
        grid.put(35, new Tile(new Location(8, 12)));
        grid.put(36, new Tile(new Location(8, 13)));
        grid.put(37, new Tile(new Location(8, 14)));


        grid.put(38, new Tile(new Location(7, 14)));

        grid.put(39, new Tile(new Location(6, 14)));
        grid.put(40, new Tile(new Location(6, 13)));
        grid.put(41, new Tile(new Location(6, 12)));
        grid.put(42, new Tile(new Location(6, 11)));
        grid.put(43, new Tile(new Location(6, 10)));
        grid.put(44, new Tile(new Location(6, 9)));

        grid.put(45, new Tile(new Location(5, 8)));
        grid.put(46, new Tile(new Location(4, 8)));
        grid.put(47, new Tile(new Location(3, 8)));
        grid.put(48, new Tile(new Location(2, 8)));
        grid.put(49, new Tile(new Location(1, 8)));
        grid.put(50, new Tile(new Location(0, 8)));

        grid.put(51, new Tile(new Location(0, 7)));
    }


}