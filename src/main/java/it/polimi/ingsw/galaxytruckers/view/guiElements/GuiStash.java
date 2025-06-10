package it.polimi.ingsw.galaxytruckers.view.guiElements;

import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.Component;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.input.Dragboard;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.ArrayList;
import java.util.List;
public class GuiStash extends HBox {
    private final ControllerToServer controller;
    private final HBox stashBox;

    public GuiStash(List<Component> stashedComponents, ControllerToServer controller) {
        this.controller = controller;

        Button stashButton = new Button("S");
        stashButton.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        stashButton.setTextFill(Color.WHITE);
        stashButton.setStyle("-fx-background-color: #2196F3;");
        stashButton.setPrefSize(50, 50);
        stashButton.setOnMouseClicked(_->controller.stashComponent());

        stashBox = new HBox();
        for (Component c : stashedComponents) {
            GuiComponent guiComponent = new GuiComponent(c);
            guiComponent.setOnMouseClicked((_)-> controller.grabStashedComponent(stashBox.getChildren().indexOf(guiComponent)));
            stashBox.getChildren().add(guiComponent);
        }

        getChildren().addAll(stashButton, stashBox);
    }

    public void notifyStash(Component component) {
        Platform.runLater(()->{
            GuiComponent guiComponent = new GuiComponent(component);
            stashBox.getChildren().add(guiComponent);
            guiComponent.setOnMouseClicked((_)-> controller.grabStashedComponent(stashBox.getChildren().indexOf(guiComponent)));
        });
    }

    public void notifyGrab(int index) {
        Platform.runLater(()-> stashBox.getChildren().remove(index));
    }
}
