package it.polimi.ingsw.galaxytruckers.view.guiScreens;

import it.polimi.ingsw.galaxytruckers.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.view.model.state.ShipInitializationState;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GuiCrewInitializationScreen extends GuiGameScreen {
    private CrewType currentCrewType;
    private final Map<CrewType, List<Point>> crewTypeToPoints;

    public GuiCrewInitializationScreen(ClientModel model, ControllerToServer controller, ShipInitializationState state) {
        super(model, controller, state);
        this.crewTypeToPoints = state.getCrewtypeToPoints().getOrDefault(myShipBoard, new HashMap<>());
    }

    @Override
    public Parent getNode() {
        VBox layout = new VBox();
        layout.setAlignment(Pos.CENTER);

        HBox crewButtons = new HBox();

        for(CrewType crewType : crewTypeToPoints.keySet()) {
            Button crewButton = new Button(crewType.name()+" ALIEN");
            crewButton.setOnAction(_ -> {
                currentCrewType = crewType;
            });
            crewButtons.getChildren().add(crewButton);
        }

        Button skipButton = new Button("SKIP PLACING SELECTED ALIEN");

        layout.getChildren().addAll(crewButtons, skipButton, getAllShips());
        return layout;
    }

    @Override
    public void notifyInitializeCabin(ShipBoard shipBoard, Point point, CrewType crewType, int numResidents){
        guiShipBoards.get(shipBoard).notifyInitializeCabin(point, crewType, numResidents);
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
