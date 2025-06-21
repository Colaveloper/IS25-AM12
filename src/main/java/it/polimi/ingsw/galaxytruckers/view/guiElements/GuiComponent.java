package it.polimi.ingsw.galaxytruckers.view.guiElements;
import it.polimi.ingsw.galaxytruckers.view.Direction;
import it.polimi.ingsw.galaxytruckers.view.enums.CliHighlights;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.*;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.List;

public class GuiComponent extends StackPane {

    private final int id;
    private final HBox contentBox;
    private final ImageView componentView;

    protected GuiComponent(Component component) {
        this.id = component.getId();

        componentView = new ImageView(GuiComponentRegistry.getInstance().getImage(component.getId()));
        componentView.setFitWidth(50);
        componentView.setFitHeight(50);
        componentView.setRotate(component.getOrientation().getAngle());

        contentBox = new HBox(2);

        setPrefSize(50, 50);
        setAlignment(Pos.CENTER);
        getChildren().addAll(componentView, contentBox);
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

    public void setDirection(Direction direction) {
        componentView.setRotate(direction.getAngle());
    }

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

    public void clearHighlight() {
        contentBox.setBorder(Border.EMPTY);
    }

//    private String getColorString(CliHighlights highlight) {
//        return switch (highlight) {
//            case GREEN -> "green";
//            case BLUE -> "blue";
//            case RED -> "red";
//            case YELLOW -> "yellow";
//            default -> "transparent";
//        };
//    }

    public void notifyContentChange() {}
}

