package e1;

public class Goblin extends Beast{

    public Goblin(int hp, String name, int armor, Dice dado) {
        super(hp, name, armor, dado);
    }

    public String death() {
        return "Goblin " + this.getName() + " dies!";
    }
}
