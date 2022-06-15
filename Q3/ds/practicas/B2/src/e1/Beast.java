package e1;

abstract class Beast extends Character{
    public Beast(int hp, String name, int armor, Dice dice) {
        super(hp,name,armor, dice, 1);
    }
}
