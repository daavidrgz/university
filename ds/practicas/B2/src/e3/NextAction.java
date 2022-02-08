package e3;

import java.util.List;

public class NextAction implements Behavior {
    public GunslingerAction action(Gunslinger g) {
        List<GunslingerAction> rivalActions = g.getRivalActions();
        int rivalLoads = g.getRivalLoads();
        int myLoads = g.getLoads();

        if ( myLoads == 5 )
            return GunslingerAction.MACHINE_GUN;
        if ( myLoads == 0 && rivalLoads == 0 ) {
            return GunslingerAction.RELOAD;
        }
        if ( myLoads == 1 && rivalLoads == 0 ) {
            return GunslingerAction.RELOAD;
        }
        if( myLoads == 1 && rivalLoads == 1 ) {
            return GunslingerAction.PROTECT;
        }
        if ( myLoads == 0 && rivalLoads == 1 ) {
            return GunslingerAction.PROTECT;
        }
        if ( rivalActions.get(rivalActions.size() - 1) == GunslingerAction.PROTECT && myLoads > 0 ) {
            return GunslingerAction.SHOOT;
        }
        if ( rivalActions.get(rivalActions.size() - 1) == GunslingerAction.SHOOT ) {
            return GunslingerAction.RELOAD;
        }
        if ( rivalActions.get(rivalActions.size() - 1) == GunslingerAction.RELOAD && myLoads > 0 ) {
            return GunslingerAction.SHOOT;
        }
        if ( rivalActions.get(rivalActions.size() - 1) == GunslingerAction.RELOAD && rivalActions.get(rivalActions.size() - 2) == GunslingerAction.RELOAD ) {
            return GunslingerAction.RELOAD;
        }
        return GunslingerAction.PROTECT;
    }
}

