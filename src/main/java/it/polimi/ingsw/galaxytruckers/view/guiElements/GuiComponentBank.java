package it.polimi.ingsw.galaxytruckers.view.guiElements;

import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.Component;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ComponentBank;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.ArrayList;

public class GuiComponentBank extends HBox {

    private final ControllerToServer controller;
    private final Label coveredNLabel;
    private final HBox rejectedContainer;

    public GuiComponentBank(ComponentBank componentBank, ControllerToServer controller) {
        this.controller = controller;

        setMaxWidth(Double.MAX_VALUE);
        setSpacing(20);
        setPadding(new Insets(10));
        setAlignment(Pos.TOP_LEFT);
        HBox.setHgrow(this, Priority.ALWAYS);

        // === Left container ===
        VBox coveredBox = new VBox(5);

        Button plusButton = new Button("+");
        plusButton.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        plusButton.setTextFill(Color.WHITE);
        plusButton.setStyle("-fx-background-color: #2196F3;");
        plusButton.setPrefSize(50, 50);
        plusButton.setOnMouseClicked(_ -> controller.requestRandComponent());

        coveredNLabel = new Label(Integer.toString(componentBank.getCoveredComponentsN()));
        coveredNLabel.setFont(Font.font(14));
        coveredNLabel.setTextFill(Color.GRAY);

        coveredBox.getChildren().addAll(plusButton, coveredNLabel);

        HBox uncoveredBox = new HBox(10);

        Button minusButton = new Button("-");
        minusButton.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        minusButton.setTextFill(Color.WHITE);
        minusButton.setStyle("-fx-background-color: #2196F3;");
        minusButton.setPrefSize(50, 50);
        minusButton.setOnMouseClicked(_ -> controller.rejectComponent());

        rejectedContainer = new HBox();
        componentBank.getUncoveredComponents().forEach(component -> {
            GuiComponent newComponent = new GuiComponent(component);
            rejectedContainer.getChildren().add(newComponent);
            newComponent.setOnMouseClicked(_ ->
                    controller.requestComponent(component.getId())
            );
        });

        uncoveredBox.getChildren().addAll(minusButton, rejectedContainer);

        getChildren().addAll(coveredBox, uncoveredBox);
    }

    public void notifyRequestRandComponent() {
        Platform.runLater(()-> coveredNLabel.setText(Integer.toString(
                Integer.parseInt(coveredNLabel.getText()) - 1)
        ));
    }

    public void notifyRejectComponent(Component component) {
        Platform.runLater(()-> {
            GuiComponent newComponent = new GuiComponent(component);
            rejectedContainer.getChildren().add(newComponent);
            newComponent.setOnMouseClicked(_ ->
                    controller.requestComponent(component.getId())
            );
        });
    }

    public void notifyRequestComponent(Component component) {
        Platform.runLater(() -> {
            rejectedContainer.getChildren().stream()
                    .filter(node -> node instanceof GuiComponent gui && gui.hasId(component.getId()))
                    .findFirst()
                    .ifPresent(node -> rejectedContainer.getChildren().remove(node));
        });
    }
}
