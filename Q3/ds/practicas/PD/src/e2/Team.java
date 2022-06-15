package e2;

import java.util.ArrayList;
import java.util.List;

public class Team implements WorkPart {
    private final String teamName;
    private final List<WorkPart> workParts = new ArrayList<>();
    private Project assignedProject;

    public Team(String teamName) {
        this.assignedProject = null;
        this.teamName = teamName;
    }

    public void addWorkPart(WorkPart w) {
        w.setAssignedProject(this.assignedProject);
        workParts.add(w);
    }
    public void removeWorkPart(WorkPart w) {
        w.setAssignedProject(null);
        workParts.remove(w);
    }

    @Override
    public List<Worker> getCoworkers() {
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
        for ( WorkPart w : workParts ) {
            w.setAssignedProject(assignedProject);
        }
    }
    @Override
    public float getHours() {
        float totalHours = 0;
        for ( WorkPart w : workParts )
            totalHours += w.getHours();
        return totalHours;
    }

    @Override
    public float getCost() {
        float totalCost = 0;
        for ( WorkPart w : workParts )
            totalCost += w.getCost();
        return totalCost;
    }

    @Override
    public String getSummary(int count) {
        StringBuilder summary = new StringBuilder();
        summary.append("Team ").append(teamName).append(": ").append(this.getHours()).append(" hours, ").
                append(this.getCost()).append("€\n");

        for ( WorkPart w : workParts ) {
            summary.append("\t".repeat(count));
            if ( w instanceof Team )
                summary.append(w.getSummary(++count));
            else
                summary.append(w.getSummary(count));
        }
        return summary.toString();
    }
    @Override
    public List<Worker> getWorkers() {
        List<Worker> workerList = new ArrayList<>();
        for ( WorkPart w : workParts ) {
            workerList.addAll(w.getWorkers());
        }
        return workerList;
    }

    @Override
    public String toString() {
        return teamName;
    }

}
