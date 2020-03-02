package game.server;

import game.server.environment.Path;

public class Config {

    public static final int RED = 0, GREEN = 1, YELLOW = 2, BLUE = 3;

    public enum PlayerType {HUMAN, AI}

    public enum GameType {LOCAL, NETWORK}

    public enum Color{RED, YELLOW, GREEN, BLUE}

    public static final int[] RED_STARTING = {71, 72, 88, 89};
    public static final int[] GREEN_STARTING = {80, 81, 97, 98};
    public static final int[] YELLOW_STARTING = {233, 234, 250, 251};
    public static final int[] BLUE_STARTING = {224, 225, 241, 242};

    public static Path pathForId(int id) {
        switch (id) {
            case RED:
                return new Path(new int[] {121, 122, 123, 124, 125, 109, 92, 75, 58, 41, 24, 25, 26, 43, 60, 77, 94, 111, 129, 130, 131, 132, 133, 134, 151, 168, 167, 166, 165, 164, 163, 179, 196, 213, 230, 247, 264, 263, 262, 245, 228, 211, 194, 177, 159, 158, 157, 156, 155, 154, 137, 138, 139, 140, 141, 142, 143});
            case GREEN:
                return new Path(new int[] {43, 60, 77, 94, 111, 129, 130, 131, 132, 133, 134, 151, 168, 167, 166, 165, 164, 163, 179, 196, 213, 230, 247, 264, 263, 262, 245, 228, 211, 194, 177, 159, 158, 157, 156, 155, 154, 137, 120, 121, 122, 123, 124, 125, 109, 92, 75, 58, 41, 24, 25, 42, 59, 76, 93, 110, 127});
            case YELLOW:
                return new Path(new int[] {167, 166, 165, 164, 163, 179, 196, 213, 230, 247, 264, 263, 262, 245, 228, 211, 194, 177, 159, 158, 157, 156, 155, 154, 137, 120, 121, 122, 123, 124, 125, 109, 92, 75, 58, 41, 24, 25, 26, 43, 60, 77, 94, 111, 129, 130, 131, 132, 133, 134, 151, 150, 149, 148, 147, 146, 145});
            case BLUE:
                return new Path(new int[] {245, 228, 211, 194, 177, 159, 158, 157, 156, 155, 154, 137, 120, 121, 122, 123, 124, 125, 109, 92, 75, 58, 41, 24, 25, 26, 43, 60, 77, 94, 111, 129, 130, 131, 132, 133, 134, 151, 168, 167, 166, 165, 164, 163, 179, 196, 213, 130, 247, 264, 263, 246, 229, 212, 195, 178, 161});
            default:
                return null;
        }
    }
}
