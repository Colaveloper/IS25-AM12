package it.polimi.ingsw.galaxytruckers.view.cliElements;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.FourColors;
import javafx.scene.paint.Color;

public class ColorUtils {
    public static String getDescription(FourColors color) {
        return switch (color) {
            case RED -> "red";
            case BLUE -> "blue";
            case GREEN -> "green";
            case YELLOW -> "yellow";
        };
    }

    public static Color getColor(FourColors color) {
        switch (color) {

        }
    }
}
