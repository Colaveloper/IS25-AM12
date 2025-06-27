package it.polimi.ingsw.galaxytruckers.client.view.cli.cliElements.CliComponents;

import it.polimi.ingsw.galaxytruckers.shared.enums.GoodsType;
import it.polimi.ingsw.galaxytruckers.client.view.cli.CliHighlights;
import it.polimi.ingsw.galaxytruckers.client.model.shipBuilding.CargoHold;

import java.util.List;
import java.util.Map;

/**
 * Represents a CargoHold in the CLI, displaying its contents and status.
 */
public class CliCargoHold extends CliComponent {

    private final CargoHold cargoHold;
    private final List<String> symbols;

    /**
     * Constructs a CLI representation of a CargoHold component.
     *
     * @param component the CargoHold component to be represented
     */
    public CliCargoHold(CargoHold component) {
        super(component);
        this.cargoHold = component;
        if (component.isSpecial()){
            symbols = List.of("○","●");
        }
        else {
            symbols = List.of("□", "■");
        }
    }

    @Override
    protected List<String> getNewDescription() {
        Map<GoodsType, Integer> goodMap = cargoHold.getGoods();
        StringBuilder result = new StringBuilder();
        String open;
        int placed = 0;

        for(GoodsType type : goodMap.keySet()) {
            switch (type) {
                case GoodsType.RED -> {
                    open = CliHighlights.RED.getHighlight();
                }
                case GoodsType.GREEN -> {
                    open = CliHighlights.GREEN.getHighlight();
                }
                case GoodsType.BLUE -> {
                    open = CliHighlights.BLUE.getHighlight();
                }
                case GoodsType.YELLOW -> {
                    open = CliHighlights.YELLOW.getHighlight();
                }
                default -> open = CliHighlights.RESET.getHighlight();
            }
            for(int i = 0; i < goodMap.get(type); i++) {
                result.append(open).append(symbols.get(1)).append(CliHighlights.RESET.getHighlight());
                placed++;
            }
        }
        result.append(symbols.get(0).repeat(cargoHold.getSize() - placed));
        result.append(" ".repeat(3 - cargoHold.getSize()));

        return addBorders(result.toString());
    }
}

