package it.polimi.ingsw.galaxytruckers.view.guiScreens;

import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.enums.Highlights;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.view.model.state.ActivateState;
import it.polimi.ingsw.galaxytruckers.view.model.state.AdventureState;
import it.polimi.ingsw.galaxytruckers.view.model.state.GameState;
import it.polimi.ingsw.galaxytruckers.view.model.state.StateActions;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

import java.awt.*;
import java.util.Set;

public abstract class GuiActivationScreen extends GuiAdventureScreen {
    private int batteriesToSpend;

    public GuiActivationScreen(ClientModel model, ControllerToServer controller, AdventureState gameState) {
        super(model, controller, gameState);
        batteriesToSpend = 0;
        if (isMyTurn()) {
            updateContextBox("Spend the batteries you want to activate components, then press OK");
        } else {
            updateContextBox("Wait for your turn");
        }
    }

    @Override
    public Parent getNode() {
        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);

        Button goNextButton = new Button("OK");
        goNextButton.setOnAction(_ -> getGuiController().goNext());
        goNextButton.setDisable(batteriesToSpend != 0);

        layout.getChildren().addAll(
                getGuiFlightBoard(),
                getGuiAllShips(),
                goNextButton
        );

        return layout;
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
//        shipToCliShip.get(shipBoard).highlightPoints(Set.of(point), Highlights.BLUE);
    }

    @Override
    public void notifyUseBattery(ShipBoard shipBoard, Point point) {
        batteriesToSpend --;
//        shipToCliShip.get(shipBoard).highlightPoints(Set.of(point), Highlights.GREEN);
    }
}
