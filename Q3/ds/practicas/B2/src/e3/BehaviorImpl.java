package e3;

import java.util.Random;

public class BehaviorImpl implements Behavior {
    int aggressivenessIndex = 50;
	
	public BehaviorImpl(){}

    public GunslingerAction action(Gunslinger g) {
        Random rand = new Random();
        int loads = g.getLoads();
        int enemyLoads = g.getRivalLoads();

        for(GunslingerAction action: g.getRivalActions()) {
            if( action == GunslingerAction.RELOAD) {
                aggressivenessIndex = (int)Math.round((100 + aggressivenessIndex) * 0.5);
            } else if ( action == GunslingerAction.PROTECT) {
                if (enemyLoads == 0) {
                    aggressivenessIndex = (int)Math.round((100 + aggressivenessIndex) * 0.3);
                } else if ( enemyLoads > 0 && enemyLoads < 3) {
                    aggressivenessIndex = (int)Math.round((100 + aggressivenessIndex)*0.7) ;
                } else {
                    aggressivenessIndex = (int)Math.round((100 + aggressivenessIndex) * 0.5) ;
                }
            } else if (action == GunslingerAction.SHOOT) {
                if (enemyLoads == 0) {
                    aggressivenessIndex = (int)Math.round((100 + aggressivenessIndex) * 0.3);
                } else if ( enemyLoads > 0 && enemyLoads < 3) {
                    aggressivenessIndex = (int)Math.round((100 + aggressivenessIndex)*0.7) ;
                } else {
                    aggressivenessIndex = (int)Math.round((100 + aggressivenessIndex) * 0.8) ;
                }
            }
        }
        if ( loads >= 5) {
            return GunslingerAction.MACHINE_GUN;

        } else if ( loads == 0) {
            if(rand.nextDouble() < 0.3) {
                return GunslingerAction.PROTECT;
            } else {
                return GunslingerAction.RELOAD;
            }
        } else {
            if (aggressivenessIndex > 80 ) {
                if(rand.nextDouble() < 0.3) {
                    return GunslingerAction.SHOOT;
                } else {
                    return GunslingerAction.PROTECT;
                }
            } else if ( aggressivenessIndex > 50 && aggressivenessIndex < 65) {
                return  GunslingerAction.SHOOT;
            } else {
                return GunslingerAction.RELOAD;
            }
        }
    }
}