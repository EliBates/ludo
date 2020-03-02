package game.server;

import java.util.Random;

public class Rand {

    public static int getDiceRoll() {
        Random random = new Random();
        return random.nextInt(6) + 1;
    }

    public static int getRandom(int size) {
        if (size > 1) {
            Random random = new Random();
            return random.nextInt(size);
        } else {
            return 0;
        }
    }

}
