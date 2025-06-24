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

/**
 * GUI representation of a cargo hold component in the ship.
 * <p>
 * This class visually represents a {@link CargoHold} in the GUI, showing the stored goods as colored circles.
 * Each color corresponds to a different {@link GoodsType}. The GUI updates automatically when the cargo hold's contents change.
 * </p>
 */
public class GuiCargoHold extends GuiComponent {
    /**
     * The underlying cargo hold model.
     */
    CargoHold cargoHold;

    /**
     * Constructs a GuiCargoHold for the given cargo hold model.
     *
     * @param cargoHold the cargo hold model to represent
     */
    public GuiCargoHold(CargoHold cargoHold) {
        super(cargoHold);
        this.cargoHold = cargoHold;
        notifyContentChange();
    }

    /**
     * Updates the GUI to reflect the current goods stored in the cargo hold.
     */
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
