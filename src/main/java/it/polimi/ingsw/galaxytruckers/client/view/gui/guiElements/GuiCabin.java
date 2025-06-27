package it.polimi.ingsw.galaxytruckers.client.view.gui.guiElements;

import it.polimi.ingsw.galaxytruckers.client.model.shipBuilding.Cabin;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Collections;

/**
 * GUI representation of a cabin component in the ship.
 * <p>
 * This class visually represents a {@link Cabin} in the GUI, showing the number and type of residents as colored circles.
 * It extends {@link GuiComponent} and updates its content whenever the cabin state changes.
 * </p>
 */
public class GuiCabin extends GuiComponent {
    /**
     * The underlying cabin model.
     */
    public final Cabin cabin;

    /**
     * Constructs a GuiCabin for the given cabin model.
     *
     * @param cabin the cabin model to represent
     */
    public GuiCabin(Cabin cabin) {
        super(cabin);
        this.cabin = cabin;
        notifyContentChange();
    }

    /**
     * Updates the GUI to reflect the current number and type of residents in the cabin.
     */
    public void notifyContentChange() {
        updateContentBox(new ArrayList<>(Collections.nCopies(
                cabin.getNumResidents(),
                switch (cabin.getCrewType()) {
                    case PURPLE -> Color.PURPLE;
                    case BROWN -> Color.BROWN;
                    case HUMAN -> Color.WHITE;
                }
        )));
    }
}
