package it.polimi.ingsw.galaxytruckers.view.guiElements;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.ToggleButton;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * A circular toggle button for JavaFX GUIs.
 * <p>
 * This button is styled as a circle and can be toggled between active and inactive states.
 * The color of the button can be customized, and its active state is exposed as a property.
 * </p>
 */
public class CircularToggleButton extends ToggleButton {

    /**
     * The base color of the button when active.
     */
    private Color baseColor;
    /**
     * Property representing whether the button is active.
     */
    private final BooleanProperty active = new SimpleBooleanProperty(false);

    /**
     * Constructs a CircularToggleButton with the specified base color.
     *
     * @param baseColor the color to use when the button is active
     */
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

    /**
     * Sets the base color of the button.
     *
     * @param color the new base color
     */
    public void setColor(Color color) {
        baseColor = color;
        updateStyle(active.get());
    }

    /**
     * Returns the property representing the active state.
     *
     * @return the active property
     */
    public BooleanProperty isActiveProperty() {
        return active;
    }

    /**
     * Returns whether the button is active.
     *
     * @return true if active, false otherwise
     */
    public boolean isActive() {
        return active.get();
    }

    /**
     * Sets the active state of the button.
     *
     * @param value true to activate, false to deactivate
     */
    public void setActive(boolean value) {
        active.set(value);
    }

    /**
     * Updates the style of the button based on its active state.
     *
     * @param active true if the button is active, false otherwise
     */
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
