package it.polimi.ingsw.galaxytruckers.view.guiElements;

import it.polimi.ingsw.galaxytruckers.network.client.ClientController;
import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.controller.ComponentRegistry;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.Component;
import javafx.animation.RotateTransition;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class GuiComponent extends ImageView {

//    private boolean isRotatable = false;

    public GuiComponent(Component component, ControllerToServer controller) {
        GuiComponentRegistry registry = GuiComponentRegistry.getInstance();
        super.setImage(registry.getImage(component.getId()));
        super.setFitWidth(50);
        super.setFitHeight(50);
    }



//    @Override
//    public Node getNode() {
//
////            Region square = new Region();
////            square.setPrefSize(20, 20);
////            square.setStyle("-fx-background-color: gray; -fx-border-color: black;");
////            return square;

//        imageView.setRotate(0); // initial rotation
//
//        // Track and force counterclockwise rotation (by -90° per click)
//        component.directionProperty().addListener((obs, oldVal, newVal) -> {
//            // Counterclockwise rotation: Always rotate by -90°
//            RotateTransition rt = new RotateTransition(Duration.millis(300), imageView);
//            rt.setByAngle(-90); // negative to rotate counterclockwise
//            rt.play();
//        });
//
//        // Click to increment component.directionProperty() (counterclockwise)
//        imageView.setOnMouseClicked(e -> component.rotateLeft());
//
//        return imageView;
//    }
}
