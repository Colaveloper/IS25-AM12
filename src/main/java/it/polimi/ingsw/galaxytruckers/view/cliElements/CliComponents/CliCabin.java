package it.polimi.ingsw.galaxytruckers.view.cliElements.CliComponents;

import it.polimi.ingsw.galaxytruckers.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.view.enums.CliHighlights;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.Cabin;

import java.util.List;

public class CliCabin extends CliComponent{
    
    private final Cabin cabin;
    
    public CliCabin(Cabin component) {
        super(component);
        this.cabin = component;
    }

    @Override
    protected List<String> getNewDescription() {
        String open;
        switch (cabin.getCrewType()){
            case CrewType.PURPLE -> {
                open = CliHighlights.MAGENTA.getHighlight();
            }
            case CrewType.BROWN -> {
                open = CliHighlights.YELLOW.getHighlight();
            }
            case CrewType.HUMAN -> {
                open = CliHighlights.WHITE.getHighlight();
            }
            default -> {
                open = CliHighlights.RESET.getHighlight();
            }
        };
        return addBorders(open + " ⌂" + cabin.getNumResidents() + CliHighlights.RESET.getHighlight());
    }
}
