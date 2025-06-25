package it.polimi.ingsw.galaxytruckers.view.guiElements;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.GameColor;
import javafx.scene.paint.Color;

public class GameColorGuiMapper {
    public static Color toJfxColor(GameColor color) {
        return switch (color) {
            case RED -> Color.RED;
            case BLUE -> Color.BLUE;
            case YELLOW -> Color.YELLOW;
            case GREEN -> Color.GREEN;
        };
    }
}