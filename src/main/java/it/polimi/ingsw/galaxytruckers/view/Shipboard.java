package it.polimi.ingsw.galaxytruckers.view;

import it.polimi.ingsw.galaxytruckers.network.shared.VirtualServer;
import it.polimi.ingsw.galaxytruckers.view.cli.CliComponent;
import it.polimi.ingsw.galaxytruckers.view.cli.CliElement;
import it.polimi.ingsw.galaxytruckers.view.viewEnums.ComponentType;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Shipboard extends CliElement {

    private List<List<ObjectProperty<CliComponent>>> componentMatrix;
    private Point upLeft;
    private Point lastPosition;

    private IntegerProperty lostComponents;
    private IntegerProperty credits;

    private IntegerProperty firepower;
    private IntegerProperty numBatteries;
    private IntegerProperty crewSize;

    public Shipboard() {
        componentMatrix = new ArrayList<>();
        lostComponents = new SimpleIntegerProperty(0);
        credits = new SimpleIntegerProperty(0);
        firepower = new SimpleIntegerProperty(0);
        numBatteries = new SimpleIntegerProperty(0);
        crewSize = new SimpleIntegerProperty(0);
        super.registerObservables(lostComponents, credits, firepower, numBatteries, crewSize);

    }

    public void setShipArea(Set<Point> shipArea) {
        // Bounds
        int minX = shipArea.stream().mapToInt(p -> p.x).min().orElse(0);
        int maxX = shipArea.stream().mapToInt(p -> p.x).max().orElse(0);
        int minY = shipArea.stream().mapToInt(p -> p.y).min().orElse(0);
        int maxY = shipArea.stream().mapToInt(p -> p.y).max().orElse(0);

        // Save top-left point
        upLeft = new Point(minX, minY);

        List<List<ObjectProperty<CliComponent>>> result = new ArrayList<>();

        ObjectProperty<CliComponent> component;

        for (int y = minY; y <= maxY; y++) {
            List<ObjectProperty<CliComponent>> row = new ArrayList<>();
            for (int x = minX; x <= maxX; x++) {

                if (shipArea.contains(new Point(x, y))) {
                    component = new SimpleObjectProperty<>(new CliComponent(ComponentType.EMPTY_AREA));
                }
                else {
                    component = new SimpleObjectProperty<>(new CliComponent(ComponentType.EMPTY_SPACE));
                }
                row.add(component);
                super.registerObservables(component);
                component.get().setChangeListener(this);

//                row.add(shipArea.contains(new Point(x, y))
//                        ? new SimpleObjectProperty<>(new CliComponent(ComponentType.EMPTY_AREA))
//                        : new SimpleObjectProperty<>(new CliComponent(ComponentType.EMPTY_SPACE))
//                );
            }
            result.add(row);
        }
        componentMatrix = result;
    }

    public void setCredits(int creditsToAdd) {
        credits.set(creditsToAdd);
    }

    public void setLostComponent(int losses) {
        lostComponents.set(losses);
    }

    public int getCredits() {
        return credits.get();
    }

    public int getLostComponents() {
        return lostComponents.get();
    }

    public void setComponent(Point position, int direction, int componentId) throws IOException {
        componentMatrix.get(position.y-upLeft.y).get(position.x-upLeft.x).set(new CliComponent(direction, componentId));
        componentMatrix.get(position.y-upLeft.y).get(position.x-upLeft.x).get().setChangeListener(this);
        lastPosition = position;
    }

    public void removeComponent(Point position) throws IOException {
                componentMatrix.get(position.y-upLeft.y).set(position.x-upLeft.x, new SimpleObjectProperty<>(new CliComponent(ComponentType.EMPTY_SPACE)));
    }

    public CliComponent getComponent(Point point) throws IOException {
        return componentMatrix.get(point.y-upLeft.y).get(point.x-upLeft.x).get();
    }

    public void setStashedComponents(int componentId) {

    }

    public List<String> getNewDescription() {

        List<String> result = new ArrayList<>();

        int height = componentMatrix.size();
        int width = componentMatrix.getFirst().size();
        int componentHeight = componentMatrix.getFirst().getFirst().get().getDescription().size();
        int componentWidth = componentMatrix.getFirst().getFirst().get().getDescription().getFirst().length();

        int yIndex = upLeft.y;
        int yIndexPadding = 2;
        int rightShipboardPadding = 1;

        StringBuilder row = new StringBuilder();
        row.append("╭");
        row.append("─".repeat(width * componentWidth + yIndexPadding + 2));
        row.append("╮").append(" ".repeat(rightShipboardPadding));
        result.add(row.toString());

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < componentHeight; j++) {
                row = new StringBuilder();

                row.append("│ ");
                // declaring y index
                if (j == componentHeight/2) {
                    //row.append(yIndex).append(" ".repeat(yIndexPadding-1));
                    row.append(yIndex).append(" ".repeat(
                            yIndex > 9 ? yIndexPadding - 2 : yIndexPadding - 1
                    ));
                    yIndex++;
                } else {
                    row.append(" ".repeat(yIndexPadding));
                }

                // component
                for (int k = 0; k < width; k++) {
                    ObjectProperty<CliComponent> component = componentMatrix.get(i).get(k);
                    List<String> description = component.get().getNewDescription();
                    row.append(description.get(j));
                }
                row.append(" │").append(" ".repeat(rightShipboardPadding));

                result.add(row.toString());
            }
        }

        StringBuilder xIndexes = new StringBuilder();
        int xIndex = upLeft.x;
        xIndexes.append("│ ");
        xIndexes.append(" ".repeat(yIndexPadding));
        for (int i = 0; i < width; i++) {
            xIndexes.append(" ".repeat((componentWidth - 1) / 2)).append(xIndex).append(" ".repeat(xIndex > 9 ? ((componentWidth - 1) / 2) - 1 : componentWidth / 2));
            xIndex++;
        }
        xIndexes.append(" │").append(" ".repeat(rightShipboardPadding));
        result.add(xIndexes.toString());

        row = new StringBuilder();
        row.append("╰");
        row.append("─".repeat(width * componentWidth + yIndexPadding + 2));
        row.append("╯").append(" ".repeat(rightShipboardPadding));
        result.add(row.toString());

        return result;
    }

    @Override
    public Node getNode(VirtualServer server) {
        return null;
    }

    public void setSelectablePoints(List<Point> selectablePoints) {
        for (Point position : selectablePoints) {
            componentMatrix.get(position.y-upLeft.y).get(position.x-upLeft.x).get().setSelectable();
        }
    }
}