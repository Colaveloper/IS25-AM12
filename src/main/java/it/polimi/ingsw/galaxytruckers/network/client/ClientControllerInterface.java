package it.polimi.ingsw.galaxytruckers.network.client;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.GameColor;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.StatType;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.serverController.events.Event;
import it.polimi.ingsw.galaxytruckers.view.enums.ProjectileType;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.Set;

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