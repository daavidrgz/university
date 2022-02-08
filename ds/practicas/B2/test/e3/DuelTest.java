package e3;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DuelTest {

    @Test
    public void TestGunslinger() {
        Gunslinger g1 = new Gunslinger();
        assertEquals(0, g1.getLoads());
        assertEquals(0, g1.getRivalLoads());

        g1.rivalAction(GunslingerAction.PROTECT);
        assertEquals(GunslingerAction.PROTECT, g1.getRivalActions().get(0));
        g1.rivalAction(GunslingerAction.RELOAD);
        assertEquals(GunslingerAction.RELOAD, g1.getRivalActions().get(1));
        assertEquals(GunslingerAction.PROTECT, g1.getRivalActions().get(0));
        assertEquals(1, g1.getRivalLoads());

        g1.setBehavior(new BehaviorImpl());
        GunslingerAction action = g1.action();
        assertNotEquals(action, GunslingerAction.MACHINE_GUN);
        assertNotEquals(action, GunslingerAction.SHOOT);

        Gunslinger g2 = new Gunslinger();
        Gunslinger g3 = new Gunslinger();
        g2.setBehavior(new BehaviorImpl());
        g3.setBehavior(new NextAction());
        Gunfight.duel(g2, g3);

    }
}
