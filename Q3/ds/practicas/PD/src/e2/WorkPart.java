package e2;

import java.util.List;

public interface WorkPart {
    void setAssignedProject(Project assignedProject);
    Project getAssignedProject();
    float getHours();
    float getCost();
    String getSummary(int count);
    List<Worker> getWorkers();
    List<Worker> getCoworkers();
}
