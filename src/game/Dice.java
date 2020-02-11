package game;

import java.util.Random;

public class Dice {

    public int roll() {
        Random random = new Random();
        int numberShowing = random.nextInt(6) + 1;
        return numberShowing;
    }
}
