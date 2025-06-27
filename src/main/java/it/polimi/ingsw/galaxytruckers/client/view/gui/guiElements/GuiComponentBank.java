package it.polimi.ingsw.galaxytruckers.client.view.gui.guiElements;

import it.polimi.ingsw.galaxytruckers.client.view.gui.guiScreens.GuiController;
import it.polimi.ingsw.galaxytruckers.client.model.shipBuilding.Component;
import it.polimi.ingsw.galaxytruckers.client.model.shipBuilding.ComponentBank;
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

/**
 * GUI component representing the player's component bank in the game.
 * <p>
 * This class displays the covered and rejected components, and provides controls for drawing and rejecting components.
 * It interacts with the {@link GuiController} to handle user actions and updates the display accordingly.
 * </p>
 */
public class GuiComponentBank extends HBox {

    private final GuiController controller;
    private final Label coveredNLabel;
    private final FlowPane rejectedContainer; // Changed to FlowPane

    /**
     * Constructs a GuiComponentBank for the given component bank and controller.
     *
     * @param componentBank the component bank model to represent
     * @param controller the GUI controller handling actions
     */
    public GuiComponentBank(ComponentBank componentBank, GuiController controller) {
        this.controller = controller;

        setMaxWidth(Double.MAX_VALUE);
        setSpacing(5);
        setPadding(new Insets(5));
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

        // flowpane for rejected components
        rejectedContainer = new FlowPane();
        rejectedContainer.setHgap(0);
        rejectedContainer.setVgap(0);
        rejectedContainer.setPrefWidth(Control.USE_COMPUTED_SIZE);
        rejectedContainer.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(rejectedContainer, Priority.ALWAYS);

        rejectedContainer.setStyle(
            "-fx-background-color: rgba(20, 20, 40, 0.7);" +
            "-fx-border-color: rgba(100, 100, 200, 0.8);" +
            "-fx-border-width: 1px;" +
            "-fx-border-radius: 5px;" +
            "-fx-background-radius: 5px;" +
            "-fx-padding: 10px;"
        );

        VBox.setVgrow(rejectedContainer, Priority.ALWAYS);
        rejectedContainer.setPrefWrapLength(1500);

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

    /**
     * Updates the label to reflect one less covered component after a random draw.
     */
    public void notifyRequestRandComponent() {
        Platform.runLater(()-> coveredNLabel.setText(Integer.toString(
                Integer.parseInt(coveredNLabel.getText()) - 1)
        ));
    }

    /**
     * Adds a rejected component to the rejected container in the GUI.
     *
     * @param component the component to add to the rejected list
     */
    public void notifyRejectComponent(Component component) {
        Platform.runLater(()-> {
            GuiComponent newComponent = new GuiComponent(component);
            rejectedContainer.getChildren().add(newComponent);
            newComponent.setOnMouseClicked(_ ->
                    controller.requestComponent(component.getId())
            );
        });
    }

    /**
     * Removes a component from the rejected container when it is requested by the player.
     *
     * @param component the component to remove from the rejected list
     */
    public void notifyRequestComponent(Component component) {
        Platform.runLater(() -> {
            rejectedContainer.getChildren().stream()
                    .filter(node -> node instanceof GuiComponent gui && gui.hasId(component.getId()))
                    .findFirst()
                    .ifPresent(node -> rejectedContainer.getChildren().remove(node));
        });
    }
}
