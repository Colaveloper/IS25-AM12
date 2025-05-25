package it.polimi.ingsw.galaxytruckers.view.cliElements.CliComponents;

import it.polimi.ingsw.galaxytruckers.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.view.enums.Highlights;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.Component;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.LifeSupport;

import java.util.List;

public class CliLifeSupport extends CliComponent {

    LifeSupport lifeSupport;
    public CliLifeSupport(ClientModel model, LifeSupport component) {
        super(model, component);
        this.lifeSupport = component;
    }

    @Override
    public List<String> getDescription() {
        String open;
        switch (lifeSupport.getAlienType()){
            case CrewType.PURPLE -> open = Highlights.PURPLE.getHighlight();
            case CrewType.BROWN -> open = Highlights.RED.getHighlight();
            case CrewType.HUMAN -> open = Highlights.WHITE.getHighlight();
            default -> open = Highlights.RESET.getHighlight();
        }
        return generateborders(open + " Ѫ " + Highlights.RESET.getHighlight());
    }
}
