package it.polimi.ingsw.galaxytruckers.view.cliElements.CliComponents;

import it.polimi.ingsw.galaxytruckers.view.DescriptionUtils;
import it.polimi.ingsw.galaxytruckers.view.cliElements.CliElement;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.Component;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ComponentBank;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CliComponentBank extends CliElement {

    private int coveredComponentsN;
    private final Map<Integer, CliComponent> cliComponentMap;
    private final List<CliComponent> uncoveredComponents;

    public CliComponentBank(ComponentBank componentBank) {
        this.cliComponentMap = new HashMap<>();
        this.coveredComponentsN = componentBank.getCoveredComponentsN();
        List<Component> modelUncovered =  componentBank.getUncoveredComponents();
        this.uncoveredComponents = new ArrayList<>();
        for (Component component : modelUncovered) {
            CliComponent cliComponent = new CliComponent(component);
            this.uncoveredComponents.add(cliComponent);
            this.cliComponentMap.put(component.getId(),cliComponent);
        }
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
        setDirty();
    }

    public void addUncovered(Component component) {
        uncoveredComponents.add(CliComponent.of(component));
        setDirty();
    }

    public void removeUncovered(Component component) {
        CliComponent cliComponent = cliComponentMap.get(component.getId());
        if (cliComponent != null) {
            cliComponentMap.remove(component.getId());
            uncoveredComponents.remove(cliComponent);
            setDirty();
        } else {
            // try to find it by checking equality instead
            for (CliComponent comp : new ArrayList<>(uncoveredComponents)) {
                if (comp.getId() == component.getId()) {
                    uncoveredComponents.remove(comp);
                    cliComponentMap.remove(component.getId());
                    setDirty();
                    break;
                }
            }
        }
    }

    public List<CliComponent> getUncoveredComponents() {
        return uncoveredComponents;
    }

    @Override
    protected List<String> getNewDescription() {
        StringBuilder row = new StringBuilder();

        List<String> coveredDescription = new ArrayList<>(DescriptionUtils.borderAndTitle(List.of("", String.valueOf(coveredComponentsN), ""), "down"));

        List<String> revealedDescription = new ArrayList<>();
        List<String> revealedDescriptionUnit = new ArrayList<>();
        int i = 0;
        for (CliComponent cliComponent : uncoveredComponents) {
            revealedDescriptionUnit.addAll(cliComponent.getNewDescription());
            revealedDescriptionUnit.add("  "+i+"  ");
            revealedDescription = DescriptionUtils.sideBySide(revealedDescription, revealedDescriptionUnit);
            revealedDescriptionUnit.clear();
            i++;
        }
        revealedDescription = DescriptionUtils.borderAndTitle(revealedDescription, "up");

        return DescriptionUtils.sideBySide(coveredDescription, revealedDescription);
    }
}
