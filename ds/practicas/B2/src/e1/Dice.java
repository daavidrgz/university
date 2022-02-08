package e1;

import java.util.Random;

public class Dice {
    protected Random dado;
    private int maxValue;

    public Dice(int maxValue) {
        this.maxValue = maxValue;
        dado = new Random();
    }

    public int roll() {
        return dado.nextInt(maxValue);
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }


}
