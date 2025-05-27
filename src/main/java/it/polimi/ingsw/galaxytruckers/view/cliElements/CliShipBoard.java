package it.polimi.ingsw.galaxytruckers.view.cliElements;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.GameColor;
import it.polimi.ingsw.galaxytruckers.view.DescriptionUtils;
import it.polimi.ingsw.galaxytruckers.view.cliElements.CliComponents.CliComponent;
import it.polimi.ingsw.galaxytruckers.view.enums.Highlights;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;

import java.awt.*;
import java.util.*;
import java.util.List;

public class CliShipBoard extends CliElement {

    protected final ShipBoard shipBoard;
    private final Map<Point, CliComponent> componentMap;
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
        componentMap = new HashMap<>();
        shipBoard.getComponentMap().forEach(((point, shipBoardCell) -> {
                    componentMap.put(point, CliComponent.of(shipBoardCell.getComponent()));
        }));
        minX = componentMap.keySet().stream().mapToInt(p -> p.x).min().orElse(0);
        maxX = componentMap.keySet().stream().mapToInt(p -> p.x).max().orElse(0);
        minY = componentMap.keySet().stream().mapToInt(p -> p.y).min().orElse(0);
        maxY = componentMap.keySet().stream().mapToInt(p -> p.y).max().orElse(0);
    }

    public void highlightPoints(Set<Point> points, Highlights color){
        for (Point point : points){
            componentMap.get(point).highlight(color);
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
                if (!componentMap.containsKey(new Point(x, y))) {
                    // empty space
                    newCell = List.of("   ", "   ", "   ");
                } else if (componentMap.get(new Point(x, y)) == null) {
                    // empty area
                    newCell = List.of("   ", " X ", "   ");
                } else {
                    newCell = componentMap.get(new Point(x, y)).getNewDescription();
                }
            }
            result.addAll(rowDescription);
        }

        StringBuilder xIndexes = new StringBuilder(" ");
        for (int x = minX; x < maxX; x++) {
            xIndexes.append("   ").append(x).append("  ");
            x++;
        }
        result.add(xIndexes.toString());

        DescriptionUtils.borderAndTitle(
                result,
                color.getDescription()+" "+nickname
        );
        return result;
    }
}