package it.polimi.ingsw.galaxytruckers.view.model.state;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;

import java.util.List;

public class AdventureState extends GameState {
    @Override
    public List<StateActions> getAvailableActions() {
        return List.of(StateActions.GIVE_UP);
    }
}
