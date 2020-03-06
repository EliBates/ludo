package game.server.component;

import java.util.Arrays;
import java.util.stream.IntStream;

/**
 * @author Eli
 * Represents a path of tile ID's that will traveled by the player depending on the player ID
 */

public class Path {

    //The path of tile ids to use
    private int[] path;

    public Path(int[] path) {
        this.path = path;
    }

    /**
     * Returns the next point on the path for the player
     * @param point the current point the piece stands on
     * @return the next point the piece will move to
     */
    public int getNextPoint(int point) {
        return getPointAtDistance(point, 1);
    }

    /**
     * Gets the last tile ID in the path for the distance rolled
     * @param point the current tile ID the player stands on
     * @param distance the roll distance
     * @return the tile ID at the given distance, -1 if the distance does not exist on the path (too short of path)
     */
    public int getDestinationId(int point, int distance) {
        return getPointAtDistance(point, distance);
    }

    /**
     * Checks if the path contains the given tileid
     * @param tileId the tile ID to check
     * @return if the tileID checking exists in the path
     */
    public boolean contains(int tileId) {
        return Arrays.stream(path).anyMatch(tile -> tile == tileId);
    }

    /**
     * Gets the starting tile id of the path
     * @return the starting tile id of the path
     */
    public int getStartPoint() {
        return path[0];
    }

    /**
     * Gets the last tile of the path
     * @return the last tile id of the path
     */
    public int getEndPoint() {
        return path[path.length-1];
    }

    /**
     * Gets a tileId on the path at a given distance
     * @param point The starting distance
     * @param distance The distance to check
     * @return The tile ID at the given distance, -1 if the distance cannot be checked on the path
     */
    private int getPointAtDistance(int point, int distance) {
        int pathLength = path.length;
        if (point != path[pathLength -1]) {
            for (int i = 0; i < pathLength; i++) {
                if (point == path[i]) {
                    if (i + distance <= pathLength-1) {
                        return path[i+distance];
                    }
                }
            }
        }
        return -1;
    }
}
