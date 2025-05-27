package it.polimi.ingsw.galaxytruckers.view.cliElements.CliComponents;

import it.polimi.ingsw.galaxytruckers.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.view.enums.Highlights;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.Cabin;

import java.util.List;

public class CliCabin extends CliComponent{
    
    Cabin cabin;
    
    public CliCabin(Cabin component) {
        super(component);
        this.cabin = component;
    }

    @Override
    public List<String> getNewDescription() {
        String open;
        switch (cabin.getCrewType()){
            case CrewType.PURPLE -> {
                open = Highlights.PURPLE.getHighlight();
            }
            case CrewType.BROWN -> {
                open = Highlights.RED.getHighlight();
            }
            case CrewType.HUMAN -> {
                open = Highlights.WHITE.getHighlight();
            }
            default -> {
                open = Highlights.RESET.getHighlight();
            }
        };
        return addBorders(open + " ⌂" + cabin.getNumResidents() + Highlights.RESET.getHighlight());
    }
}
