package e1;

public class Manual implements ThermostatMode {
    public Manual() { }

    @Override
    public String getInfo(Thermostat thermostat) {
        return thermostat.getCurrentTemperature() + " " + thermostat.getState().name() + " M";
    }

    @Override
    public String updateState(Thermostat thermostat) {
        return thermostat.getCurrentTemperature() + " ºC | Mode: " + this.toString() +
                " - Heating " + thermostat.getState().getExtendedName();
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
