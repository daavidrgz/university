package e1;

public class Timer implements ThermostatMode {
    private int time;

    public Timer(int time) throws IllegalArgumentException{
        if ( time <= 0 || time >= 1440 )
            throw new IllegalArgumentException("Invalid time");
        this.time = time;
    }

    @Override
    public String getInfo(Thermostat thermostat) {
        return thermostat.getCurrentTemperature() + " " + thermostat.getState().name() + " T " + this.time;
    }

    @Override
    public String updateState(Thermostat thermostat) {
        this.time = this.time - 5;
        if ( this.time <= 0 ) {
            thermostat.setState(Thermostat.State.OFF);
            thermostat.setMode(new Off());
            return "Timer mode deactivated";
        }
        return thermostat.getCurrentTemperature() + " ºC | Mode: " + this.toString() +
                " (" + this.time + " minutes left) - Heating " + thermostat.getState().getExtendedName();
    }

    @Override
    public String changeToProgram(Thermostat thermostat, float aimTemperature) {
        return "Impossible to change to Program mode";
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
