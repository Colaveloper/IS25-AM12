package it.polimi.ingsw.galaxytruckers.view.guiElements;

import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.Cabin;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Collections;

public class GuiCabin extends GuiComponent {
    public final Cabin cabin;

    public GuiCabin(Cabin cabin) {
        super(cabin);
        this.cabin = cabin;
        notifyContentChange();
    }

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

//    public void notifyInitialize(CrewType crewType, int numResidents) {
//        updateContentBox(new ArrayList<>(Collections.nCopies(
//                numResidents,
//                switch (crewType) {
//                    case PURPLE -> Color.PURPLE;
//                    case BROWN -> Color.BROWN;
//                    case HUMAN -> Color.WHITE;
//                }
//        )));
//    }

