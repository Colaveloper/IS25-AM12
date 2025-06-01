package it.polimi.ingsw.galaxytruckers.view.guiElements;

import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.Component;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ComponentBank;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

public class GuiComponentBank extends HBox {

    private final ControllerToServer controller;
    private final Label coveredNLabel;
    private final FlowPane rejectedContainer;

    public GuiComponentBank(ComponentBank componentBank, ControllerToServer controller) {
        this.controller = controller;

        setSpacing(20);
        setPadding(new Insets(10));
        setAlignment(Pos.TOP_LEFT);

        // === Left container ===
        VBox leftBox = new VBox(5);
        leftBox.setAlignment(Pos.TOP_CENTER);
        Label coveredLabel = new Label("Covered");

        StackPane coveredSquare = new StackPane();
        Rectangle square = new Rectangle(100, 100);
        square.setFill(Color.BLACK);
        Label question = new Label("?");
        question.setTextFill(Color.WHITE);
        question.setFont(Font.font(24));

        coveredNLabel = new Label(Integer.toString(componentBank.getCoveredComponentsN()));
        coveredNLabel.setFont(Font.font(14));
        coveredNLabel.setTextFill(Color.GRAY);

        coveredSquare.getChildren().addAll(square, question);
        leftBox.getChildren().addAll(coveredLabel, coveredSquare, coveredNLabel);

        // === Right container ===
        VBox rightBox = new VBox(5);
        rightBox.setAlignment(Pos.TOP_LEFT);
        Label rejectedLabel = new Label("Rejected");

        rejectedContainer = new FlowPane();
        rejectedContainer.setHgap(5);
        rejectedContainer.setVgap(5);
        rejectedContainer.setPrefWrapLength(300); // Will wrap items when full width is reached

        componentBank.getUncoveredComponents().forEach(component -> {

        });

        rightBox.getChildren().addAll(rejectedLabel, rejectedContainer);

        // === Final layout ===
        getChildren().addAll(leftBox, rightBox);
    }

    public void notifyRequestRandComponent() {
        Platform.runLater(()-> {
            coveredNLabel.setText(Integer.toString(
                    Integer.parseInt(coveredNLabel.getText()) - 1)
            );
        });
    }

    public void addUncovered(Component component) {
        Platform.runLater(()-> {
            GuiComponent newComponent = new GuiComponent(component, controller);
            rejectedContainer.getChildren().add(newComponent);
        });
    }
}

//    @Override
//    public Node getNode() throws IOException {
//        HBox box = new HBox(5);
//        box.setAlignment(Pos.CENTER);
//        ObservableList<Node> squares = box.getChildren();
//        squares.add(covered());
//        squares.add(revealedComponents());
//
//        model.revealedComponentsProperty().addListener((ListChangeListener<Component>) change -> {
//            while (change.next()) {
//                Platform.runLater(() -> {
//                    if (change.wasRemoved()) {
//                        squares.remove(change.getFrom(), change.getFrom() + change.getRemovedSize());
//                    }
//                    if (change.wasAdded()) {
//                        int index = change.getFrom();
//                        for (Component component : change.getAddedSubList()) {
//                            squares.add(index++, new GuiComponent(model, controller, component).getNode());
//                        }
//                    }
//                });
//            }
//        });
//
//        return box;
//    }
//
//    private Node covered() {
//        StackPane square = new StackPane();
//        square.setPrefSize(50, 50);
//        square.setStyle("-fx-background-color: lightgray; -fx-border-color: black;");
//
//        Label label = new Label();
//        label.textProperty().bind(model.coveredComponentNProperty().asString());
//
//        square.getChildren().add(label);
//
//        square.setOnMouseClicked(event -> {
//            controller.requestRandComponent();
//        });
//
//        return square;
//    }
//
//    private Node revealedComponents() {
//        VBox container = new VBox(10); // Spacing between components
//        container.setAlignment(Pos.TOP_LEFT); // Optional alignment
//        ListProperty<Component> componentList = model.revealedComponentsProperty();
//        componentList.addListener((ListChangeListener<Component>) change -> {
//            Platform.runLater(() -> {
//                container.getChildren().clear();
//                for (Component component : componentList) {
//                    GuiComponent guiComponent = new GuiComponent(model, controller, component);
//                    container.getChildren().add(guiComponent.getNode());
//                }
//            });
//        });
//
//        // Initial population
//        for (Component component : componentList) {
//            GuiComponent guiComponent = new GuiComponent(model, controller, component);
//            container.getChildren().add(guiComponent.getNode());
//        }
//
//        return container;
//    }

