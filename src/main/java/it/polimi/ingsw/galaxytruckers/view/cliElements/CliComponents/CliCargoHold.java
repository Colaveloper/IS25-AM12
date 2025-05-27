package it.polimi.ingsw.galaxytruckers.view.cliElements.CliComponents;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.view.enums.Highlights;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.CargoHold;

import java.util.List;
import java.util.Map;

public class CliCargoHold extends CliComponent {

    private final CargoHold cargoHold;
    private final List<String> symbols;

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
    public List<String> getNewDescription() {
        Map<GoodsType, Integer> goodMap = cargoHold.getGoods();
        StringBuilder result = new StringBuilder();
        String open;
        int placed = 0;

        for(GoodsType type : goodMap.keySet()) {
            switch (type) {
                case GoodsType.RED -> {
                    open = Highlights.RED.getHighlight();
                }
                case GoodsType.GREEN -> {
                    open = Highlights.GREEN.getHighlight();
                }
                case GoodsType.BLUE -> {
                    open = Highlights.BLUE.getHighlight();
                }
                case GoodsType.YELLOW -> {
                    open = Highlights.YELLOW.getHighlight();
                }
                default -> open = Highlights.RESET.getHighlight();
            }
            for(int i = 0; i < goodMap.get(type); i++) {
                result.append(open).append(symbols.get(1)).append(Highlights.RESET.getHighlight());
                placed++;
            }
        }
        result.append(symbols.get(0).repeat(cargoHold.getSize() - placed));
        result.append(" ".repeat(3 - cargoHold.getSize()));

        return addBorders(result.toString());
    }
}

