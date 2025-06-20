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
    CargoHold cargoHold;

    public GuiCargoHold(CargoHold cargoHold) {
        super(cargoHold);
        this.cargoHold = cargoHold;
        notifyContentChange();
    }

    @Override
    public void notifyContentChange() {
        updateContentBox(cargoHold.getGoods().entrySet().stream()
                .flatMap(entry ->
                        Collections.nCopies(entry.getValue(), switch (entry.getKey()) {
                            case RED -> Color.RED;
                            case BLUE -> Color.BLUE;
                            case GREEN -> Color.GREEN;
                            case YELLOW -> Color.GOLD;
                        }).stream())
                .collect(Collectors.toList())
        );
    }
}
