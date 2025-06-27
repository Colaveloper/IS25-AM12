package it.polimi.ingsw.galaxytruckers.client.view.gui.guiElements;

import javafx.geometry.Pos;
import javafx.scene.layout.HBox;

public class PurpleHBox extends HBox {
    public PurpleHBox() {
        setPurpleStyle();
    }

    /**
     * Creates a new purple container with custom spacing.
     *
     * @param spacing the spacing between elements
     */
    public PurpleHBox(double spacing) {
        super(spacing);
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
