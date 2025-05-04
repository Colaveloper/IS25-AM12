package it.polimi.ingsw.galaxytruckers.model.enumTypes;

public enum Colors {
    RED("🔴"),
    BLUE("🔵"),
    YELLOW("🟡"),
    GREEN("🟢");

    private final String emoji;

    Colors(String emoji) {
        this.emoji = emoji;
    }

    public String getDescription() {
        return emoji;
    }
}