package it.polimi.ingsw.galaxytruckers.view.guiScreens;

import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.view.model.state.ShipCorrectionState;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

public class GuiValidationScreen extends GuiGameScreen {
    private boolean shipValid;
    private final List<Set<Point>> shipPieces; // .isEmpty iif ship is 1 piece

    public GuiValidationScreen(ClientModel model, ControllerToServer controller, ShipCorrectionState state) {
        super(model, controller, state);
        shipPieces = new ArrayList<>(state.getShipPieces().get(model.getMyShip()));
        shipValid = state.getValidShipBoards().contains(model.getMyShip());
    }

    @Override
    public Parent getNode() {
        VBox layout = new VBox();
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().add(guiShipBoards.get(model.getMyShip()));
        return layout;
    }

    @Override
    public void notifyRemoveComponent(ShipBoard shipBoard, Point point) {
        guiShipBoards.get(model.getMyShip()).notifyRemoveComponent(point);
    }

    @Override
    public void notifyChooseShipPiece(ShipBoard shipBoard, int pieceIndex, List<Point> removed) {
        for (Point piece : removed) guiShipBoards.get(model.getMyShip()).notifyRemoveComponent(piece);
    }

    @Override
    public void notifyShipNotConnected(ShipBoard shipBoard, List<Set<Point>> shipPieces) {

    }

    @Override
    public void notifyShipValidated(ShipBoard shipBoard) {

    }

    @Override
    protected GuiController getGuiController() {
        return new GuiController() {
            @Override
            public void handlePointPress(Point point) {
                if (shipPieces.isEmpty()) {
                    controller.removeComponent(point);
                } else {
                    IntStream.range(0, shipPieces.size())
                            .filter(i -> shipPieces.get(i).contains(point))
                            .findFirst()
                            .ifPresent(controller::chooseShipPiece);
                }
            }
        };
    }
}
