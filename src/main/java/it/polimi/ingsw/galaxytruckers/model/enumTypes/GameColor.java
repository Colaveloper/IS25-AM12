package it.polimi.ingsw.galaxytruckers.model.enumTypes;

import javafx.scene.paint.Color;

public enum GameColor {
    RED("\u001B[31m●\u001B[0m", Color.RED),
    BLUE("\u001B[34m●\u001B[0m", Color.BLUE),
    YELLOW("\u001B[33m●\u001B[0m", Color.YELLOW),
    GREEN("\u001B[32m●\u001B[0m", Color.GREEN);

    private final String emoji;
    private final Color jfxColor;

    GameColor(String emoji, Color color) {
        this.emoji = emoji;
        this.jfxColor = color;
    }

    public String getDescription() {
        return emoji;
    }

    public Color getJfxColor() {
        return jfxColor;
    }
}
