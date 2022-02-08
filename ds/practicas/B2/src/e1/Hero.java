package e1;

abstract class Hero extends Character {

    public Hero(int hp, String name, int armor, Dice dado) {
        super(hp, name, armor, dado, 2);
    }
}
