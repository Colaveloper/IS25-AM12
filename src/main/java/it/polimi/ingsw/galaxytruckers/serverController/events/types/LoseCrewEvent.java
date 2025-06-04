package it.polimi.ingsw.galaxytruckers.serverController.events.types;

import java.awt.*;

public record LoseCrewEvent(String playerName, Point point) implements LobbyEvent {
}
