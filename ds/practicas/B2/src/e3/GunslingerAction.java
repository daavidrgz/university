package e3;

public enum GunslingerAction {
    RELOAD("RELOAD"),
    SHOOT("SHOOT"),
    PROTECT("PROTECT"),
    MACHINE_GUN("MACHINE GUN");

    private String name;

    GunslingerAction (String name) {
        this.name = name;
    }
    public String getName() {
        return this.name;
    }
}
