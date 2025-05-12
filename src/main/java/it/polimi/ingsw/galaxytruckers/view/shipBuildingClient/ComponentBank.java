package it.polimi.ingsw.galaxytruckers.view.shipBuildingClient;

import it.polimi.ingsw.galaxytruckers.network.shared.VirtualServer;
import it.polimi.ingsw.galaxytruckers.view.CliElement;
import it.polimi.ingsw.galaxytruckers.view.viewEnums.ComponentType;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ComponentBank extends CliElement {
    private ObjectProperty<CliComponent> currentComponent;

    public ComponentBank() {
        stashedComponents = new ArrayList<>();
        currentComponent = new SimpleObjectProperty<>(new CliComponent(ComponentType.EMPTY_AREA));
        currentComponent.get().setChangeListener(this);
        super.registerObservables(currentComponent);
        super.registerObservables(coveredComponentN);
    }

    public void setStashedComponents(List<Integer> components) throws IOException {
        stashedComponents.clear();
        for (Integer componentId : components) {
            stashedComponents.add(new CliComponent(0, componentId));
        }
    }

    public void addRevealedComponent(int componentId) throws IOException {
        revealedComponents.add(new CliComponent(0, componentId));
    }

    public void removeRevealedComponent(int componentId) throws IOException {
        revealedComponents.remove(new CliComponent(0, componentId));
    }

    public void setRevealedComponents(List<Integer> components) throws IOException {
        revealedComponents.clear();
        for (Integer componentId : components) {
            revealedComponents.add(new CliComponent(0, componentId));
        }
    }

    public void setCoveredComponentN(int coveredComponentsN) {
        this.coveredComponentN.set(coveredComponentsN);
    }

    public void setCurrentComponent(int componentId) throws IOException {
        currentComponent.set(new CliComponent(0, componentId));
        currentComponent.get().setChangeListener(this);
    }

    public void rotateCurrentComponentLeft() {
        currentComponent.get().rotateLeft();
    }

    public void clearCurrentComponent() {
        currentComponent.set(new CliComponent(ComponentType.EMPTY_AREA));
    }

    public IntegerProperty coveredComponentNProperty() {
        return coveredComponentN;
    }

    public IntegerProperty currentComponentDirectionProperty() {
        return currentComponent.get().directionProperty();
    }

    @Override
    public List<String> getNewDescription() {
        String padding = "  ";
        StringBuilder row = new StringBuilder();
        List<String> description = new ArrayList<>();

        description.add("Face down: " + coveredComponentN.get());

        description.add("Face up: ");
        for (int i = 0; i < 3; i++) {
            for (CliComponent component : revealedComponents) {
                row.append(component.getDescription().get(i));
//                System.out.println("REVEALED");
                row.append(padding);
            }
            description.add(row.toString());
            row.setLength(0);
        }
        for (int n = 1; n <= revealedComponents.size(); n++) {
            row.append("  ").append(n).append("  ").append(padding);
        }
        description.add(row.toString());
        row.setLength(0);

        description.add("Stash: " + "\tHand: ");
        for (int i = 0; i < 3; i++) {
            for (CliComponent component : stashedComponents) {
                row.append(component.getDescription().get(i));
                System.out.println("STASHED");
                row.append(padding);
            }
            for (int n = 0; n < 2 - stashedComponents.size(); n++) {
                row.append("     ").append(padding);
            }
            row.append("\t\t\t");
            row.append(currentComponent.get().getDescription().get(i));
            description.add(row.toString());
            row.setLength(0);
        }
        for (int n = 0; n < stashedComponents.size(); n++) {
            row.append("  ").append((char) ('A' + n)).append("  ").append(padding);
        }
        description.add(row.toString());

        return description;
    }

    @Override
    public Node getNode(VirtualServer server) throws IOException {
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
                numberText.textProperty().bind(coveredComponentN.asString());

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
                cardRow.getChildren().add(new CliComponent(0, 12).getNode(server));
                currentComponent.addListener((obs, oldVal, newVal) -> {
                    cardRow.getChildren().clear();
                    cardRow.getChildren().add(newVal.getNode(server));
                });
            } else {
                cardRow.getChildren().add(new CliComponent(0, 12).getNode(server));
            }
        }

        box.getChildren().add(cardRow);
        return box;
    }
}
