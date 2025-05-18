package it.polimi.ingsw.galaxytruckers.view.gui;

import it.polimi.ingsw.galaxytruckers.network.shared.VirtualServer;
import it.polimi.ingsw.galaxytruckers.view.cli.CliComponent;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.Component;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.awt.event.MouseEvent;
import java.io.IOException;

public class GuiComponentBank extends GuiElement {

    public GuiComponentBank(ClientModel model, VirtualServer server) {
        super(model, server);
    }

    @Override
    public Node getNode() throws IOException {
        HBox box = new HBox(5);
        box.setAlignment(Pos.CENTER);
        ObservableList<Node> squares = box.getChildren();
        squares.add(covered());

        model.revealedComponentsProperty().addListener((ListChangeListener<Component>) change -> {
            while (change.next()) {
                Platform.runLater(() -> {
                    if (change.wasRemoved()) {
                        squares.remove(change.getFrom(), change.getFrom() + change.getRemovedSize());
                    }
                    if (change.wasAdded()) {
                        int index = change.getFrom();
                        for (Component component : change.getAddedSubList()) {
                            squares.add(index++, new GuiComponent(model, server, component).getNode());
                        }
                    }
                });
            }
        });

        return box;
    }

    private Node covered() {
        StackPane square = new StackPane();
        square.setPrefSize(50, 50);
        square.setStyle("-fx-background-color: lightgray; -fx-border-color: black;");

        Label label = new Label();
        model.coveredComponentNProperty().addListener((observable, oldValue, newValue) -> {
            Platform.runLater(() -> label.textProperty().set(newValue.toString()));
        });

        square.getChildren().add(label);

        square.setOnMouseClicked(event -> {
            try {
                server.requestRandComponent();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        return square;
    }
}
