package it.polimi.ingsw.galaxytruckers.view.guiElements;

import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.Component;
import javafx.application.Platform;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;

import java.util.ArrayList;
import java.util.List;
public class GuiStash extends HBox {

    private final List<GuiComponent> guiStashedComponents = new ArrayList<>();
    private final Region placeholder = createPlaceholder();

    public GuiStash(List<Component> stashedComponents, ControllerToServer controller) {
        setSpacing(5);

        if (stashedComponents.isEmpty()) {
            getChildren().add(placeholder);
        } else {
            for (Component c : stashedComponents) {
                GuiComponent guiComponent = new GuiComponent(c);
                guiStashedComponents.add(guiComponent);
                if (controller != null) {
                    guiComponent.setOnMouseClicked((_)->{
                        controller.grabStashedComponent(guiStashedComponents.indexOf(guiComponent));
                    });
                }
                getChildren().add(guiComponent);
            }
        }
    }

    public void notifyStash(Component component) {
        Platform.runLater(()->{
            if (guiStashedComponents.isEmpty()) {
                getChildren().remove(placeholder);
            }

            GuiComponent guiComponent = new GuiComponent(component);
            guiStashedComponents.add(guiComponent);
            getChildren().add(guiComponent);
        });
    }

    public void notifyGrab(int index) {
        Platform.runLater(()->{
            guiStashedComponents.remove(index);
            getChildren().remove(index);

            if (guiStashedComponents.isEmpty()) {
                getChildren().add(placeholder);
            }
        });
    }

    private Region createPlaceholder() {
        Region region = new Region();
        region.setPrefSize(50, 50);
        region.setStyle("-fx-background-color: lightgray; -fx-border-color: gray;");
        return region;
    }
}
