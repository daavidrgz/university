package e1;

import java.util.ArrayList;

public class Battle {
    Army<Hero> armyOfHeroes;
    Army<Beast> armyOfBeasts;

    public Battle(Army<Hero> armyOfHeroes, Army<Beast> armyOfBeasts) {
        this.armyOfBeasts = armyOfBeasts;
        this.armyOfHeroes = armyOfHeroes;
    }

    public ArrayList<String> toBattle() {
        ArrayList<String> battleResults = new ArrayList<>();
        Character hero ;
        Character beast;
        int counter = 0;

        while ( !armyOfHeroes.isEmpty() && !armyOfBeasts.isEmpty() ) {
            StringBuilder st = new StringBuilder();
            battleResults.add("\n\nTurn " + counter + " :");
            counter++;
            int minArmySize = Math.min(armyOfHeroes.armySize(), armyOfBeasts.armySize());

            for ( int i = 0; i < minArmySize; i++ ) {
                hero = armyOfHeroes.getUnit(i);
                beast = armyOfBeasts.getUnit(i);

                st.append("    Fight between ").append(hero.getName()).append(hero.getEnergy())
                        .append("and ").append(beast.getName()).append(beast.getEnergy());
                battleResults.add(st.toString());

                hero.fight(beast);
                beast.fight(hero);

                st = new StringBuilder();
            }
            battleResults.add(armyOfHeroes.cleanDeaths());
            battleResults.add(armyOfBeasts.cleanDeaths());
        }

        if ( this.armyOfHeroes.armySize() != armyOfBeasts.armySize() ) {
            if ( this.armyOfHeroes.armySize() > armyOfBeasts.armySize() ) {
                battleResults.add("\nHEROES WIN!\n");
            } else {
                battleResults.add("\nBEASTS WIN!\n");
            }
        } else {
            battleResults.add("\nIT'S A DRAW!\n");
        }

        for (String a : battleResults ) {
            if (!a.equals(""))
                System.out.println(a);
        }

        return battleResults;
    }
}
