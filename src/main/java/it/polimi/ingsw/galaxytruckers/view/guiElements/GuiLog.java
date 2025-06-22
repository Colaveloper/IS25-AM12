package it.polimi.ingsw.galaxytruckers.view.guiElements;

import javafx.scene.control.TextArea;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class GuiLog extends VBox {

    private final TextArea textArea;

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

    public void log(String text) {
        textArea.appendText(text + "\n");
        textArea.setScrollTop(Double.MAX_VALUE); // ensures it scrolls to the bottom
    }
}
