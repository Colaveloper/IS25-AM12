package it.polimi.ingsw.galaxytruckers.view.guiElements;

import it.polimi.ingsw.galaxytruckers.view.guiScreens.GuiController;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.Component;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ComponentBank;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class GuiComponentBank extends HBox {

    private final GuiController controller;
    private final Label coveredNLabel;
    private final FlowPane rejectedContainer; // Changed to FlowPane

    public GuiComponentBank(ComponentBank componentBank, GuiController controller) {
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

        // Replace HBox with FlowPane for automatic wrapping
        rejectedContainer = new FlowPane();
        rejectedContainer.setHgap(10); // Horizontal gap between components
        rejectedContainer.setVgap(10); // Vertical gap between rows
        rejectedContainer.setPrefWidth(Control.USE_COMPUTED_SIZE); // Allow it to calculate width
        rejectedContainer.setMaxWidth(Double.MAX_VALUE); // Allow it to grow to full width
        HBox.setHgrow(rejectedContainer, Priority.ALWAYS); // Let it grow horizontally within parent

        // Add styling for visual separation - semi-transparent dark background with border
        rejectedContainer.setStyle(
            "-fx-background-color: rgba(20, 20, 40, 0.7);" + // Semi-transparent dark background
            "-fx-border-color: rgba(100, 100, 200, 0.8);" +  // Blue-ish border
            "-fx-border-width: 1px;" +                       // Border width
            "-fx-border-radius: 5px;" +                      // Rounded corners for border
            "-fx-background-radius: 5px;" +                  // Rounded corners for background
            "-fx-padding: 10px;"                             // Inner padding
        );

        // Set the container width to fill most of the screen
        VBox.setVgrow(rejectedContainer, Priority.ALWAYS);
        rejectedContainer.setPrefWrapLength(1200); // Set preferred wrap length to be very wide

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
