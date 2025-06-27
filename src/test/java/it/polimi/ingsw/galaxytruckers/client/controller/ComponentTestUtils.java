package it.polimi.ingsw.galaxytruckers.client.controller;

import it.polimi.ingsw.galaxytruckers.client.model.shipBuilding.*;

public class ComponentTestUtils {
    public static int findFirstComponentIdOfType(Class<?> componentType) {
        ComponentRegistry registry = ComponentRegistry.getInstance();
        // Try IDs from 1 to 200 (assuming this covers all components)
        for (int i = 1; i <= 200; i++) {
            try {
                Component component = registry.getComponent(i);
                if (componentType.isInstance(component)) {
                    return i;
                }
            } catch (IllegalArgumentException ignored) {
                // ID doesn't exist, continue searching
            }
        }
        throw new IllegalStateException("No component found of type: " + componentType.getSimpleName());
    }
}
