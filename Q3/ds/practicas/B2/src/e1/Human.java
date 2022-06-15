package e1;

public class Human extends Hero{

    public Human(int hp, String name, int armor, Dice dice) {
        super(hp, name, armor, dice);
    }

    public String death() {
        return "Human " + this.getName() + " dies!";
    }
}
