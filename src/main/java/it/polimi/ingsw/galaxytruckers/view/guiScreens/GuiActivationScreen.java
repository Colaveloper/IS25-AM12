package it.polimi.ingsw.galaxytruckers.view.guiScreens;

import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.enums.Highlights;
import it.polimi.ingsw.galaxytruckers.view.guiElements.GuiComponent;
import it.polimi.ingsw.galaxytruckers.view.guiElements.GuiShipBoard;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.view.model.state.AdventureState;
import it.polimi.ingsw.galaxytruckers.view.model.state.StateActions;
import javafx.scene.Group;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.awt.*;

public abstract class GuiActivationScreen extends GuiAdventureScreen {
    private int batteriesToSpend;
    private final Button goNextButton = new Button("FINISH ACTIVATION") ;

    public GuiActivationScreen(ClientModel model, ControllerToServer controller, AdventureState gameState) {
        super(model, controller, gameState);
        batteriesToSpend = 0;
        if (isMyTurn()) {
            updateContextBox("Spend the batteries you want to activate components, then press OK");
            goNextButton.setOnAction(_ -> getGuiController().goNext());
        } else {
            updateContextBox("Wait for your turn");
        }
    }

    @Override
    public Pane getNode() {
        Pane superGroup = super.getNode();
        if (isMyTurn()) {
            superGroup.getChildren().add(goNextButton);
        }
        return superGroup;
    }

    @Override
    protected GuiController getGuiController() {
        return new GuiController() {
            @Override
            public void handlePointPress(Point point) {
                if (isMyTurn()) {
                    if (model.getMyShip().getActivatables().containsKey(point)) {
                        if (!model.getMyShip().getActivatables().get(point).isActive()) {
                            if (state.getAvailableActions().contains(StateActions.ACTIVATE_COMPONENT)) {
                                controller.activateComponent(point);
                            }
                        } else {
                            updateContextBox("This component is already active");
                        }
                    } else if (model.getMyShip().getBatteries().containsKey(point)) {
                        if (model.getMyShip().getBatteries().get(point).getNumBatteries() > 0) {
                            if (state.getAvailableActions().contains(StateActions.SPEND_BATTERIES)) {
                                controller.useBattery(point);
                            }
                        } else {
                            updateContextBox("Out of batteries at this position");
                        }
                    }
                }
            }

            @Override
            public void goNext() {
                if (isMyTurn()) {
                    if(batteriesToSpend > 0) {
                        updateContextBox("You need to use " + batteriesToSpend + " batteries");
                    } else if(batteriesToSpend < 0) {
                        updateContextBox("You need to activate " + (batteriesToSpend * (-1)) + " components");
                    } else {
                        if (state.getAvailableActions().contains(StateActions.GO_NEXT)) {
                            controller.goNext();
                        }
                    }
                }
            }
        };
    }

    @Override
    public void notifyActivateComponent(ShipBoard shipBoard, Point point) {
        batteriesToSpend ++;
        highlightComponent(shipBoard, point, Highlights.BLUE);
        goNextButton.setDisable(batteriesToSpend != 0);
    }

    @Override
    public void notifyUseBattery(ShipBoard shipBoard, Point point) {
        batteriesToSpend --;
        highlightComponent(shipBoard, point, Highlights.GREEN);
        goNextButton.setDisable(batteriesToSpend != 0);
    }

    private void highlightComponent(ShipBoard shipBoard, Point point, Highlights color) {
        GuiShipBoard guiShipBoard = guiShipBoards.get(shipBoard);
        if (guiShipBoard == null) return;

        int minX = shipBoard.getShipArea().stream().mapToInt(p -> p.x).min().orElse(0);
        int minY = shipBoard.getShipArea().stream().mapToInt(p -> p.y).min().orElse(0);

        int targetCol = point.x - minX + 1;
        int targetRow = point.y - minY + 1;

        guiShipBoard.getChildren().stream()
            .filter(node -> {
                Integer col = GridPane.getColumnIndex(node);
                Integer row = GridPane.getRowIndex(node);
                return col != null && row != null && col == targetCol && row == targetRow;
            })
            .findFirst()
            .ifPresent(node -> highlightNode(node, color));
    }

    private void highlightNode(javafx.scene.Node node, Highlights color) {
        GuiComponent component = (GuiComponent) node;
        component.highlight(color);
    }
}
