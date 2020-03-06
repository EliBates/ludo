package game.util;

import java.util.Random;

/**
 * @author Eli
 * Simple random number generator class for dice / other random events
 */

public class Rand {

    //returns random 1-6
    public static int getDiceRoll() {
        Random random = new Random();
        return random.nextInt(6) + 1;
    }

    //returns random 1-size
    public static int getRandom(int size) {
        if (size > 1) {
            Random random = new Random();
            return random.nextInt(size);
        } else {
            return 0;
        }
    }

}
