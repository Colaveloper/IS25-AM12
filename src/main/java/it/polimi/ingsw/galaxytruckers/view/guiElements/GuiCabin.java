package it.polimi.ingsw.galaxytruckers.view.guiElements;

import it.polimi.ingsw.galaxytruckers.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.Cabin;
import javafx.application.Platform;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.stream.IntStream;

public class GuiCabin extends GuiComponent {
    HBox crewBox;

    public GuiCabin(Cabin cabin) {
        super(cabin);
        crewBox = new HBox(2);
        for (int i = 0; i < cabin.getNumResidents(); i++) {
            Circle crewMember = switch (cabin.getCrewType()) {
                case PURPLE -> new Circle(4, Color.PURPLE);
                case BROWN -> new Circle(4, Color.BROWN);
                case HUMAN -> new Circle(4, Color.WHITE);
            };
            crewBox.getChildren().add(crewMember);
        }
        getChildren().add(crewBox);
    }

    public void notifyInitialize(CrewType crewType, int numResidents) {
        Platform.runLater(()->{
            crewBox.getChildren().clear();
            for (int i = 0; i < numResidents; i++) {
                Circle crewMember = switch (crewType) {
                    case PURPLE -> new Circle(4, Color.PURPLE);
                    case BROWN -> new Circle(4, Color.BROWN);
                    case HUMAN -> new Circle(4, Color.WHITE);
                };
                crewBox.getChildren().add(crewMember);
            }
        });
    }
}


