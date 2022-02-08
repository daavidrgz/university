package e1;

public class Program implements ThermostatMode {
    private final float aimTemperature;

    public Program(float aimTemperature) throws IllegalArgumentException {
        if ( aimTemperature <= 5 || aimTemperature >= 35 )
            throw new IllegalArgumentException("Invalid temperature");
        this.aimTemperature = aimTemperature;
    }

    @Override
    public String getInfo(Thermostat thermostat) {
        return thermostat.getCurrentTemperature() + " " + thermostat.getState().name() + " P " + this.aimTemperature;
    }

    @Override
    public String updateState(Thermostat thermostat) {
        if ( thermostat.getCurrentTemperature() < this.aimTemperature)
            thermostat.setState(Thermostat.State.ON);
        else
            thermostat.setState(Thermostat.State.OFF);

        return thermostat.getCurrentTemperature() + " ºC | Mode: " + this.toString() +
                " (at " + this.aimTemperature + " ºC) - Heating " + thermostat.getState().getExtendedName();
    }

     @Override
     public String changeToTimer(Thermostat thermostat, int time) {
        return "Impossible to change to Timer mode";
     }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
