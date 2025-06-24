package it.polimi.ingsw.galaxytruckers.view.guiElements;

import javafx.scene.paint.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * Enum representing highlight colors for GUI elements.
 * <p>
 * Each value corresponds to a JavaFX {@link Color}, or null for RESET (no color).
 * Provides utility methods for retrieving the associated color and a subset of highlight colors.
 * </p>
 */
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

    /**
     * Returns the JavaFX Color associated, or null for RESET (no color).
     *
     * @return the JavaFX Color, or null if RESET
     */
    public Color getColor() {
        return color;
    }

    /**
     * Returns the first (i+1) colors as a list.
     *
     * @param i index (0-based)
     * @return a list of the first (i+1) GuiHighlights
     */
    public static List<GuiHighlights> getSomeColors(int i) {
        List<GuiHighlights> someColors = new ArrayList<>(List.of(GuiHighlights.values()));
        return someColors.subList(0, Math.min(i + 1, someColors.size()));
    }
}
