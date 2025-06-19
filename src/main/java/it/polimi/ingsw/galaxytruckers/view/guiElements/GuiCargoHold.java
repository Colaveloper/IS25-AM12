package it.polimi.ingsw.galaxytruckers.view.guiElements;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.CargoHold;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

public class GuiCargoHold extends GuiComponent {

    public GuiCargoHold(CargoHold cargoHold) {
        super(cargoHold);
        updateContentBox(cargoHold.getGoods().entrySet().stream()
                .flatMap(entry ->
                        Collections.nCopies(entry.getValue(), switch (entry.getKey()) {
                            case RED -> Color.RED;
                            case BLUE -> Color.BLUE;
                            case GREEN -> Color.GREEN;
                            case YELLOW -> Color.YELLOW;
                        }).stream())
                .collect(Collectors.toList())
        );
    }
}
