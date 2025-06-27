package it.polimi.ingsw.galaxytruckers.client.view.gui.guiElements;

import javafx.application.Platform;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 * GUI component for displaying a log area in the game.
 * <p>
 * This class provides a non-editable, scrollable text area for displaying log messages to the user.
 * It is styled for readability and automatically scrolls to show the latest log entry.
 * </p>
 */
public class GuiLog extends VBox {

    /**
     * The text area used to display log messages.
     */
    private final TextArea textArea;

    /**
     * Constructs a GuiLog and initializes the log display area.
     */
    public GuiLog() {
        textArea = new TextArea();
        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.setFocusTraversable(false);
        textArea.setPrefRowCount(10);
        textArea.setStyle("-fx-control-inner-background: black; -fx-text-fill: white;");
        textArea.setPrefHeight(200);
        this.getChildren().add(textArea);
        VBox.setVgrow(textArea, Priority.ALWAYS);
    }

    /**
     * Appends a message to the log and scrolls to the bottom.
     *
     * @param text the message to log
     */
    public void log(String text) {
        Platform.runLater(() -> {
            textArea.appendText(text + "\n");
            textArea.setScrollTop(Double.MAX_VALUE); // ensures it scrolls to the bottom
        });
    }
}
