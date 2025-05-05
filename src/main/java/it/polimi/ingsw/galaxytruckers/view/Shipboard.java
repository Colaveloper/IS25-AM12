package it.polimi.ingsw.galaxytruckers.view;

import it.polimi.ingsw.galaxytruckers.view.viewEnums.ComponentType;
import javafx.scene.image.Image;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Shipboard extends Physical{

    private List<List<Component>> componentMatrix;
    private Point upLeft;
    private Point lastPosition;

    private int lostComponents;

    private int credits;
    private int losses;                 //TODO: what s this
    private int firepower;
    private int numBatteries;
    private int crewSize;

    public Shipboard() {
        componentMatrix = new ArrayList<>();
    }

    public void setShipArea(Set<Point> shipArea) {

        // Bounds
        int minX = shipArea.stream().mapToInt(p -> p.x).min().orElse(0);
        int maxX = shipArea.stream().mapToInt(p -> p.x).max().orElse(0);
        int minY = shipArea.stream().mapToInt(p -> p.y).min().orElse(0);
        int maxY = shipArea.stream().mapToInt(p -> p.y).max().orElse(0);

        // Save top-left point
        upLeft = new Point(minX, minY);

        List<List<Component>> result = new ArrayList<>();

        for (int y = minY; y <= maxY; y++) {
            List<Component> row = new ArrayList<>();
            for (int x = minX; x <= maxX; x++) {
                row.add(shipArea.contains(new Point(x, y))
                        ? new Component(ComponentType.EMPTY_AREA)
                        : new Component(ComponentType.EMPTY_SPACE));
            }
            result.add(row);
        }
        componentMatrix = result;
    }



    public void setComponent(Point position, int direction, int componentId) throws IOException {
        Component component = new Component(direction, componentId);
        componentMatrix.get(position.y-upLeft.y).set(position.x-upLeft.x, component);
        lastPosition = position;
    }

    public void removeComponent(Point position) throws IOException {
                componentMatrix.get(position.y-upLeft.y).set(position.x-upLeft.x, new Component(ComponentType.EMPTY_SPACE));
    }

    public Component getComponent(Point point) throws IOException {
        return componentMatrix.get(point.y-upLeft.y).get(point.x-upLeft.x);
    }

    public void setStashedComponents(int componentId) {

    }

    public List<String> getDescription() {

        List<String> result = new ArrayList<>();

        int height = componentMatrix.size();
        int width = componentMatrix.getFirst().size();
        int componentHeight = componentMatrix.getFirst().getFirst().getDescription().size();
        int componentWidth = componentMatrix.getFirst().getFirst().getDescription().getFirst().length();

        int yIndex = upLeft.y;

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < componentHeight; j++) {
                StringBuilder row = new StringBuilder();

                // declaring y index
                if (j == componentHeight/2) {
                    row.append(yIndex).append(" ".repeat(componentWidth-1));
                    yIndex++;
                } else {
                    row.append(" ".repeat(componentWidth));
                }

                // component
                for (int k = 0; k < width; k++) {
                    Component component = componentMatrix.get(i).get(k);
                    List<String> description = component.getDescription();
                    row.append(description.get(j));
                }

                result.add(row.toString());
            }
        }

        StringBuilder xIndexes = new StringBuilder();
        int xIndex = upLeft.x;
        xIndexes.append(" ".repeat(componentWidth));
        for (int i = 0; i < width; i++) {
            xIndexes.append(" ".repeat(componentWidth / 2)).append(xIndex).append(" ".repeat(componentWidth / 2));
            xIndex++;
        }
        result.add(xIndexes.toString());

        return result;
    }

    public void setSelectablePoints(List<Point> selectablePoints) {
        for (Point position : selectablePoints) {
            componentMatrix.get(position.y-upLeft.y).get(position.x-upLeft.x).setSelectable();
        }
    }
}