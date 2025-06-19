package it.polimi.ingsw.galaxytruckers.view.guiScreens;

import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.state.DeclareFirePowerState;
import it.polimi.ingsw.galaxytruckers.view.model.state.StateActions;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.awt.*;

public class GuiDeclareFirePowerScreen extends GuiActivationScreen {
    public GuiDeclareFirePowerScreen(ClientModel model, ControllerToServer controller, DeclareFirePowerState declareFirePowerState) {
        super(model, controller, declareFirePowerState);
    }

    @Override
    public Parent getNode() {
        VBox layout = new VBox();
        layout.getChildren().add(new Label("GuiDeclareFirePowerScreen"));
        layout.getChildren().add(guiContextBox);
        layout.getChildren().add(getGuiAllShips());

        Button goNextButton = new Button("OK");
        goNextButton.setOnAction(_ -> getGuiController().goNext());
        layout.getChildren().add(goNextButton);

        return layout;
    }


}
