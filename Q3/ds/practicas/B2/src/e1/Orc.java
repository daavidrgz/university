package e1;

public class Orc extends Beast {

    public Orc(int hp, String name, int armor, Dice dice) {
        super(hp, name, armor, dice);
    }

    @Override
    protected int getEnemyArmor(Character enemy){
        return (int) (enemy.getArmor() * 0.9);
    }

    public String death() {
        return "Orc " + this.getName() + " dies!";
    }
}
