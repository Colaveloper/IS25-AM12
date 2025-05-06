package it.polimi.ingsw.galaxytruckers.view.screens;

import it.polimi.ingsw.galaxytruckers.network.shared.VirtualServer;
import it.polimi.ingsw.galaxytruckers.view.ClientModel;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.awt.*;
import java.io.IOException;

public class ShipBuildingScreen implements ScreenStrategy {

    @Override
    public void showCLI(ClientModel model) {
        model.getComponentBank().getDescription().forEach(System.out::println);
        model.getAllShips().getDescription().forEach(System.out::println);

        // always allowed
        System.out.println("C       \tGet New covered component");
        System.out.println("U [i]   \tGet i-th uncovered component");
        System.out.println("S [i]   \tGet i-th stashed component");         // NOT IN Levels.TEST
        System.out.println("F [i]   \tGet i-th forecast deck");             // NOT IN Levels.TEST

        // allowed if existsUnwelded
        if (model.existsUnwelded()) {
            System.out.println("R       \tReject current component");
            System.out.println("P [x] [y] \tPlace current component in position x, y");
            System.out.println("S       \tStash current component");        // NOT IN Levels.TEST
            System.out.println("L       \tRotate current component left");
        }

        // allowed if hourglassTime==0 and flipsLeft>1
        // (or flipsLeft==1 and building is over, see FinishBuildingScreen)
        System.out.println("H       \tFlip hourglass");                     // NOT IN Levels.TEST
    }

    @Override
    public boolean isLegalInput(ClientModel model, String input) {
        return input.matches("^(C|U \\d+|S(?: \\d+)?|F \\d+|R|P \\d+ \\d+|L|H)$\n");
    }

    @Override
    public void parseAndInvoke(ClientModel model, String input, VirtualServer server) throws IOException {
        String[] parts = input.split("\\s+");
        switch (parts[0]) {
            case "C":
                server.requestRandComponent();
                break;

            case "U":
                if (parts.length == 2) {
                    int index = Integer.parseInt(parts[1]);
                    server.requestComponent(index);
                }
                break;

            case "S":
                if (parts.length == 1) {
                    server.stashComponent();
                } else if (parts.length == 2) {
                    int index = Integer.parseInt(parts[1]);
                    server.grabStashedComponent(index);
                }
                break;

            case "F":
                if (parts.length == 2) {
                    int index = Integer.parseInt(parts[1]);
                    server.acquireForecast(index);
                }
                break;

            case "R":
                server.rejectComponent();
                break;

            case "P":
                if (parts.length == 3) {
                    int x = Integer.parseInt(parts[1]);
                    int y = Integer.parseInt(parts[2]);
                    server.placeComponent(new Point(x, y));
                }
                break;

            case "L":
                // TODO: HANDLE ROTATION LOCALLY
                break;

            case "H":
                server.flipHourglass();
                break;

            default:
                // should be impossible
                System.out.println("Invalid command.");
                break;
        }
    }

    @Override
    public void showGUI(ClientModel model, Pane root, VirtualServer server) {
        VBox covered = createHorizontalPile(model, "Covered:", 1, Color.DODGERBLUE);
        VBox uncovered = createHorizontalPile(model, "Uncovered:", 7, Color.LIMEGREEN);
        VBox stash = createHorizontalPile(model, "Stash", 2, Color.TRANSPARENT);
        VBox hand = createHorizontalPile(model, "Hand", 1, Color.TRANSPARENT);

        HBox row = new HBox(20, hand, stash, covered, uncovered);
        row.setStyle("-fx-padding: 20; -fx-background-color: white;");

        root.getChildren().add(row);
    }

    private Rectangle card(Color color) {
        Rectangle r = new Rectangle(50,50);
        r.setArcWidth(10);
        r.setArcHeight(10);
        r.setFill(color);
        r.setStroke(Color.BLACK);
        return r;
    }

    private VBox createHorizontalPile(ClientModel model, String title, int count, Color color) {
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
                numberText.textProperty().bind(model.coveredComponentsProperty().asString());

                StackPane stack = new StackPane(r, numberText);
                stack.setPrefSize(50, 50);
                cardRow.getChildren().add(stack);
            } else {
                cardRow.getChildren().add(r);
            }
        }

        box.getChildren().add(cardRow);
        return box;
    }


}
