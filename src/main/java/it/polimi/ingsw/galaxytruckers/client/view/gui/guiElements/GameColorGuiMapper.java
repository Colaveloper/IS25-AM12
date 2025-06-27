package it.polimi.ingsw.galaxytruckers.client.view.gui.guiElements;

import it.polimi.ingsw.galaxytruckers.shared.enums.GameColor;
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