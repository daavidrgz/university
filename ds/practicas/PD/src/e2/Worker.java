package e2;

import java.util.ArrayList;
import java.util.List;

public class Worker implements WorkPart {
    private final String name;
    private final float hourCost;
    private float workedHours;
    private Project assignedProject;

    public Worker(String name, float hourCost ) throws IllegalArgumentException {
        if ( hourCost < 0 )
            throw new IllegalArgumentException("Invalid hour cost");
        this.hourCost = hourCost;
        this.name = name;
        this.workedHours = 0;
        this.assignedProject = null;
    }

    public void setHours(float workedHours) throws IllegalArgumentException {
        if ( workedHours < 0 )
            throw new IllegalArgumentException("Invalid worked hours");
        this.workedHours = workedHours;
    }

    @Override
    public List<Worker> getCoworkers() throws IllegalCallerException {
        if ( assignedProject == null )
            throw new IllegalCallerException("No project assigned");
        return assignedProject.getAllWorkers();
    }
    @Override
    public Project getAssignedProject() {
        return this.assignedProject;
    }
    @Override
    public void setAssignedProject(Project assignedProject) {
        this.assignedProject = assignedProject;
    }
    @Override
    public float getHours() {
        return workedHours;
    }
    @Override
    public float getCost() {
        return workedHours * hourCost;
    }
    @Override
    public String getSummary(int count) {
        return "Worker " + name + ": " + workedHours + " hours, " + workedHours* hourCost + "€\n";
    }
    @Override
    public List<Worker> getWorkers() {
        List<Worker> worker = new ArrayList<>();
        worker.add(this);
        return worker;
    }

    @Override
    public String toString() {
        return name;
    }
}
