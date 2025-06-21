package it.polimi.ingsw.galaxytruckers.view.guiScreens;

import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.view.model.state.AdventureState;
import it.polimi.ingsw.galaxytruckers.view.model.state.StateActions;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.awt.*;
import java.util.Set;

public abstract class GuiActivationScreen extends GuiAdventureScreen {
    private final IntegerProperty batteriesToSpend;
    private final Button goNextButton = new Button("FINISH ACTIVATION") ;

    public GuiActivationScreen(ClientModel model, ControllerToServer controller, AdventureState gameState) {
        super(model, controller, gameState);
        batteriesToSpend = new SimpleIntegerProperty(0);
        if (isMyTurn()) {
            guiLog.getChildren().setAll(new Label("Spend the batteries you want to activate components, then press OK"));
        } else {
            guiLog.getChildren().setAll(new Label("Wait for your turn"));
        }
    }

    @Override
    public Pane getNode() {
        Pane superGroup = super.getNode();
        if (isMyTurn()) {
            batteriesToSpend.set(0);

            goNextButton.setOnAction(_ -> getGuiController().goNext());
            goNextButton.disableProperty().bind(
                    Bindings.createBooleanBinding(() -> (batteriesToSpend.get() != 0), batteriesToSpend)
            );
            superGroup.getChildren().add(goNextButton);
            goNextButton.setVisible(true);

            guiLog.log("Spend the batteries you want to activate components, then press FINISH ACTIVATION");
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
                            guiLog.getChildren().setAll(new Label("This component is already active"));
                        }
                    } else if (model.getMyShip().getBatteries().containsKey(point)) {
                        if (model.getMyShip().getBatteries().get(point).getNumBatteries() > 0) {
                            if (state.getAvailableActions().contains(StateActions.SPEND_BATTERIES)) {
                                controller.useBattery(point);
                            }
                        } else {
                            guiLog.getChildren().setAll(new Label("Out of batteries at this position"));
                        }
                    }
                }
            }

            @Override
            public void goNext() {
                if (isMyTurn()) {
                    if(batteriesToSpend.get() > 0) {
                        guiLog.getChildren().setAll(new Label("You need to use " + batteriesToSpend.get() + " batteries"));
                    } else if(batteriesToSpend.get() < 0) {
                        guiLog.getChildren().setAll(new Label("You need to activate " + (batteriesToSpend.get() * (-1)) + " components"));
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
        batteriesToSpend.add(-1);
        guiShipBoards.get(myShipBoard).highlightPoints(Set.of(point), Color.BLUE);
    }

    @Override
    public void notifyUseBattery(ShipBoard shipBoard, Point point) {
        batteriesToSpend.add(1);
        guiShipBoards.get(myShipBoard).notifyComponentChange(point);
        guiShipBoards.get(myShipBoard).highlightPoints(Set.of(point), Color.GREEN);
    }
}
