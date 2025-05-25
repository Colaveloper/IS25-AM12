package it.polimi.ingsw.galaxytruckers.view.cliElements;

import it.polimi.ingsw.galaxytruckers.view.DescriptionUtils;
import it.polimi.ingsw.galaxytruckers.view.cliElements.CliComponents.CliComponent;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.Component;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ComponentBank;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;

import java.util.ArrayList;
import java.util.List;

public class CliComponentBank extends CliElement {


    private final ShipBoard[] forecastDeck;
    private final int coveredComponentN;
    private List<CliComponent> revealedComponents;

    public CliComponentBank(ClientModel model) {
        super(model);
        ComponentBank componentBank = model.getGame().getCurrentState().getComponentBank();
        this.forecastDeck = model.getGame().getCurrentState().getLockedForecasts();
        this.coveredComponentN = componentBank.getCoveredComponentsN();
        for(Component component : componentBank.getUncoveredComponents()){
            revealedComponents.add(new CliComponent(model, component));
        }
    }

    @Override
    public List<String> getDescription() {
        String padding = "  ";
        StringBuilder row = new StringBuilder();
        List<String> description = new ArrayList<>();

        description.add("Face down: " + coveredComponentN);

        description.add("Face up: ");
        for (int i = 0; i < 3; i++) {
            for (CliComponent cliComponent : revealedComponents) {
                row.append(cliComponent.getDescription().get(i));
                row.append(padding);
            }
            description.add(row.toString());
            row.setLength(0);
        }
        for (int n = 1; n <= revealedComponents.size(); n++) {
            row.append("  ").append(n).append("  ").append(padding);
        }
        description.add(row.toString());

        if (forecastDeck != null) {
            List<String> forecast = new ArrayList<>();
            forecast.add("  deck 1    deck 2    deck 3  ");
            forecast.add(
                    (forecastDeck[0] == null ? "available " : forecastDeck[0].getColor().toString()) +
                    (forecastDeck[1] == null ? "available " : forecastDeck[1].getColor().toString()) +
                    (forecastDeck[2] == null ? "available " : forecastDeck[2].getColor().toString())
            );
            description.addAll(DescriptionUtils.borderAndTitle(forecast, "forecast decks"));
        }

        return description;
    }
}
