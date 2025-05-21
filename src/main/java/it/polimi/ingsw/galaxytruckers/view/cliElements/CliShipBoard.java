package it.polimi.ingsw.galaxytruckers.view.cliElements;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.FourColors;
import it.polimi.ingsw.galaxytruckers.view.DescriptionUtils;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CliShipBoard extends CliElement {

    private final List<List<CliComponent>> componentMatrix; // ALL FINAL
    Point upLeft;
    private final FourColors color;

    public CliShipBoard(ClientModel model, FourColors color) throws IOException {
        super(model);
        this.color = color;
        upLeft = model.getUpLeft();
        componentMatrix = model.getShips().get(color).stream()
                .map(innerList -> innerList.stream()
                        .map(componentProperty -> {
                            try {
                                CliComponent cliComponent = new CliComponent(model, componentProperty);
                                cliComponent.addListener(this);
                                return cliComponent;
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        })
                        .collect(Collectors.toList()))
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getNewDescription() throws IOException {

        List<String> result = new ArrayList<>();
        List<String> rowDescription = new ArrayList<>();

        int yIndex = upLeft.y;
        for (List<CliComponent> row : componentMatrix) {
            rowDescription.clear();
            rowDescription.addAll(List.of("",String.valueOf(yIndex),""));
            for (CliComponent cliComponent : row) {
                DescriptionUtils.sideBySide(rowDescription, cliComponent.getDescription());
            }
            result.addAll(rowDescription);
            yIndex ++;
        }

        int xIndex = upLeft.x;
        StringBuilder xIndexes = new StringBuilder();
        for (int i = 0; i < componentMatrix.getFirst().size(); i++) {
            xIndexes.append("  ").append(xIndex).append("  ");
            xIndex++;
        }
        result.add(xIndexes.toString());

        DescriptionUtils.borderAndTitle(
                result,
                color.getDescription()+" "+model.getPlayerToColor().inverse().get(color)
        );
        return result;
    }
}