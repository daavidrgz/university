package e1;

abstract class Character {
    private int hp;
    private final String name;
    private final int armor;
    private int rollTimes;
    private final Dice dice;

    public Character(int hp, String name, int armor, Dice dice, int rollTimes) {
        this.hp = hp;
        this.name = name;
        this.armor = armor;
        this.dice = dice;
        this.rollTimes = rollTimes;
    }

    public String getEnergy() {
        return " (Energy = " + this.hp + ") ";
    }

    public int getHp() {
        return this.hp;
    }

    public String getName() {
        return name;
    }

    public int getArmor() {
        return armor;
    }

    public int rollDice() {
        return this.dice.roll();
    }

    public void setDiceMax(int max){
        this.dice.setMaxValue(max);
    }

    public void setRollTimes(int rollTimes) {
        this.rollTimes = rollTimes;
    }

    public int getRollTimes() {
        return this.rollTimes;
    }

    public void damage(int hpDamage) {
        this.hp = this.hp - hpDamage;
    }

    protected int getEnemyArmor(Character enemy) {
        return enemy.getArmor();
    }

    protected int getExtraDamage(Character enemy) {
        return 0;
    }

    public void fight(Character enemy) {
        int damage = 0;
        int finalDamage;

        for ( int i=0; i < this.getRollTimes(); i++ ) {
            damage = Math.max(damage, this.rollDice());
        }
        damage = damage + this.getExtraDamage(enemy);
        finalDamage = Math.max(0, damage - this.getEnemyArmor(enemy));
        enemy.damage(finalDamage);
    }

    public abstract String death();

}
