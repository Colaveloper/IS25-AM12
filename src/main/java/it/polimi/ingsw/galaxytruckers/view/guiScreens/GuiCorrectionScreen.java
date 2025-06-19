package it.polimi.ingsw.galaxytruckers.view.guiScreens;

import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.view.model.state.ShipCorrectionState;
import it.polimi.ingsw.galaxytruckers.view.model.state.StateActions;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

public class GuiCorrectionScreen extends GuiGameScreen {
    private final List<Set<Point>> shipPieces;

    public GuiCorrectionScreen(ClientModel model, ControllerToServer controller, ShipCorrectionState state) {
        super(model, controller, state);
        shipPieces = new ArrayList<>(state.getShipPieces().getOrDefault(model.getMyShip(), Collections.emptyList()));
    }

    @Override
    public Parent getNode() {
        VBox layout = new VBox();
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().add(getGuiAllShips());
        return layout;
    }

    @Override
    public void notifyRemoveComponent(ShipBoard shipBoard, Point point) {
        guiShipBoards.get(shipBoard).notifyRemoveComponent(point);
    }

    @Override
    public void notifyChooseShipPiece(ShipBoard shipBoard, int pieceIndex, List<Point> removed) {
        for (Point piece : removed) guiShipBoards.get(shipBoard).notifyRemoveComponent(piece);
    }

    @Override
    public void notifyShipNotConnected(ShipBoard shipBoard, List<Set<Point>> shipPieces) {
        this.shipPieces.clear();
        this.shipPieces.addAll(shipPieces);
        // todo: give the user a message
    }

    @Override
    public void notifyShipValidated(ShipBoard shipBoard) {
        // todo: give the user a message
    }

    @Override
    protected GuiController getGuiController() {
        return new GuiController() {
            @Override
            public void handlePointPress(Point point) {
                if (state.getAvailableActions().contains(StateActions.REMOVE_COMPONENT)) {
                    controller.removeComponent(point);
                } else if (state.getAvailableActions().contains(StateActions.CHOOSE_SHIP_PIECE)) {
                    IntStream.range(0, shipPieces.size())
                            .filter(i -> shipPieces.get(i).contains(point))
                            .findFirst()
                            .ifPresent(controller::chooseShipPiece);
                }
            }
        };
    }
}
