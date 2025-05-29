package it.polimi.ingsw.galaxytruckers.view.cliElements.CliComponents;

import it.polimi.ingsw.galaxytruckers.view.DescriptionUtils;
import it.polimi.ingsw.galaxytruckers.view.cliElements.CliElement;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.Component;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ComponentBank;

import java.util.ArrayList;
import java.util.List;

public class CliComponentBank extends CliElement {

    private final ComponentBank componentBank;
    private int coveredComponentsN;
    private final List<Component> uncoveredComponents;

    public CliComponentBank(ComponentBank componentBank) {
        this.componentBank = componentBank;
        this.coveredComponentsN = componentBank.getCoveredComponentsN();
        this.uncoveredComponents = componentBank.getUncoveredComponents();
//        componentBank.getCoveredComponentsNProperty().addObserver(_ -> {
//            super.notifyObservers();
//        });
//
//        componentBank.getUncoveredComponentsProperty().getUnmodifiableView().forEach(component -> {
//            revealedComponents.put(component, CliComponent.of(component));
//        });
//        componentBank.getUncoveredComponentsProperty().addListener(new ObservableList.Listener<>() {
//            @Override
//            public void onAdd(int index, Component component) {
//                revealedComponents.put(component, CliComponent.of(component));
//                notifyObservers(); // Notify observers when component added to uncovered pile
//            }
//
//            @Override
//            public void onRemove(int index, Component component) {
//                revealedComponents.remove(component);
//                notifyObservers(); // Notify observers when component removed from uncovered pile
//            }
//        });
    }

    public void removeCovered() {
        coveredComponentsN--;
    }

    public void addUncovered(Component component) {
        uncoveredComponents.add(component);
    }

    public void removeUncovered(Component component) {
        uncoveredComponents.remove(component);
    }

    @Override
    protected List<String> getNewDescription() {
        StringBuilder row = new StringBuilder();

        List<String> coveredDescription = new ArrayList<>(DescriptionUtils.borderAndTitle(List.of("", String.valueOf(coveredComponentsN), ""), "down"));

        List<String> revealedDescription = new ArrayList<>();
        List<String> revealedDescriptionUnit = new ArrayList<>();
        int i = 0;
        List<CliComponent> components = componentBank.getUncoveredComponents().stream()
                .map(CliComponent::of).toList();
        for (CliComponent cliComponent : components) {
            revealedDescriptionUnit.addAll(cliComponent.getNewDescription());
            revealedDescriptionUnit.add("  "+i+"  ");
            revealedDescription = DescriptionUtils.sideBySide(revealedDescription, revealedDescriptionUnit);
            revealedDescriptionUnit.clear();
        }
        revealedDescription = DescriptionUtils.borderAndTitle(revealedDescription, "up");

        return DescriptionUtils.sideBySide(coveredDescription, revealedDescription);
    }
}
