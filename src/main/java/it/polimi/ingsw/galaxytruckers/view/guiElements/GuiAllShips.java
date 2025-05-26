package it.polimi.ingsw.galaxytruckers.view.guiElements;


import it.polimi.ingsw.galaxytruckers.model.enumTypes.GameColor;
import it.polimi.ingsw.galaxytruckers.network.client.ClientController;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.Component;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

import java.util.Map;

public class GuiAllShips extends GuiElement {

    public GuiAllShips(ClientModel model, ClientController controller) {
        super(model, controller);
    }

    public Node getNode() {
        HBox handBox = new HBox(10);
        handBox.setAlignment(Pos.CENTER);

        Map<GameColor, ObjectProperty<Component>> hand = model.getHand();

        for (Map.Entry<GameColor, ObjectProperty<Component>> entry : hand.entrySet()) {
            GameColor color = entry.getKey();
            ObjectProperty<Component> componentProperty = entry.getValue();

            VBox cardBox = new VBox(5);
            cardBox.setAlignment(Pos.CENTER);

            // HBox for circle + text side-by-side
            HBox colorBox = new HBox(5);
            colorBox.setAlignment(Pos.CENTER);

            // Circle with color
            Circle colorCircle = new Circle(20, color.getJfxColor());
            colorBox.getChildren().add(colorCircle);

            // Text next to circle with player name from inverse map
            String playerName = model.getPlayerToColor().inverse().get(color);
            Text playerText = new Text(playerName);
            colorBox.getChildren().add(playerText);

            cardBox.getChildren().add(colorBox);

            // Placeholder for the component Node
            StackPane componentContainer = new StackPane();
            componentContainer.setPrefSize(60, 60); // Or adjust size as needed
            // Listener to update on property change
            componentProperty.addListener((obs, oldVal, newVal) -> {
                Platform.runLater(() -> {
                    componentContainer.getChildren().setAll(
                            newVal != null
                                    ? new GuiComponent(model, controller, newVal).getNode()
                                    : new Label("?"));
                });
            });

            // Initial content
            GuiComponent initial = new GuiComponent(model, controller, componentProperty.get());
            componentContainer.getChildren().add(initial != null ? initial.getNode() : new Label("?"));

            GuiShipBoard shipBoard = new GuiShipBoard(model, controller, color);

            handBox.getChildren().add(cardBox);
            cardBox.getChildren().add(componentContainer);
            cardBox.getChildren().add(shipBoard.getNode());
        }

        return handBox;
    }

}
