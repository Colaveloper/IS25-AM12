package it.polimi.ingsw.galaxytruckers.view.guiElements;

import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.guiScreens.GuiController;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.Component;
import javafx.application.Platform;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;

public class GuiHand extends HBox {

    private GuiComponent guiHandComponent;

    public GuiHand(Component component, GuiController controller) {
        if (component == null) {
            getChildren().add(createPlaceholder());
        } else {
            guiHandComponent = new GuiComponent(component);
//            guiHandComponent.setOnMouseClicked(_->controller.rotateLeft());
            getChildren().add(guiHandComponent);
        }
    }

    public void notifySetHand(Component component) {
        Platform.runLater(()->{
            getChildren().clear();
            guiHandComponent = new GuiComponent(component);
            getChildren().add(guiHandComponent);
        });
    }

    public void notifyClearHand() {
        Platform.runLater(()->{
            getChildren().clear();
            guiHandComponent = null;
            getChildren().add(createPlaceholder());
        });
    }

    private Region createPlaceholder() {
        Region region = new Region();
        region.setPrefSize(50, 50);
        region.setStyle("-fx-border-color: gray; -fx-border-width: 2;");
        return region;
    }
}
