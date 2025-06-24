package it.polimi.ingsw.galaxytruckers.view.enums;

import java.util.ArrayList;
import java.util.List;

/**
 * Enum representing ANSI color codes for CLI highlights.
 * <p>
 * Each value corresponds to an ANSI escape code for coloring text in the command-line interface.
 * Provides utility methods for retrieving the color code and a subset of highlight colors.
 * </p>
 */
public enum CliHighlights {

       /*0*/RESET   ("\u001b[0m"),
       /*1*/RED     ("\u001b[31m"),
       /*2*/BLUE    ("\u001b[34m"),
       /*3*/YELLOW  ("\u001b[33m"),
       /*4*/GREEN   ("\u001b[32m"),
       /*5*/BLACK   ("\u001b[30m"),
       /*6*/WHITE   ("\u001b[37m"),
       /*7*/CYAN    ("\u001b[36m"),
       /*8*/MAGENTA ("\u001b[35m"),
       /*9*/GRAY    ("\u001b[90m"),
      /*10*/PURPLE  ("\u001B[45m");



    private final String color;

    CliHighlights(String color) {
        this.color = color;
    }

    /**
     * Returns the ANSI escape code for this highlight color.
     *
     * @return the ANSI color code string
     */
    public String getHighlight() {
        return color;
    }

    /**
     * Returns the first (i+1) colors as a list.
     *
     * @param i index (0-based)
     * @return a list of the first (i+1) CliHighlights
     */
    public static List<CliHighlights> getSomeColors(int i) {
        List<CliHighlights> someColors = new ArrayList<CliHighlights>(List.of(CliHighlights.values()));
        return someColors.subList(0, i + 1);
    }
}
