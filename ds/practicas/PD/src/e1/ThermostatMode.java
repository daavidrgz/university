package e1;

public interface ThermostatMode {
    String getInfo(Thermostat thermostat);
    String updateState(Thermostat thermostat);

    default String changeToOff(Thermostat thermostat) {
        String s;
        if ( thermostat.getMode() instanceof Off ) {
            s = "Thermostat already in Off mode";
            return s;
        }
        s = "Mode changed to Off";
        thermostat.setState(Thermostat.State.OFF);
        thermostat.setMode(new Off());
        return s;
    }

    default String changeToManual(Thermostat thermostat) {
        String s;
        if ( thermostat.getMode() instanceof Manual ) {
            s = "Thermostat already in Manual mode";
            return s;
        }
        s = "Mode changed to Manual";
        thermostat.setState(Thermostat.State.ON);
        thermostat.setMode(new Manual());
        return s;
    }

    default String changeToTimer(Thermostat thermostat, int time) {
        String s;
        if ( thermostat.getMode() instanceof Timer )
            s = "Reset timer to " + time + " minutes";
        else
            s = "Mode changed to Timer for" + time + " minutes";

        thermostat.setState(Thermostat.State.ON);
        thermostat.setMode(new Timer(time));
        return s;
    }

    default String changeToProgram(Thermostat thermostat, float aimTemperature) {
        String s;
        if ( thermostat.getMode() instanceof Program )
            s = "Aim temperature changed to " + aimTemperature + " ºC";
        else
            s = "Mode changed to Program at " + aimTemperature + " ºC";

        if ( thermostat.getCurrentTemperature() < aimTemperature)
            thermostat.setState(Thermostat.State.ON);
        else
            thermostat.setState(Thermostat.State.OFF);

        thermostat.setMode(new Program(aimTemperature));
        return s;
    }
}
