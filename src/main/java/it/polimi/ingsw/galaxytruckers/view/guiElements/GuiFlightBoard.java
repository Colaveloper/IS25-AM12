//package it.polimi.ingsw.galaxytruckers.view.guiElements;
//
//import it.polimi.ingsw.galaxytruckers.model.enumTypes.GameColor;
//import it.polimi.ingsw.galaxytruckers.network.client.ClientController;
//import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
//import javafx.application.Platform;
//import javafx.beans.InvalidationListener;
//import javafx.collections.ListChangeListener;
//import javafx.geometry.Pos;
//import javafx.scene.Node;
//import javafx.scene.layout.HBox;
//import javafx.scene.layout.StackPane;
//import javafx.scene.paint.Color;
//import javafx.scene.shape.Circle;
//import javafx.scene.shape.Polygon;
//
//import java.util.*;
//
//public class GuiFlightBoard extends GuiElement {
//
//    public GuiFlightBoard(ClientModel model, ClientController controller) {
//        super(model, controller);
//    }
//
//    @Override
//    public Node getNode() {
//        final int slotSize = 30;
//        final double spacing = 5.0;
//        final int loopLength = model.getLoopLength();
//
//        HBox container = new HBox(spacing);
//        container.setAlignment(Pos.CENTER);
//        List<StackPane> slots = new ArrayList<>();
//
//        for (int i = 0; i < loopLength; i++) {
//            StackPane slot = new StackPane();
//            slot.setPrefSize(slotSize, slotSize);
//            slots.add(slot);
//            container.getChildren().add(slot);
//        }
//
//        Runnable update = () -> {
//            Set<Integer> starting = new HashSet<>(model.startingPositionLeftProperty());
//            Map<GameColor, Integer> colorMap = new HashMap<>(model.colorToPlaceProperty());
//
//            Platform.runLater(() -> {
//                for (int i = 0; i < loopLength; i++) {
//                    int finalI = i;
//                    StackPane slot = slots.get(i);
//                    slot.getChildren().clear();
//
//                    Optional<GameColor> colorHere = colorMap.entrySet().stream()
//                            .filter(e -> e.getValue() == finalI)
//                            .map(Map.Entry::getKey)
//                            .findFirst();
//
//                    if (colorHere.isPresent()) {
//                        Circle circle = new Circle(slotSize / 2.0);
//                        circle.setFill(colorHere.get().getJfxColor());
//                        slot.getChildren().add(circle);
//                    } else {
//                        Polygon triangle = new Polygon(
//                                0.0, 0.0,
//                                0.0, slotSize,
//                                slotSize, slotSize / 2.0
//                        );
//                        if (starting.contains(finalI)) {
//                            triangle.setFill(Color.BLACK);
//                            triangle.setStroke(null);
//                        } else {
//                            triangle.setFill(Color.TRANSPARENT);
//                            triangle.setStroke(Color.BLACK);
//                        }
//                        slot.getChildren().add(triangle);
//                    }
//                }
//            });
//        };
//
//        update.run();
//        model.startingPositionLeftProperty().addListener((ListChangeListener<Integer>) c -> update.run());
//        model.colorToPlaceProperty().addListener((InvalidationListener) c -> update.run());
//
//        return container;
//    }
//
//}
