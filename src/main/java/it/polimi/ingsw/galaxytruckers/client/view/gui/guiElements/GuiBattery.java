package it.polimi.ingsw.galaxytruckers.client.view.gui.guiElements;

import it.polimi.ingsw.galaxytruckers.client.model.shipBuilding.Battery;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Collections;

/**
 * GUI representation of a battery component in the ship.
 * <p>
 * This class visually represents a {@link Battery} in the GUI, showing the number of batteries as colored circles.
 * It extends {@link GuiComponent} and updates its content whenever the battery state changes.
 * </p>
 */
public class GuiBattery extends GuiComponent {
    /**
     * The underlying battery model.
     */
    private final Battery battery;

    /**
     * Constructs a GuiBattery for the given battery model.
     *
     * @param battery the battery model to represent
     */
    public GuiBattery(Battery battery) {
        super(battery);
        this.battery = battery;
        notifyContentChange();
    }

    /**
     * Updates the GUI to reflect the current number of batteries.
     */
    public void notifyContentChange() {
        updateContentBox(new ArrayList<>(Collections.nCopies(
                battery.getNumBatteries(),
                Color.GREEN
        )));
    }
}
