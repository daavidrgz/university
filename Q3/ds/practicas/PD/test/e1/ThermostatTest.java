package e1;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ThermostatTest {

    @Test
    public void thermostatTest() {
        Thermostat therm = new Thermostat(18);

        //getters
        assertEquals(18, therm.getCurrentTemperature());
        assertEquals(Off.class, therm.getMode().getClass());
        assertEquals(Thermostat.State.OFF, therm.getState());

        //newTemperature()
        therm.newTemperature(19);
        assertEquals(19 ,therm.getCurrentTemperature());

        //screenInfo()
        therm.changeToManual();
        assertEquals("19.0 ON M", therm.screenInfo());
        therm.changeToOff();
        assertEquals("19.0 OFF O", therm.screenInfo());
    }

    @Test
    public void timerModeTest() {
        Thermostat therm = new Thermostat(20);

        therm.changeToTimer(10);
        assertEquals(Timer.class, therm.getMode().getClass());
        assertEquals(Thermostat.State.ON, therm.getState());

        therm.newTemperature(17);
        assertEquals("17.0 ON T 5", therm.screenInfo());

        therm.newTemperature(16);
        assertEquals("16.0 OFF O", therm.screenInfo());
        assertEquals(Off.class, therm.getMode().getClass());

        therm.changeToTimer(15);
        therm.changeToProgram(20);
        assertEquals(Timer.class, therm.getMode().getClass());
        assertEquals(Thermostat.State.ON, therm.getState());

        assertThrows(IllegalArgumentException.class, () -> new Timer(-2));
        assertThrows(IllegalArgumentException.class, () -> new Timer(20000));
    }

    @Test
    public void programModeTest() {
        Thermostat therm = new Thermostat(20);
        therm.newTemperature(18);

        therm.changeToProgram(20);
        assertEquals(Program.class, therm.getMode().getClass());
        assertEquals(Thermostat.State.ON, therm.getState());
        assertEquals("18.0 ON P 20.0", therm.screenInfo());

        therm.newTemperature(19);
        assertEquals(Program.class, therm.getMode().getClass());
        assertEquals(Thermostat.State.ON, therm.getState());

        therm.newTemperature(20);
        assertEquals(Program.class, therm.getMode().getClass());
        assertEquals(Thermostat.State.OFF, therm.getState());

        therm.newTemperature(22);
        assertEquals(Program.class, therm.getMode().getClass());
        assertEquals(Thermostat.State.OFF, therm.getState());

        therm.changeToTimer(20);
        assertEquals(Program.class, therm.getMode().getClass());
        assertEquals(Thermostat.State.OFF, therm.getState());

        assertThrows(IllegalArgumentException.class, () -> new Program(0));
        assertThrows(IllegalArgumentException.class, () -> new Program(40));
    }

    @Test
    public void offManualModesTest() {
        Thermostat therm = new Thermostat(20);

        //Off Mode
        therm.changeToOff();
        assertEquals(Off.class, therm.getMode().getClass());
        assertEquals(Thermostat.State.OFF, therm.getState());

        //Manual Mode
        therm.changeToManual();
        assertEquals(Manual.class, therm.getMode().getClass());
        assertEquals(Thermostat.State.ON, therm.getState());
    }

    @Test
    public void logTest() {
        Thermostat therm = new Thermostat(19);
        therm.newTemperature(20);

        therm.changeToManual();
        therm.newTemperature(19);
        therm.changeToManual();

        therm.changeToTimer(5);
        therm.changeToTimer(3);

        therm.newTemperature(18);
        therm.changeToProgram(18);
        therm.changeToProgram(20);
        therm.newTemperature(17);

        therm.printAllLog();
    }
}
