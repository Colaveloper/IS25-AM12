package it.polimi.ingsw.galaxytruckers.view;

import javafx.scene.image.Image;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Shipboard implements Physical{

    private final List<List<Component>> componentMatrix;
    private Point upLeft;
    private Point lastPosition;
    private int credits;
    private int losses;
    private int firepower;
    private int numBatteries;
    private int crewSize;

    public Shipboard(Map<Point, Component> componentMap) {
        componentMatrix = componentMatrixConstructor(componentMap);
    }

    public List<List<Component>> componentMatrixConstructor(Map<Point, Component> map) {
        if (map.isEmpty()) return List.of();

        // Bounds
        int minX = map.keySet().stream().mapToInt(p -> p.x).min().orElse(0);
        int maxX = map.keySet().stream().mapToInt(p->p.x).max().orElse(0);
        int minY = map.keySet().stream().mapToInt(p->p.y).min().orElse(0);
        int maxY = map.keySet().stream().mapToInt(p->p.y).max().orElse(0);

        // Save top-left point
        upLeft = new Point(minX, minY);

        List<List<Component>> result = new ArrayList<>();

        for (int y = minY; y <= maxY; y++) {
            List<Component> row = new ArrayList<>();
            for (int x = minX; x <= maxX; x++) {
                row.add(map.getOrDefault(new Point(x, y), new Component(ComponentType.NONE)));
            }
            result.add(row);
        }

        return result;
    }



    public Image getImage() {
        return null;
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
}