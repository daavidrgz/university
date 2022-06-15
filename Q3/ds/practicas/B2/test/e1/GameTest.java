package e1;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class GameTest {

    @Test
    public void CharacterTest() {
        Hobbit hobbit = new Hobbit(30, "Frodo", 50, new Dice(100));
        Goblin goblin = new Goblin(50, "Gollum", 20, new Dice(90));
        Hero human = new Human(40, "Gandalf", 70, new Dice(100));

        assertEquals("Frodo", hobbit.getName());
        assertEquals("Gollum", goblin.getName());
        assertEquals("Gandalf", human.getName());

        assertEquals(30, hobbit.getHp());
        assertEquals(50, goblin.getHp());
        assertEquals(40, human.getHp());

        assertEquals(50, hobbit.getArmor());
        assertEquals(20, goblin.getArmor());
        assertEquals(70, human.getArmor());
    }

    @Test
    public void AttackAndReceiveDamage() {
        Hero human = new Human(60, "Aragorn", 60, new TrickedDice(100, 1));
        Beast goblin = new Goblin(80, "Gothmog", 30, new TrickedDice(90, 2));

        //Num list tricked dice (Heroes): 85, 88, 47, 13, 54, 4, 34
        //Num list tricked dice (Beasts): 58, 42, 20, 37, 69, 60, 6

        human.fight(goblin);
        assertEquals(22, goblin.getHp());
        goblin.fight(human);
        assertEquals(60, human.getHp());

        human.fight(goblin);
        assertEquals(5, goblin.getHp());
        goblin.fight(human);
        assertEquals(60, human.getHp());

        assertEquals(60, human.getArmor());
        assertEquals(30, goblin.getArmor());
    }

    @Test
    public void WeaknessesAndStrengths() {
        //Num list tricked dice (Heroes): 85, 88, 47, 13, 54, 4, 34
        //Num list tricked dice (Beasts): 58, 42, 20, 37, 69, 60, 6

        Elf elf = new Elf(50, "Legolas", 40, new TrickedDice(100, 1));
        Orc orc = new Orc(70, "Sauron", 30, new TrickedDice(90, 2));

        elf.fight(orc);
        assertEquals(2, orc.getHp()); // 10 damage points more

        Hobbit hobbit = new Hobbit(60, "Sam", 60, new TrickedDice(100, 1));
        Goblin goblin = new Goblin(40, "Gollum", 50, new TrickedDice(100, 2));

        hobbit.fight(goblin);
        assertEquals(7, goblin.getHp()); // 5 damage points less

        orc.fight(hobbit);
        assertEquals(56, hobbit.getHp()); // 6 damage point more because the armor reduction
    }

    @Test
    public void ArmyTest() {
        Army<Hero> heroesArmy = new Army<>();
        Army<Beast> beastsArmy = new Army<>();

        assertTrue(heroesArmy.isEmpty());
        assertTrue(beastsArmy.isEmpty());
        assertEquals(0, heroesArmy.armySize());
        assertEquals(0, beastsArmy.armySize());

        heroesArmy.addUnit(new Human(60, "Aragorn", 60, new Dice(100)));
        assertFalse(heroesArmy.isEmpty());
        assertTrue(heroesArmy.getUnit(0) instanceof Human);
        assertEquals("Aragorn", heroesArmy.getUnit(0).getName());
        assertEquals(60, heroesArmy.getUnit(0).getArmor());
        assertEquals(1, heroesArmy.armySize());

        heroesArmy.addUnit(new Elf(50, "Legolas", 50, new Dice(100)));
        assertTrue(heroesArmy.getUnit(1) instanceof Elf);
        assertEquals(2, heroesArmy.armySize());

        heroesArmy.removeDeadUnit(0);
        assertEquals(1, heroesArmy.armySize());
        assertEquals("Legolas", heroesArmy.getUnit(0).getName());
        assertEquals(50, heroesArmy.getUnit(0).getHp());

        beastsArmy.addUnit(new Orc(100, "Sauron", 60, new Dice(90)));
        beastsArmy.addUnit(new Orc(70, "Gothmog", 30, new Dice(90)));
        beastsArmy.addUnit(new Goblin(80, "Gollum", 50, new Dice(90)));
        beastsArmy.removeDeadUnit(0);
        beastsArmy.removeDeadUnit(1);
        assertEquals(1, beastsArmy.armySize());
    }

    @Test
    public void BattleTest() {
        Army<Hero> heroesArmy = new Army<>();
        heroesArmy.addUnit(new Human(80, "Gandalf", 40, new TrickedDice(100, 12)));
        heroesArmy.addUnit(new Human(70, "Aragorn", 60, new TrickedDice(100, 12)));
        heroesArmy.addUnit(new Elf(50, "Legolas", 50, new TrickedDice(100, 12)));
        heroesArmy.addUnit(new Hobbit(60, "Frodo",  20, new TrickedDice(100, 12)));

        Army<Beast> beastsArmy = new Army<>();
        beastsArmy.addUnit(new Orc(90, "Dragon", 40, new TrickedDice(90, 12)));
        beastsArmy.addUnit(new Orc(100, "Sauron", 60, new TrickedDice(90, 12)));
        beastsArmy.addUnit(new Goblin(80, "Gollum", 50, new TrickedDice(90, 12)));
        beastsArmy.addUnit(new Orc(70, "Gothmog", 30, new TrickedDice(90, 12)));

        Battle game1 = new Battle(heroesArmy, beastsArmy);
        ArrayList<String> battleText1 = game1.toBattle();
        assertEquals("\nHEROES WIN!\n", battleText1.get(battleText1.size() - 1 ));

        heroesArmy = new Army<>();
        heroesArmy.addUnit(new Human(80, "Gandalf", 40, new TrickedDice(100, 16)));
        heroesArmy.addUnit(new Human(70, "Aragorn", 60, new TrickedDice(100, 16)));
        heroesArmy.addUnit(new Elf(50, "Legolas", 50, new TrickedDice(100, 16)));
        heroesArmy.addUnit(new Hobbit(60, "Frodo",  20, new TrickedDice(100, 16)));

        beastsArmy = new Army<>();
        beastsArmy.addUnit(new Orc(90, "Dragon", 40, new TrickedDice(90, 123)));
        beastsArmy.addUnit(new Orc(100, "Sauron", 60, new TrickedDice(90, 123)));
        beastsArmy.addUnit(new Goblin(80, "Gollum", 50, new TrickedDice(90, 123)));
        beastsArmy.addUnit(new Orc(70, "Gothmog", 30, new TrickedDice(90, 123)));

        Battle game2 = new Battle(heroesArmy, beastsArmy);
        ArrayList<String> battleText2 = game2.toBattle();
        assertEquals("\nBEASTS WIN!\n", battleText2.get(battleText2.size() - 1 ));

    }

}
