package it.polimi.ingsw.galaxytruckers.view;

import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.guiScreens.GuiGameScreen;
import it.polimi.ingsw.galaxytruckers.view.guiScreens.GuiScreen;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.view.model.state.ShipCorrectionState;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;

import java.awt.*;
import java.util.List;
import java.util.Set;

public class GuiValidationScreen extends GuiGameScreen {
    private boolean shipValid;
    private boolean shipBroken = false;
    private int numPieces;

    public GuiValidationScreen(ClientModel model, ControllerToServer controller, ShipCorrectionState state) {
        super(model, controller, state);
        shipValid = state.getValidShipBoards().contains(model.getMyShip());
        if (shipValid) shipBroken = state.getShipPieces().containsKey(model.getMyShip());
    }

    @Override
    public Parent getNode() {
        VBox layout = new VBox();
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(guiFlightBoard, guiAllShips);
        return layout;
    }

    @Override
    public void notifyRemoveComponent(ShipBoard shipBoard, Point point) {
        guiAllShips.notifyRemoveComponent(shipBoard, point);
    }

    @Override
    public void notifyChooseShipPiece(ShipBoard shipBoard, int pieceIndex, List<Point> removed) {
        for (Point piece : removed) guiAllShips.notifyRemoveComponent(shipBoard, piece);
    }

    @Override
    public void notifyShipNotConnected(ShipBoard shipBoard, List<Set<Point>> shipPieces) {

    }

    @Override
    public void notifyShipValidated(ShipBoard shipBoard) {

    }

    @Override
    public void handlePointPress(Point point) {
        controller.removeComponent(point);
        // TODO ADD SHIP PIECE CHOICE
    }
}
