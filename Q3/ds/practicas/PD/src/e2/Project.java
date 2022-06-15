package e2;

import java.util.ArrayList;
import java.util.List;

public class Project {
    private final String projectName;
    private final List<Team> projectTeams = new ArrayList<>();

    public Project(String projectName) {
        this.projectName = projectName;
    }

    public void addTeam(Team team) {
        team.setAssignedProject(this);
        projectTeams.add(team);
    }
    public void removeTeam(Team team) {
        team.setAssignedProject(null);
        projectTeams.remove(team);
    }

    public String getSummary() {
        StringBuilder summary = new StringBuilder();

        summary.append("\n----- PROJECT: ").append(projectName).append(" -----\n\n");
        for ( Team team : projectTeams) {
            summary.append(team.getSummary(1));
        }
        return summary.toString();
    }

    public List<Worker> getAllWorkers() {
        List<Worker> coWorkers = new ArrayList<>();

        for ( Team team : projectTeams) {
            coWorkers.addAll(team.getWorkers());
        }
        return coWorkers;
    }

    @Override
    public String toString() {
        return projectName;
    }
}
