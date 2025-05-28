package it.polimi.ingsw.galaxytruckers.view.cliElements.CliComponents;

import it.polimi.ingsw.galaxytruckers.view.observables.ObservableList;
import it.polimi.ingsw.galaxytruckers.view.cliElements.CliAdventureCard;
import it.polimi.ingsw.galaxytruckers.view.cliElements.CliElement;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.Component;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ComponentBank;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CliComponentBank extends CliElement {

    private final ComponentBank componentBank;
    private final Map<Component, CliComponent> revealedComponents = new HashMap<>();

    public CliComponentBank(ComponentBank componentBank) {

        this.componentBank = componentBank;

        componentBank.getCoveredComponentsNProperty().addObserver(_ -> {
            super.notifyObservers();
        });

        componentBank.getUncoveredComponentsProperty().getUnmodifiableView().forEach(component -> {
            revealedComponents.put(component, CliComponent.of(component));
        });
        componentBank.getUncoveredComponentsProperty().addListener(new ObservableList.Listener<>() {
            @Override
            public void onAdd(int index, Component component) {
                revealedComponents.put(component, CliComponent.of(component));
            }

            @Override
            public void onRemove(int index, Component component) {
                revealedComponents.remove(component);
            }
        });
    }

    @Override
    protected List<String> getNewDescription() {
        String padding = "  ";
        StringBuilder row = new StringBuilder();
        List<String> description = new ArrayList<>();

        description.add("Face down: " + componentBank.getCoveredComponentsNProperty().getValue());

        description.add("Face up: ");
        for (int i = 0; i < 3; i++) {
            for (CliComponent cliComponent : revealedComponents.values()) {
                row.append(cliComponent.getNewDescription().get(i));
                row.append(padding);
            }
            description.add(row.toString());
            row.setLength(0);
        }
        for (int n = 1; n <= revealedComponents.size(); n++) {
            row.append("  ").append(n).append("  ").append(padding);
        }
        description.add(row.toString());

        return description;
    }
}
