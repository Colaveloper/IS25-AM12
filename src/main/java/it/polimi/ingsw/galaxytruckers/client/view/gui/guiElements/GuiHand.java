package it.polimi.ingsw.galaxytruckers.client.view.gui.guiElements;

import it.polimi.ingsw.galaxytruckers.shared.enums.Direction;
import it.polimi.ingsw.galaxytruckers.client.view.gui.guiScreens.GuiController;
import it.polimi.ingsw.galaxytruckers.client.model.shipBuilding.Component;
import javafx.application.Platform;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;

/**
 * GUI component representing the player's hand in the game.
 * <p>
 * This class displays the component currently held in the player's hand, allowing for rotation and clearing.
 * It interacts with the {@link GuiController} to handle user actions and updates the display accordingly.
 * </p>
 */
public class GuiHand extends HBox {

    private GuiComponent guiHandComponent;
    private final GuiController controller;

    /**
     * Constructs a GuiHand for the given component and controller.
     *
     * @param component the component to display in the hand (can be null for placeholder)
     * @param controller the GUI controller handling actions
     */
    public GuiHand(Component component, GuiController controller) {
        this.controller = controller;
        if (component == null) {
            getChildren().add(createPlaceholder());
        } else {
            guiHandComponent = new GuiComponent(component);
            guiHandComponent.setOnMouseClicked(_ -> controller.rotateHandComponent());
            getChildren().add(guiHandComponent);
        }
    }

    /**
     * Updates the hand to display the given component.
     *
     * @param component the new component to display in the hand
     */
    public void notifySetHand(Component component) {
        Platform.runLater(()->{
            getChildren().clear();
            guiHandComponent = new GuiComponent(component);
            guiHandComponent.setOnMouseClicked(_ -> controller.rotateHandComponent());
            getChildren().add(guiHandComponent);
        });
    }

    /**
     * Clears the hand and displays a placeholder.
     */
    public void notifyClearHand() {
        Platform.runLater(()->{
            getChildren().clear();
            guiHandComponent = null;
            getChildren().add(createPlaceholder());
        });
    }

    /**
     * Sets the visual direction of the component in the hand.
     *
     * @param direction the direction to set
     */
    public void setComponentDirection(Direction direction) {
        guiHandComponent.setDirection(direction);
    }

    /**
     * Creates a placeholder region to display when the hand is empty.
     *
     * @return a styled placeholder region
     */
    private Region createPlaceholder() {
        Region region = new Region();
        region.setPrefSize(50, 50);
        region.setStyle("-fx-border-color: gray; -fx-border-width: 2;");
        return region;
    }
}
