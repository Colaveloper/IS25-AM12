package it.polimi.ingsw.galaxytruckers.view.guiElements;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Control;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 * A reusable styled container that provides consistent styling for components.
 * This container extends VBox and applies the same styling used for the
 * rejected components container in GuiComponentBank.
 */
public class PurpleContainer extends VBox {

    /**
     * Creates a new purple container with default settings.
     */
    public PurpleContainer() {
        setPurpleStyle();
    }

    /**
     * Creates a new purple container with custom spacing.
     *
     * @param spacing the spacing between elements
     */
    public PurpleContainer(double spacing) {
        super(spacing);
        setPurpleStyle();
    }

    /**
     * Configures the container with purple styling.
     */
    private void setPurpleStyle() {
        setStyle(
            "-fx-background-color: rgba(20, 20, 40, 0.7);" +
            "-fx-border-color: rgba(100, 100, 200, 0.8);" +
            "-fx-border-width: 1px;" +
            "-fx-border-radius: 5px;" +
            "-fx-background-radius: 5px;" +
            "-fx-padding: 10px;"
        );
    }
}
