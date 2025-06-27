package it.polimi.ingsw.galaxytruckers.client.view.cli.cliElements.CliComponents;

import it.polimi.ingsw.galaxytruckers.server.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.client.view.cli.CliHighlights;
import it.polimi.ingsw.galaxytruckers.client.model.shipBuilding.Cabin;

import java.util.List;

/**
 * Represents a CLI representation of a Cabin component.
 */
public class CliCabin extends CliComponent{
    
    private final Cabin cabin;

    /**
     * Constructs a CLI representation of a Cabin component.
     *
     * @param component the Cabin component to be represented in the CLI
     */
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
