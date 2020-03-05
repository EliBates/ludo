package game.server.component;

public class Path {

    private int[] path;

    public Path(int[] path) {
        this.path = path;
    }

    public int getNextPoint(int point) {
        return getPointAtDistance(point, 1);
    }

    public int getDestinationId(int point, int distance) {
        return getPointAtDistance(point, distance);
    }

    public boolean contains(int tileId) {
        for (int i : path) {
            if (i == tileId)
                return true;
        }
        return false;
    }

    public int getStartPoint() {
        return path[0];
    }

    public int getEndPoint() {
        return path[path.length-1];
    }

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
