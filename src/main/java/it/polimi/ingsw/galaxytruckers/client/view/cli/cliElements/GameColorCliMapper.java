package it.polimi.ingsw.galaxytruckers.client.view.cli.cliElements;

import it.polimi.ingsw.galaxytruckers.shared.enums.GameColor;

public class GameColorCliMapper {
    public static String toAnsiBullet(GameColor color) {
        return switch (color) {
            case RED -> "\u001B[31m●\u001B[0m";
            case BLUE -> "\u001B[34m●\u001B[0m";
            case YELLOW -> "\u001B[33m●\u001B[0m";
            case GREEN -> "\u001B[32m●\u001B[0m";
        };
    }
}