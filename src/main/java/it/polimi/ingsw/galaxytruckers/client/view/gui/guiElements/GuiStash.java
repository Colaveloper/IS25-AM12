package it.polimi.ingsw.galaxytruckers.client.view.gui.guiElements;

import it.polimi.ingsw.galaxytruckers.client.view.gui.guiScreens.GuiController;
import it.polimi.ingsw.galaxytruckers.client.model.shipBuilding.Component;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.List;

/**
 * GUI component representing the player's stash in the game.
 * <p>
 * This class displays the stashed components and provides controls for stashing and grabbing components.
 * It interacts with the {@link GuiController} to handle user actions and updates the display accordingly.
 * </p>
 */
public class GuiStash extends HBox {
    private final GuiController controller;
    private final HBox stashBox;

    /**
     * Constructs a GuiStash for the given stashed components and controller.
     *
     * @param stashedComponents the list of components currently stashed
     * @param controller the GUI controller handling actions
     */
    public GuiStash(List<Component> stashedComponents, GuiController controller) {
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

    /**
     * Notifies the GUI to add a component to the stash.
     *
     * @param component the component to add to the stash
     */
    public void notifyStash(Component component) {
        Platform.runLater(()->{
            GuiComponent guiComponent = new GuiComponent(component);
            stashBox.getChildren().add(guiComponent);
            guiComponent.setOnMouseClicked((_)-> controller.grabStashedComponent(stashBox.getChildren().indexOf(guiComponent)));
        });
    }

    /**
     * Notifies the GUI to remove a component from the stash at the specified index.
     *
     * @param index the index of the component to remove
     */
    public void notifyGrab(int index) {
        Platform.runLater(()-> stashBox.getChildren().remove(index));
    }
}
