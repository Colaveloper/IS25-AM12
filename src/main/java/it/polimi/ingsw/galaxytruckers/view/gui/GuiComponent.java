package it.polimi.ingsw.galaxytruckers.view.gui;

import it.polimi.ingsw.galaxytruckers.network.shared.VirtualServer;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.Component;
import javafx.animation.RotateTransition;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class GuiComponent extends GuiElement {
    private final Component component;
    private final String imagePath;

    public GuiComponent(ClientModel model, VirtualServer server, Component component) {
        super(model, server);
        this.component = component;
        imagePath = component.getNode().get("path").asText();
    }

    @Override
    public Node getNode() {

//            Region square = new Region();
//            square.setPrefSize(20, 20);
//            square.setStyle("-fx-background-color: gray; -fx-border-color: black;");
//            return square;

        Image image = new Image(imagePath);
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(50);
        imageView.setFitHeight(50);
        imageView.setRotate(0); // initial rotation

        // Track and force counterclockwise rotation (by -90° per click)
        component.directionProperty().addListener((obs, oldVal, newVal) -> {
            // Counterclockwise rotation: Always rotate by -90°
            RotateTransition rt = new RotateTransition(Duration.millis(300), imageView);
            rt.setByAngle(-90); // negative to rotate counterclockwise
            rt.play();
        });

        // Click to increment component.directionProperty() (counterclockwise)
        imageView.setOnMouseClicked(e -> component.rotateLeft());

        return imageView;
    }
}
