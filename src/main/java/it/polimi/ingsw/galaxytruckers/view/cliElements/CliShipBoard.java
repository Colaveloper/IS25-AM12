package it.polimi.ingsw.galaxytruckers.view.cliElements;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.FourColors;
import it.polimi.ingsw.galaxytruckers.view.DescriptionUtils;
import it.polimi.ingsw.galaxytruckers.view.cliElements.CliComponents.CliComponent;
import it.polimi.ingsw.galaxytruckers.view.cliElements.CliComponents.CliComponentFactory;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoardCell;

import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class CliShipBoard extends CliElement {

    private final ShipBoard shipBoard;
    private final Map<Point, CliComponent> componentMap;
    Point upLeft;
    private final FourColors color;

    public CliShipBoard(ClientModel model, ShipBoard shipBoard) {
        super(model);
        this.shipBoard = shipBoard;
        this.color = shipBoard.getColor();
        upLeft = getUpLeft(shipBoard.getShipArea());
        componentMap = new HashMap<>();
        shipBoard.getComponentMap().forEach(((point, shipBoardCell) -> {
                    componentMap.put(point, CliComponentFactory.createCliComponent(shipBoardCell.getComponent()));
                }))
                        .map(componentProperty -> {
                            try {
                                CliComponent cliComponent = new CliComponent(model, componentProperty);
                                cliComponent.addListener(this);
                                return cliComponent;
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        })
                        .collect(Collectors.toMap());
    }

    @Override
    public List<String> getDescription(){

        List<String> result = new ArrayList<>();
        List<String> rowDescription = new ArrayList<>();

        int yIndex = upLeft.y;
        for (List<CliComponent> row : componentMap) {
            rowDescription.clear();
            rowDescription.addAll(List.of("",String.valueOf(yIndex),""));
            for (CliComponent cliComponent : row) {
                DescriptionUtils.sideBySide(rowDescription, cliComponent.getDescription());
            }
            result.addAll(rowDescription);
            yIndex ++;
        }

        int xIndex = upLeft.x;
        StringBuilder xIndexes = new StringBuilder(" ");
        for (int i = 0; i < componentMap.getFirst().size(); i++) {
            xIndexes.append("   ").append(xIndex).append("  ");
            xIndex++;
        }
        result.add(xIndexes.toString());

        DescriptionUtils.borderAndTitle(
                result,
                color.getDescription()+" "+model.getPlayerToColor().inverse().get(color)
        );
        return result;
    }

    private Point getUpLeft(Set<Point> points) {
        int xMin = points.stream().mapToInt(point -> point.x).min().orElse(0);
        int yMin = points.stream().mapToInt(point -> point.y).min().orElse(0);
        return new Point(xMin, yMin);
    }
}