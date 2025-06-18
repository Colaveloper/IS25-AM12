package it.polimi.ingsw.galaxytruckers.view.guiElements;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.*;
import javafx.animation.RotateTransition;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

public class GuiComponent extends StackPane {

    private final int id;

    protected GuiComponent(Component component) {
        this.id = component.getId();

        ImageView componentImage = new ImageView(GuiComponentRegistry.getInstance().getImage(component.getId()));
        componentImage.setFitWidth(50);
        componentImage.setFitHeight(50);

        setPrefSize(50, 50);
        setAlignment(Pos.CENTER);
        getChildren().add(componentImage);
    }

    public static GuiComponent of(Component component) {
        return switch (component) {
            case DoubleCannon doubleCannon-> new GuiDoubleCannon(doubleCannon);
            case DoubleEngine doubleEngine -> new GuiDoubleEngine(doubleEngine);
            case Battery battery -> new GuiBattery(battery);
            case Cabin cabin -> new GuiCabin(cabin);
            case Cannon cannon -> new GuiCannon(cannon);
            case CargoHold cargoHold -> new GuiCargoHold(cargoHold);
            case Engine engine -> new GuiEngine(engine);
            case LifeSupport lifeSupport -> new GuiLifeSupport(lifeSupport);
            case Shield shield -> new GuiShield(shield);
            case Component comp -> new GuiComponent(comp);
        };
    }

    public boolean hasId(int id) {
        return this.id == id;
    }
}
