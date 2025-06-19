package it.polimi.ingsw.galaxytruckers.view.guiElements;

import it.polimi.ingsw.galaxytruckers.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.Cabin;
import javafx.application.Platform;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.IntStream;

public class GuiCabin extends GuiComponent {

    public GuiCabin(Cabin cabin) {
        super(cabin);
        updateContentBox(new ArrayList<>(Collections.nCopies(
                cabin.getNumResidents(),
                switch (cabin.getCrewType()) {
                    case PURPLE -> Color.PURPLE;
                    case BROWN -> Color.BROWN;
                    case HUMAN -> Color.WHITE;
                }
        )));
    }

    public void notifyInitialize(CrewType crewType, int numResidents) {
        updateContentBox(new ArrayList<>(Collections.nCopies(
                numResidents,
                switch (crewType) {
                    case PURPLE -> Color.PURPLE;
                    case BROWN -> Color.BROWN;
                    case HUMAN -> Color.WHITE;
                }
        )));
    }

    public void notifyCrewLoss() {
        // todo: implement and use
    }
}


