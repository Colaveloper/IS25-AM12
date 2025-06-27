package it.polimi.ingsw.galaxytruckers.client.view.cli.cliElements.CliComponents;

import it.polimi.ingsw.galaxytruckers.client.view.cli.DescriptionUtils;
import it.polimi.ingsw.galaxytruckers.client.view.cli.cliElements.CliElement;
import it.polimi.ingsw.galaxytruckers.client.model.shipBuilding.Component;
import it.polimi.ingsw.galaxytruckers.client.model.shipBuilding.ComponentBank;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents the component bank in the CLI.
 */
public class CliComponentBank extends CliElement {

    private int coveredComponentsN;
    private final Map<Integer, CliComponent> cliComponentMap;
    private final List<CliComponent> uncoveredComponents;

    /**
     * Creates a new CliComponentBank from the given ComponentBank.
     * It initializes the covered components count and maps the uncovered components to their IDs.
     *
     * @param componentBank The ComponentBank to convert into a CLI representation
     */
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
    }

    /**
     * Creates a new empty CliComponentBank.
     * Initializes the covered components count to 0 and prepares an empty list for uncovered components.
     */
    public void removeCovered() {
        coveredComponentsN--;
        setDirty();
    }

    /**
     * Adds a new uncovered component to the bank.
     * Increments the covered components count and adds the component to the list.
     *
     * @param component The component to add
     */
    public void addUncovered(Component component) {
        uncoveredComponents.add(CliComponent.of(component));
        setDirty();
    }

    /**
     * Removes an uncovered component from the bank.
     * If the component is found in the map, it is removed and the covered components count is decremented.
     * If not found, it searches through the list of uncovered components for equality.
     *
     * @param component The component to remove
     */
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

    /**
     * Returns the number of covered components in the bank.
     *
     * @return The number of covered components
     */
    public int getCoveredComponentsN() {
        return coveredComponentsN;
    }

    /**
     * Returns the list of uncovered components in the bank.
     *
     * @return List of CliComponent representing uncovered components
     */
    public List<CliComponent> getUncoveredComponents() {
        return uncoveredComponents;
    }

    @Override
    protected List<String> getNewDescription() {
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
