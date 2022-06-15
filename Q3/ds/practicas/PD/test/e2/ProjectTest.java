package e2;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.List;

public class ProjectTest {

    @Test
    public void constructorTest() {
        new Project("Project 1");
        new Team("Team 1");
        new Team("Team 2");
        new Worker("Peter", 10);
        new Worker("Juan", 15);
        assertThrows(IllegalArgumentException.class, () -> new Worker("Claudia", -1));
    }

    @Test
    public void workerTest() {
        Worker worker1 = new Worker("Ana", 10);
        assertEquals("Ana", worker1.toString());
        Worker worker2 = new Worker("Julio", 12);

        //setWorkedHours()
        worker1.setHours(100);
        worker2.setHours(50);
        assertThrows(IllegalArgumentException.class, () -> worker1.setHours(-5));

        //getHours(), getCost()
        assertEquals(100, worker1.getHours());
        assertEquals(50, worker2.getHours());
        assertEquals(1000, worker1.getCost());
        assertEquals(600, worker2.getCost());

        //getWorkers()
        List<Worker> list = new ArrayList<>();
        list.add(worker2);
        assertEquals(list, worker2.getWorkers());

        //getProject()
        assertNull(worker1.getAssignedProject());
        Project project = new Project("project");
        worker1.setAssignedProject(project);
        assertEquals(project, worker1.getAssignedProject());

        //getCoworkers()
        Team team = new Team("team1");
        team.addWorkPart(worker1);
        project.addTeam(team);
        List<Worker> workerList = new ArrayList<>();
        workerList.add(worker1);
        assertEquals(workerList, worker1.getCoworkers());
        assertThrows(IllegalCallerException.class, worker2::getCoworkers);

    }

    @Test
    public void teamTest() {
        Team team1 = new Team("Team 1");
        assertEquals("Team 1", team1.toString());
        Team team12 = new Team("Team 1.2");
        Team team2 = new Team("Team 2");

        Worker worker1 = new Worker("Ana", 10);
        Worker worker2 = new Worker("Julio", 5);
        Worker worker3 = new Worker("Peter", 10);
        Worker worker4 = new Worker("Juan", 15);
        worker1.setHours(100);
        worker2.setHours(10);
        worker3.setHours(50);
        worker4.setHours(40);

        //addWorkPart()
        team1.addWorkPart(worker1);
        team1.addWorkPart(team12);
        team12.addWorkPart(worker2);
        team2.addWorkPart(worker3);
        team2.addWorkPart(worker4);

        //getHours()
        assertEquals(10, team12.getHours());
        assertEquals(110, team1.getHours());
        assertEquals(90, team2.getHours());

        //getCost()
        assertEquals(50, team12.getCost());
        assertEquals(1050, team1.getCost());
        assertEquals(1100, team2.getCost());

        //getWorkers()
        List<Worker> team1Workers = new ArrayList<>();
        team1Workers.add(worker1); team1Workers.add(worker2);
        List<Worker> team2Workers = new ArrayList<>();
        team2Workers.add(worker3); team2Workers.add(worker4);

        assertEquals(team1Workers, team1.getWorkers());
        assertEquals(team2Workers, team2.getWorkers());

        //getCoworkers
        Project project = new Project("Project");
        project.addTeam(team1);
        assertEquals(team1Workers, team1.getCoworkers());
        assertEquals(team1Workers, team12.getCoworkers());
        assertThrows(IllegalCallerException.class, team2::getCoworkers);

        //getProject()
        assertNull(team2.getAssignedProject());
        assertEquals(project, team1.getAssignedProject());

        //removeWorkParts()
        team12.removeWorkPart(worker2);
        team1.removeWorkPart(team12);
    }

    @Test
    public void projectTest() {
        Project project = new Project("Project 1");
        assertEquals("Project 1", project.toString());
        Team team1 = new Team("Team 1");
        Team team12 = new Team("Team 1.2");
        Team team2 = new Team("Team 2");

        Worker worker1 = new Worker("Ana", 10);
        Worker worker2 = new Worker("Julio", 5);
        Worker worker3 = new Worker("Peter", 10);
        Worker worker4 = new Worker("Juan", 15);
        worker1.setHours(100);
        worker2.setHours(10);
        worker3.setHours(50);
        worker4.setHours(40);
        team1.addWorkPart(worker1);
        team1.addWorkPart(team12);
        team12.addWorkPart(worker2);
        team2.addWorkPart(worker3);
        team2.addWorkPart(worker4);

        //addTeam()
        project.addTeam(team1);
        project.addTeam(team2);

        //getAllWorkers()
        List<Worker> projectWorkers = new ArrayList<>();
        projectWorkers.add(worker1); projectWorkers.add(worker2); projectWorkers.add(worker3);
        projectWorkers.add(worker4);

        assertEquals(projectWorkers, project.getAllWorkers());

        //getSummary()
        System.out.println(project.getSummary());

        //removeTeams()
        project.removeTeam(team1);
        assertThrows(IllegalCallerException.class, worker1::getCoworkers);

    }
}
