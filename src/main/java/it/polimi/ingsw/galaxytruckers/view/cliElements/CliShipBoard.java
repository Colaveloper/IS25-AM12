package it.polimi.ingsw.galaxytruckers.view.cliElements;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.GameColor;
import it.polimi.ingsw.galaxytruckers.view.DescriptionUtils;
import it.polimi.ingsw.galaxytruckers.view.cliElements.CliComponents.CliComponent;
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
    protected final Map<Point, CliComponent> cliComponentMap;
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

        //shipBoard.getShipArea().forEach(p-> cliComponentMap.put(p, null));
        Set<Point> shipArea = shipBoard.getShipArea();

        minX = shipArea.stream().mapToInt(p -> p.x).min().orElse(0);
        maxX = shipArea.stream().mapToInt(p -> p.x).max().orElse(0);
        minY = shipArea.stream().mapToInt(p -> p.y).min().orElse(0);
        maxY = shipArea.stream().mapToInt(p -> p.y).max().orElse(0);

        for (Point p : shipBoard.getComponentMap().keySet()) {
            cliComponentMap.put(p, CliComponent.of(shipBoard.getComponentMap().get(p)));
        }
    }

    public void onPutComponent(Point p, Component newValue) {
        CliComponent newCliComponent = CliComponent.of(newValue);
        cliComponentMap.put(p, newCliComponent);
    }

    public void onRemoveComponent(Point p) {
        cliComponentMap.get(p).removeObserver(this);
        cliComponentMap.remove(p);
    }

    public void highlightPoints(Set<Point> points, Highlights color){
        for (Point point : points){
            cliComponentMap.get(point).highlight(color);
        }
    }

    @Override
    protected List<String> getNewDescription(){

        List<String> result = new ArrayList<>();
        List<String> rowDescription = new ArrayList<>();

        for (int y = minY; y <= maxY; y++) {
            rowDescription.clear();
            rowDescription.addAll(List.of("",String.valueOf(y),""));
            for (int x = minX; x <= maxX; x++) {
                List<String> newCell = new ArrayList<>();
                if (!cliComponentMap.containsKey(new Point(x, y))) {
                    // empty-space
                    newCell = List.of("     ", "     ", "     ");
                } else if (cliComponentMap.get(new Point(x, y)) == null) {
                    // empty-area
                    newCell = List.of("     ", "  X  ", "     ");
                } else {
                    newCell = cliComponentMap.get(new Point(x, y)).getDescription();
                }
                rowDescription = DescriptionUtils.sideBySide(rowDescription, newCell);
            }
            result.addAll(rowDescription);
        }

        StringBuilder xIndexes = new StringBuilder("   ");
        for (int x = minX; x <= maxX; x++) {
            xIndexes.append("  ").append(x).append("   ");
        }
        result.add(xIndexes.toString());

        result = DescriptionUtils.borderAndTitle(
                result,
                color.getDescription()+" "+nickname
        );
        return result;
    }
}