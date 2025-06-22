package it.polimi.ingsw.galaxytruckers.view.guiElements;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.VBox;

/**
 * A reusable styled container that provides consistent styling for components.
 * This container extends VBox and applies the same styling used for the
 * rejected components container in GuiComponentBank.
 */
public class PurpleVBox extends VBox {

    /**
     * Creates a new purple container with default settings.
     */
    public PurpleVBox() {
        setPurpleStyle();
    }

    /**
     * Creates a new purple container with custom spacing.
     *
     * @param spacing the spacing between elements
     */
    public PurpleVBox(double spacing) {
        super(spacing);
        setPurpleStyle();
    }

    public PurpleVBox(Node node) {
        super(node);
        setPurpleStyle();
    }

    /**
     * Configures the container with purple styling.
     */
    private void setPurpleStyle() {
        setAlignment(Pos.CENTER);
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
