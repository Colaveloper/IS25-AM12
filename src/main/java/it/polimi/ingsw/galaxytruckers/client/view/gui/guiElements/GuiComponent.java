package it.polimi.ingsw.galaxytruckers.client.view.gui.guiElements;

import it.polimi.ingsw.galaxytruckers.shared.enums.Direction;
import it.polimi.ingsw.galaxytruckers.client.model.shipBuilding.*;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.List;

/**
 * Base GUI component for ship parts in the game.
 * <p>
 * This class provides a visual representation of a ship component, including its image and any content (such as cargo, batteries, or crew).
 * It is the superclass for more specific GUI components like {@link GuiCargoHold}, {@link GuiBattery}, and {@link GuiCabin}.
 * </p>
 */
public class GuiComponent extends StackPane {

    private final int id;
    private final HBox contentBox;
    private final ImageView componentView;

    /**
     * Constructs a GuiComponent for the given model component.
     *
     * @param component the model component to represent
     */
    protected GuiComponent(Component component) {
        this.id = component.getId();

        componentView = new ImageView(GuiComponentRegistry.getInstance().getImage(component.getId()));
        componentView.setFitWidth(60);
        componentView.setFitHeight(60);
        componentView.setRotate(component.getOrientation().getAngle());

        contentBox = new HBox(2);

        setMaxSize(60, 60);
        setAlignment(Pos.CENTER);
        getChildren().addAll(componentView, contentBox);
    }

    /**
     * Factory method to create the appropriate GuiComponent subclass for a given model component.
     *
     * @param component the model component
     * @return the corresponding GuiComponent
     */
    public static GuiComponent of(Component component) {
        return switch (component) {
            case CargoHold cargoHold -> new GuiCargoHold(cargoHold);
            case Battery battery -> new GuiBattery(battery);
            case Cabin cabin -> new GuiCabin(cabin);
            case Component c -> new GuiComponent(c);
        };
    }

    /**
     * Checks if this component has the specified ID.
     *
     * @param id the ID to check
     * @return true if the IDs match, false otherwise
     */
    public boolean hasId(int id) {
        return this.id == id;
    }

    /**
     * Sets the visual orientation of the component.
     *
     * @param direction the direction to set
     */
    public void setDirection(Direction direction) {
        componentView.setRotate(direction.getAngle());
    }

    /**
     * Updates the content box with the given list of colors, each represented as a circle.
     *
     * @param colors the list of colors to display
     */
    protected void updateContentBox(List<Color> colors) {
        Platform.runLater(()->{
            contentBox.getChildren().clear();
            for (Color color : colors) {
                Circle contentUnitCircle = new Circle(4);
                contentUnitCircle.setFill(color);

                DropShadow shadow = new DropShadow();
                shadow.setOffsetX(0.5);
                shadow.setOffsetY(0.5);
                shadow.setColor(Color.rgb(0, 0, 0, 0.5)); // semi-transparent black

                contentUnitCircle.setEffect(shadow);
                contentBox.getChildren().add(contentUnitCircle);
            }
        });
    }

    /**
     * Sets the highlight color of the component's content box.
     *
     * @param color the color to set as highlight, or null to clear the highlight
     */
    public void setHighlight(Color color) {
        if (color == null) {
            clearHighlight();
            return;
        }

        BorderStroke borderStroke = new BorderStroke(
                color,
                BorderStrokeStyle.SOLID,
                CornerRadii.EMPTY,
                new BorderWidths(2),
                new Insets(0)
        );

        contentBox.setBorder(new Border(borderStroke));
    }

    /**
     * Clears the highlight from the component's content box.
     */
    public void clearHighlight() {
        contentBox.setBorder(Border.EMPTY);
    }

    /**
     * Notifies that the content of the component has changed.
     * <p>
     * This method can be overridden by subclasses to perform actions when the content changes.
     * </p>
     */
    public void notifyContentChange() {}
}
