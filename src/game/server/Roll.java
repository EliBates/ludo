package game.server;

import java.util.Random;

public class Roll {

    public static int getDiceRoll() {
        Random random = new Random();
        return random.nextInt(6) + 1;
    }

}
