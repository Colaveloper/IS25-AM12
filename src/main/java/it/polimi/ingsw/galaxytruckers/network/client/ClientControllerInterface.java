package it.polimi.ingsw.galaxytruckers.network.client;

import it.polimi.ingsw.galaxytruckers.serverController.events.types.Event;

/**
 * Interface for client-side controller used to receive updates from the server
 * during the game lifecycle in Galaxy Truckers.
 */
public interface ClientControllerInterface {

    void notifyEvent(Event event);

       /**
     * Reports an error to the client.
     * //todo i don t know how exceptions are used for the connection but this had to be removed(?)
     *
     * @param details a message describing the error
     */
    void reportError(String details);

}