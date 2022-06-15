package e1;

import java.util.ArrayList;

public class Army <E extends Character>{
    private final ArrayList<E> armyUnits;

    public Army() {
        this.armyUnits = new ArrayList<>();
    }

    public ArrayList<E> getArmyUnits() {
        return this.armyUnits;
    }

    public void addUnit(E unit) {
        this.armyUnits.add(unit);
    }

    public String cleanDeaths() {
        StringBuilder st = new StringBuilder();

        for ( int j = 0; j < this.armySize(); j++ ) {
            if (this.getUnit(j).getHp() <= 0) {
                st.append(this.getUnit(j).death());
                this.removeDeadUnit(j);
            }
        }
        return st.toString();
    }

    public boolean isEmpty(){
        return this.armyUnits.isEmpty();
    }

    public int armySize() {
        return this.armyUnits.size();
    }

    public E getUnit(int unitIndex) {
        if ( unitIndex < this.armySize() && unitIndex >= 0 )
            return this.armyUnits.get(unitIndex);
        else
            throw new IllegalArgumentException();
    }

    public void removeDeadUnit(int j) {
        this.armyUnits.remove(j);
    }
}
