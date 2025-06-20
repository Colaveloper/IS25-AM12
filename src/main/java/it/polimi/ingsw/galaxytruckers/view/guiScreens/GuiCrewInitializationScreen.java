package it.polimi.ingsw.galaxytruckers.view.guiScreens;

import it.polimi.ingsw.galaxytruckers.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.view.model.state.ShipInitializationState;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GuiCrewInitializationScreen extends GuiGameScreen {
    private CrewType currentCrewType;
    private final Map<CrewType, Set<Point>> crewTypeToPoints;

    public GuiCrewInitializationScreen(ClientModel model, ControllerToServer controller, ShipInitializationState state) {
        super(model, controller, state);
        this.crewTypeToPoints = state.getCrewtypeToPoints().getOrDefault(myShipBoard, new HashMap<>());
    }

    @Override
    public Pane getNode() {
        Pane superPane = super.getNode();

        HBox crewButtons = new HBox();

        for(CrewType crewType : crewTypeToPoints.keySet()) {
            Button crewButton = new Button(crewType.name()+" ALIEN");
            crewButton.setOnAction(_ -> currentCrewType = crewType);
            crewButtons.getChildren().add(crewButton);
        }
        Button skipButton = new Button("SKIP PLACING SELECTED ALIEN");

        superPane.getChildren().addAll(crewButtons, skipButton);
        return superPane;
    }

    @Override
    public void notifyInitializeCabin(ShipBoard shipBoard, Point point, CrewType crewType, int numResidents){
        guiShipBoards.get(shipBoard).notifyComponentChange(point);
    }

    @Override
    protected GuiController getGuiController() {
        return new GuiController() {
            @Override
            public void handlePointPress(Point point) {
                if (currentCrewType != null) {
                    controller.initializeCabin(point, currentCrewType);
                }
            }
        };
    }
}
