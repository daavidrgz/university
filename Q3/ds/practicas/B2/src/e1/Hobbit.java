package e1;

public class Hobbit extends Hero {

    public Hobbit(int hp, String name, int armor, Dice dado) {
        super(hp, name, armor, dado);
    }

    @Override
    protected int getExtraDamage(Character enemy) {
        if ( enemy instanceof Goblin )
            return -5;
        return 0;
    }

    public String death() {
        return "Hobbit " + this.getName() + " dies!";
    }


}
