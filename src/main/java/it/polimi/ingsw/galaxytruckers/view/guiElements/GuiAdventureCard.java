package it.polimi.ingsw.galaxytruckers.view.guiElements;

import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.Component;
import javafx.scene.image.ImageView;

/**
 * GUI representation of an adventure card in the game.
 * <p>
 * This class extends {@link ImageView} and displays the image associated with a specific adventure card ID.
 * The image is retrieved from the {@link GuiAdventureCardRegistry} singleton.
 * </p>
 */
public class GuiAdventureCard extends ImageView {
    /**
     * Constructs a GuiAdventureCard with the specified card ID.
     *
     * @param id the unique identifier of the adventure card image to display
     */
    public GuiAdventureCard(int id) {
        super(GuiAdventureCardRegistry.getInstance().getImage(id));
        setFitHeight(90);
        setPreserveRatio(true);
    }
}
