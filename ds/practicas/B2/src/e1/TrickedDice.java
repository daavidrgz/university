package e1;

import java.util.Random;

public class TrickedDice extends Dice {

    public TrickedDice(int max, int seed) {
        super(max);
        dado = new Random(seed);
    }
}
