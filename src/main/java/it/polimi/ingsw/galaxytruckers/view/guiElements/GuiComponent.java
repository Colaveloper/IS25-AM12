package it.polimi.ingsw.galaxytruckers.view.guiElements;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.*;
import javafx.animation.RotateTransition;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class GuiComponent extends StackPane {

    private final int id;
    private final HBox contentBox;

    protected GuiComponent(Component component) {
        this.id = component.getId();

        ImageView componentImage = new ImageView(GuiComponentRegistry.getInstance().getImage(component.getId()));
        componentImage.setFitWidth(50);
        componentImage.setFitHeight(50);
        componentImage.setRotate(component.getOrientation().getAngle());

        contentBox = new HBox(2);

        setPrefSize(50, 50);
        setAlignment(Pos.CENTER);
        getChildren().addAll(componentImage, contentBox);
    }

    public static GuiComponent of(Component component) {
        return switch (component) {
            case CargoHold cargoHold -> new GuiCargoHold(cargoHold);
            case Battery battery -> new GuiBattery(battery);
            case Cabin cabin -> new GuiCabin(cabin);
            case Component c -> new GuiComponent(c);
        };
    }

    public boolean hasId(int id) {
        return this.id == id;
    }

    protected void updateContentBox(List<Color> colors) {
        contentBox.getChildren().clear();
        for (Color color : colors) {
            contentBox.getChildren().add(new Circle(4, color));
        }
    }
}
