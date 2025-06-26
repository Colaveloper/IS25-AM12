package it.polimi.ingsw.galaxytruckers.network.messages;

import it.polimi.ingsw.galaxytruckers.serverController.events.types.Event;

/**
 * Message that contains an event to be sent to the client.
 * @param event the event to be sent
 */
public record EventMessage(Event event) implements Message {
}
