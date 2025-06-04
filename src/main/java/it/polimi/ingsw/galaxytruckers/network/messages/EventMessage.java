package it.polimi.ingsw.galaxytruckers.network.messages;

import it.polimi.ingsw.galaxytruckers.serverController.events.types.Event;

public record EventMessage(Event event) implements Message {
}
