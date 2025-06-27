package it.polimi.ingsw.galaxytruckers.view.cliElements.CliComponents;

import it.polimi.ingsw.galaxytruckers.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.view.enums.CliHighlights;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.LifeSupport;

import java.util.List;

/**
 * Represents a CLI component for displaying the Life Support system in the Galaxy Truckers game.
 */
public class CliLifeSupport extends CliComponent {

    private final LifeSupport lifeSupport;

    /**
     * Creates a new CLI representation of a Life Support component.
     *
     * @param component the LifeSupport component to be represented
     */
    public CliLifeSupport(LifeSupport component) {
        super(component);
        this.lifeSupport = component;
    }

    @Override
    protected List<String> getNewDescription() {
        String open;
        switch (lifeSupport.getAlienType()){
            case CrewType.PURPLE -> open = CliHighlights.MAGENTA.getHighlight();
            case CrewType.BROWN -> open = CliHighlights.YELLOW.getHighlight();
            case CrewType.HUMAN -> open = CliHighlights.WHITE.getHighlight();
            default -> open = CliHighlights.RESET.getHighlight();
        }
        return addBorders(open + " Ѫ " + CliHighlights.RESET.getHighlight());
    }
}
