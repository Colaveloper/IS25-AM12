package it.polimi.ingsw.galaxytruckers.view.cliElements;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.GameColor;
import it.polimi.ingsw.galaxytruckers.view.DescriptionUtils;
import it.polimi.ingsw.galaxytruckers.view.cliElements.CliComponents.CliComponent;
import it.polimi.ingsw.galaxytruckers.view.cliScreens.MapListener;
import it.polimi.ingsw.galaxytruckers.view.enums.Highlights;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.Component;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;

import java.awt.*;
import java.util.*;
import java.util.List;

public class CliShipBoard extends CliElement {

    protected final ShipBoard shipBoard;

    // no key = empty-space,
    // no value = empty-area
    private final Map<Point, CliComponent> cliComponentMap;
    private final GameColor color;
    private final String nickname;
    int minX;
    int maxX;
    int minY;
    int maxY;

    public CliShipBoard(ShipBoard shipBoard, String nickname) {
        this.shipBoard = shipBoard;
        this.nickname = nickname;
        this.color = shipBoard.getColor();

        cliComponentMap = new HashMap<>();

        shipBoard.getComponentMap().addListener(new MapListener<>() {
            @Override
            public void onPut(Point p, Component oldValue, Component newValue) {
                onPutComponent(p, oldValue, newValue);
            }

            @Override
            public void onRemove(Point p, Component oldValue) {
                onRemoveComponent(p, oldValue);
            }
        });

        shipBoard.getShipArea().forEach(p-> cliComponentMap.put(p, null));

        minX = cliComponentMap.keySet().stream().mapToInt(p -> p.x).min().orElse(0);
        maxX = cliComponentMap.keySet().stream().mapToInt(p -> p.x).max().orElse(0);
        minY = cliComponentMap.keySet().stream().mapToInt(p -> p.y).min().orElse(0);
        maxY = cliComponentMap.keySet().stream().mapToInt(p -> p.y).max().orElse(0);
    }

    private void onPutComponent(Point p, Component oldValue, Component newValue) {
        CliComponent newCliComponent = CliComponent.of(newValue);
        newCliComponent.addObserver(this);
        cliComponentMap.put(p, newCliComponent);
    }

    private void onRemoveComponent(Point p, Component oldValue) {
        cliComponentMap.get(p).removeObserver(this);
        cliComponentMap.put(p, null);
    }

    public void highlightPoints(Set<Point> points, Highlights color){
        for (Point point : points){
            cliComponentMap.get(point).highlight(color);
        }
    }

    @Override
    public List<String> getNewDescription(){

        List<String> result = new ArrayList<>();
        List<String> rowDescription = new ArrayList<>();

        for (int y = minY; y <= maxY; y++) {
            rowDescription.clear();
            rowDescription.addAll(List.of("",String.valueOf(y),""));
            for (int x = minX; x <= maxX; x++) {
                List<String> newCell = new ArrayList<>();
                if (!cliComponentMap.containsKey(new Point(x, y))) {
                    // empty-space
                    newCell = List.of("   ", "   ", "   ");
                } else if (cliComponentMap.get(new Point(x, y)) == null) {
                    // empty-area
                    newCell = List.of("   ", " X ", "   ");
                } else {
                    newCell = cliComponentMap.get(new Point(x, y)).getNewDescription();
                }
                rowDescription = DescriptionUtils.sideBySide(rowDescription, newCell);
            }
            result.addAll(rowDescription);
        }

        StringBuilder xIndexes = new StringBuilder(" ");
        for (int x = minX; x < maxX; x++) {
            xIndexes.append("   ").append(x).append("  ");
            x++;
        }
        result.add(xIndexes.toString());

        result = DescriptionUtils.borderAndTitle(
                result,
                color.getDescription()+" "+nickname
        );
        return result;
    }
}