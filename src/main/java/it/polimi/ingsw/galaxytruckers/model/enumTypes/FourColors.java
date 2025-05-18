package it.polimi.ingsw.galaxytruckers.model.enumTypes;

import javafx.scene.paint.Color;

public enum FourColors {
    RED("🔴", Color.RED),
    BLUE("🔵", Color.BLUE),
    YELLOW("🟡", Color.YELLOW),
    GREEN("🟢", Color.GREEN);

    private final String emoji;
    private final Color jfxColor;

    FourColors(String emoji, Color color) {
        this.emoji = emoji;
        this.jfxColor = color;
    }

    public String getDescription() {
        return emoji;
    }

    public Color getJfxColor() {return jfxColor;}
}