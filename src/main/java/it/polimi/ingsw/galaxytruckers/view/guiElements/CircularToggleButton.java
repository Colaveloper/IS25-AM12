package it.polimi.ingsw.galaxytruckers.view.guiElements;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.ToggleButton;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class CircularToggleButton extends ToggleButton {

    private Color baseColor;
    private final BooleanProperty active = new SimpleBooleanProperty(false);

    public CircularToggleButton(Color baseColor) {
        this.baseColor = baseColor;

        int radius = 20;
        setShape(new Circle(radius));
        setMinSize(radius * 2, radius * 2);
        setPrefSize(radius * 2, radius * 2);
        setMaxSize(radius * 2, radius * 2);

        // Bind style update to active property
        active.addListener((obs, oldVal, newVal) -> updateStyle(newVal));
        updateStyle(active.get());

        // Optional: bind toggle state to active
//        selectedProperty().addListener((obs, oldVal, newVal) -> active.set(newVal));
    }

    public void setColor(Color color) {
        baseColor = color;
        updateStyle(active.get());
    }

    public BooleanProperty isActiveProperty() {
        return active;
    }

    public boolean isActive() {
        return active.get();
    }

    public void setActive(boolean value) {
        active.set(value);
    }

    private void updateStyle(boolean active) {
        String hex = toHex(baseColor);
        String commonStyle = "-fx-text-fill: white;" +
                "-fx-font-size: 16px;";

        if (active) {
            setStyle(
                    "-fx-background-color: " + hex + ";" +
                            "-fx-background-radius: 50%;" +
                            "-fx-border-color: transparent;" +
                            commonStyle
            );
        } else {
            setStyle(
                    "-fx-background-color: transparent;" +
                            "-fx-border-color: " + hex + ";" +
                            "-fx-border-width: 5px;" +
                            "-fx-border-radius: 50%;" +
                            commonStyle
            );
        }
    }


    private String toHex(Color color) {
        return String.format("#%02X%02X%02X",
                (int)(color.getRed() * 255),
                (int)(color.getGreen() * 255),
                (int)(color.getBlue() * 255));
    }
}
