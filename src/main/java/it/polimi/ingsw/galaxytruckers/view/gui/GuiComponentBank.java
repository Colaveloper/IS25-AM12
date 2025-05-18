package it.polimi.ingsw.galaxytruckers.view.gui;

import it.polimi.ingsw.galaxytruckers.network.shared.VirtualServer;
import it.polimi.ingsw.galaxytruckers.view.cli.CliComponent;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.Component;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.io.IOException;

public class GuiComponentBank extends GuiElement {

    public GuiComponentBank(ClientModel model, VirtualServer server) {
        super(model, server);
    }

    @Override
    public Node getNode() throws IOException {
        VBox covered = createHorizontalPile(server,"Covered:", 1, Color.DODGERBLUE);
        VBox uncovered = createHorizontalPile( server, "Uncovered:", 7, Color.LIMEGREEN);
        VBox stash = createHorizontalPile(server, "Stash", 2, Color.TRANSPARENT);
        VBox hand = createHorizontalPile(server, "Hand", 1, Color.INDIGO);

        HBox row = new HBox(20, hand, stash, covered, uncovered);
        row.setStyle("-fx-padding: 20; -fx-background-color: white;");
        return row;
    }

    private Rectangle card(Color color) {
        Rectangle r = new Rectangle(50,50);
        r.setArcWidth(10);
        r.setArcHeight(10);
        r.setFill(color);
        r.setStroke(Color.BLACK);
        return r;
    }

    private VBox createHorizontalPile(VirtualServer server, String title, int count, Color color) throws IOException {
        VBox box = new VBox(5);
        box.setStyle("-fx-border-color: black; -fx-border-width: 1; -fx-padding: 10;");
        box.getChildren().add(new Label(title));

        HBox cardRow = new HBox(5);
        for (int i = 0; i < count; i++) {
            // Create the card
            Rectangle r = card(color);

            // If it's the "Covered" pile, add a number in the center
            if (color.equals(Color.DODGERBLUE)) {
                Text numberText = new Text();
                numberText.setFill(Color.WHITE);
                numberText.setStyle("-fx-font-size: 20; -fx-font-weight: bold;");
                numberText.textProperty().bind(model.coveredComponentNProperty().asString());

                StackPane stack = new StackPane(r, numberText);
                stack.setPrefSize(50, 50);
                stack.setOnMouseClicked(e -> {
                    try {
                        server.requestRandComponent();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                });
                cardRow.getChildren().add(stack);
            } else if (color.equals(Color.INDIGO)) {
                cardRow.getChildren().add(new GuiComponent(model, server, new Component(1)).getNode());
                model.currentComponentProperty().addListener((obs, oldVal, newVal) -> {
                    cardRow.getChildren().clear();
                    cardRow.getChildren().add(new GuiComponent(model, server, newVal).getNode());
                });
            } else {
                cardRow.getChildren().add(new GuiComponent(model, server, new Component(1)).getNode());
            }
        }

        box.getChildren().add(cardRow);
        return box;
    }
}
