package it.polimi.ingsw.galaxytruckers.network.messages;

import it.polimi.ingsw.galaxytruckers.serverController.events.Event;

public record EventMessage(Event event) implements Message {
}
