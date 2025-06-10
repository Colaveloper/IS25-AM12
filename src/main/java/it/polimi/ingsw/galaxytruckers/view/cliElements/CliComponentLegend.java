package it.polimi.ingsw.galaxytruckers.view.cliElements;

import it.polimi.ingsw.galaxytruckers.view.DescriptionUtils;
import it.polimi.ingsw.galaxytruckers.view.enums.ComponentType;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * A CLI element that displays a legend for all component symbols.
 * This element shows what each symbol in the game represents.
 */
public class CliComponentLegend extends CliElement {

    /**
     * Creates a new component legend CLI element.
     */
    public CliComponentLegend() {
        // No initialization needed
    }

    @Override
    protected List<String> getNewDescription() {
        // Create a map to organize components into columns
        Map<String, List<ComponentType>> columns = new LinkedHashMap<>();

        // Define columns and their components
        columns.put("Column1", List.of(
            ComponentType.SHIELD,
            ComponentType.LIFE_SUPPORT
        ));

        columns.put("Column2", List.of(
            ComponentType.DOUBLE_CANNON,
            ComponentType.DOUBLE_ENGINE
        ));

        columns.put("Column3", List.of(
            ComponentType.ENGINE,
            ComponentType.CANNON
        ));

        columns.put("Column4", List.of(
            ComponentType.CARGO_HOLD,
            ComponentType.SPECIAL_CARGO_HOLD
        ));

        columns.put("Column5", List.of(
            ComponentType.BATTERY,
            ComponentType.CABIN
        ));

        // Create the two rows of content
        List<String> row1 = new ArrayList<>();
        List<String> row2 = new ArrayList<>();

        // Fill the rows with components from each column
        for (List<ComponentType> column : columns.values()) {
            if (column.size() > 0) {
                ComponentType component1 = column.get(0);
                String symbol1 = component1.getSymbol(0);
                String name1 = formatComponentName(component1.name());
                row1.add(symbol1 + ":" + name1);
            }

            if (column.size() > 1) {
                ComponentType component2 = column.get(1);
                String symbol2 = component2.getSymbol(0);
                String name2 = formatComponentName(component2.name());
                row2.add(symbol2 + ":" + name2);
            } else {
                // Add an empty placeholder to maintain column alignment
                row2.add("");
            }
        }

        // Combine the components in each row with spacing
        String row1Text = String.join("  ", row1);
        String row2Text = String.join("  ", row2).trim();

        List<String> result = new ArrayList<>();
        result.add(row1Text);
        result.add(row2Text);

        // Add a border with title
        return DescriptionUtils.borderAndTitle(result, "components");
    }

    /**
     * Formats the component name to be more readable.
     * Converts from SNAKE_CASE to Title Case and replaces underscores with spaces.
     *
     * @param name The component name in SNAKE_CASE
     * @return The formatted component name
     */
    private String formatComponentName(String name) {
        // Replace underscores with spaces and convert to Title Case
        String[] words = name.split("_");
        StringBuilder result = new StringBuilder();

        for (String word : words) {
            if (word.isEmpty()) continue;

            // Convert first letter to uppercase and the rest to lowercase
            result.append(word.charAt(0))
                  .append(word.substring(1).toLowerCase())
                  .append(" ");
        }

        return result.toString().trim();
    }
}
