package it.polimi.ingsw.galaxytruckers.view.guiElements;

import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.Component;
import javafx.scene.image.ImageView;

public class GuiAdventureCard extends ImageView {
    //    private boolean isRotatable = false;


    public GuiAdventureCard(int id) {
        super(GuiAdventureCardRegistry.getInstance().getImage(id));
        setFitWidth(60);
        setFitHeight(90);
        setPreserveRatio(true);
    }
}
