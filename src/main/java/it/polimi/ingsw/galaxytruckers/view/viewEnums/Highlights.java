package it.polimi.ingsw.galaxytruckers.view.viewEnums;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public enum Highlights {

            RESET ("\u001b[0m"),
            RED ("\u001b[31m"),
            BLUE ("\u001b[34m"),
            YELLOW ("\u001b[33m"),
            GREEN ("\u001b[32m"),
            BLACK ("\u001b[30m"),
            WHITE ("\u001b[37m"),
            CYAN ("\u001b[36m"),
            MAGENTA ("\u001b[35m"),
            GRAY ("\u001b[90m"),
            PURPLE ("\u001B[45m");



    private final String color;

    Highlights(String color) {
        this.color = color;
    }

    public String getHighlight() {
        return color;
    }

    public static List<Highlights> getSomeColors(int i) {
        List<Highlights> someColors = new ArrayList<Highlights>(List.of(Highlights.values()));
        return someColors.subList(0, i + 1);
    }

}
