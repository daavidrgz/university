package e1;

public class Elf extends Hero {

    public Elf(int hp, String name, int armor, Dice dado) {
        super(hp, name, armor, dado);
    }

    @Override
    protected int getExtraDamage(Character enemy) {
        if ( enemy instanceof Orc )
            return 10;
        return 0;
    }

    public String death() {
        return "Elf " + this.getName() + " dies!";
    }
}
