package it.polimi.ingsw.galaxytruckers.serverController.events;

import java.awt.*;

public record LoseCrewEvent(String playerName, Point point) implements Event{
}
