package e3;

import java.util.ArrayList;
import java.util.List;

public class Gunslinger {

    private int loads;
    private int rivalLoads;
    private final List<GunslingerAction> rivalActions;
    private Behavior behavior;

    public Gunslinger() {
        this.loads = 0;
        this.rivalLoads = 0;
        this.behavior = null;
        this.rivalActions = new ArrayList<>();
    }

    public GunslingerAction action() {
        GunslingerAction action = this.behavior.action(this);
        if (action == GunslingerAction.RELOAD) {
            this.loads ++;
        } else if (action == GunslingerAction.SHOOT) {
            this.loads --;
        }

        return action;
    }

    public int getLoads(){
        return this.loads;
    }

    public void rivalAction(GunslingerAction action) {
        this.rivalActions.add(action);
        if(action == GunslingerAction.RELOAD) {
            this.rivalLoads++;
        }
        if(action == GunslingerAction.SHOOT) {
            this.rivalLoads--;
        }
    }

    public List<GunslingerAction> getRivalActions(){
        return this.rivalActions;
    }

    public int getRivalLoads(){
        return this.rivalLoads;
    }

    public void setBehavior(Behavior behavior) {
        this.behavior = behavior;
    }
}
