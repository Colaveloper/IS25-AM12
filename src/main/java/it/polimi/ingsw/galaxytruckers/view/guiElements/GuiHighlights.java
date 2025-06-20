package it.polimi.ingsw.galaxytruckers.view.guiElements;

import javafx.scene.paint.Color;
import java.util.ArrayList;
import java.util.List;

public enum GuiHighlights {

    RESET(null),
    RED(Color.RED),
    BLUE(Color.BLUE),
    YELLOW(Color.GOLD),
    GREEN(Color.GREEN),
    BLACK(Color.BLACK),
    WHITE(Color.WHITE),
    CYAN(Color.CYAN),
    MAGENTA(Color.MAGENTA),
    GRAY(Color.GRAY),
    PURPLE(Color.PURPLE);

    private final Color color;

    GuiHighlights(Color color) {
        this.color = color;
    }

    /** Returns the JavaFX Color associated, or null for RESET (no color). */
    public Color getColor() {
        return color;
    }

    /**
     * Returns the first (i+1) colors as a list.
     * @param i index (0-based)
     */
    public static List<GuiHighlights> getSomeColors(int i) {
        List<GuiHighlights> someColors = new ArrayList<>(List.of(GuiHighlights.values()));
        return someColors.subList(0, Math.min(i + 1, someColors.size()));
    }
}
