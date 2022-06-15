package e1;

public class Thermostat {
    enum State {
        OFF("Powered OFF"), ON("Powered ON");
        private final String s;
        State(String s) { this.s = s; }
        public String getExtendedName() { return s; }
    }
    private float currentTemperature;
    private ThermostatMode mode;
    private State state;
    private final ThermostatLog log;

    public Thermostat(float newTemperature) {
        this.currentTemperature = newTemperature;
        mode = new Off();
        state = State.OFF;
        log = new ThermostatLog();
    }

    public String screenInfo() {
        String s = mode.getInfo(this);
        System.out.println(s);
        return s;
    }

    public void changeToOff() {
        String s = mode.changeToOff(this);
        System.out.println(s);
        log.addToLog(s);
    }

    public void changeToManual() {
        String s = mode.changeToManual(this);
        System.out.println(s);
        log.addToLog(s);
    }

    public void changeToTimer(int time) {
        String s = mode.changeToTimer(this, time);
        System.out.println(s);
        log.addToLog(s);
    }

    public void changeToProgram(float aimTemperature) {
        String s = mode.changeToProgram(this ,aimTemperature);
        System.out.println(s);
        log.addToLog(s);
    }

    public void newTemperature(float currentTemperature) {
        this.currentTemperature = currentTemperature;
        String s = this.mode.updateState(this);
        System.out.println(s);
        log.addToLog(s);
    }

    public float getCurrentTemperature() {
        return this.currentTemperature;
    }
    public ThermostatMode getMode() {
        return this.mode;
    }
    public void setMode(ThermostatMode mode) {
        this.mode = mode;
    }
    public State getState() {
        return this.state;
    }
    public void setState(State state) {
        this.state = state;
    }
    public void printAllLog() {
        this.log.printLog();
    }

    private static class ThermostatLog {
        private final StringBuilder log;
        public ThermostatLog() {
            log = new StringBuilder();
        }
        public void addToLog(String s) {
            this.log.append("· ").append(s).append("\n");
        }
        public void printLog() {
            System.out.println("\n-_-_-_-_-_ log _-_-_-_-_-");
            System.out.print(this.log.toString());
            System.out.println("-_-_-_-_-_-_-_-_-_-_-_-_-_-\n");
        }
    }
}
