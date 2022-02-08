package e3;

public class Gunfight {

    static void duel(Gunslinger g1, Gunslinger g2) {
        boolean end = false;
        int i=1;

        while ( !end && i<50 ) {
            System.out.println("\nTurn " + i + " -------------\n");
            GunslingerAction actiong1 = g1.action();
            GunslingerAction actiong2 = g2.action();
            g1.rivalAction(actiong2);
            g2.rivalAction(actiong1);
            System.out.println("Gunslinger 1: " + actiong1.name());
            System.out.println("\nGunslinger 2: " + actiong2.name());
            end = getResult(actiong1, actiong2);
            i++;
        }
        if ( i == 50 ) {
            System.out.println("Too many turns");
        }
    }

    static boolean getResult(GunslingerAction actiong1, GunslingerAction actiong2) {
        if ( actiong1 == GunslingerAction.SHOOT && actiong2 == GunslingerAction.RELOAD ) {
            System.out.println("\nThe duel has ended\n\nThe winner is... GUNSLINGER1");
            return true;
        }
        if ( actiong2 == GunslingerAction.SHOOT && actiong1 == GunslingerAction.RELOAD ) {
            System.out.println("\nThe duel has ended\n\nThe winner is... GUNSLINGER2");
            return true;
        }
        if ( actiong1 == GunslingerAction.MACHINE_GUN && actiong2 != GunslingerAction.MACHINE_GUN) {
            System.out.println("\nThe duel has ended\n\nThe winner is... GUNSLINGER1");
            return true;
        }
        if ( actiong2 == GunslingerAction.MACHINE_GUN && actiong1 != GunslingerAction.MACHINE_GUN) {
            System.out.println("\nThe duel has ended\n\nThe winner is... GUNSLINGER2");
            return true;
        }
        if ( actiong1 == GunslingerAction.MACHINE_GUN && actiong2 == GunslingerAction.MACHINE_GUN) {
            System.out.println("\nThe duel has ended\n\nThe winner is... IT'S A DRAW!");
            return true;
        }
        if ( actiong1 == GunslingerAction.SHOOT && actiong2 == GunslingerAction.SHOOT ) {
            System.out.println("\nThe duel has ended\n\nThe winner is... IT'S A DRAW!");
            return true;
        }
        System.out.println("\nThe duel continues...\n");
        return false;
    }
}
