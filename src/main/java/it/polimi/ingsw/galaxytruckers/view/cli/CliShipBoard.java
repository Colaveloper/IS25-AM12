package it.polimi.ingsw.galaxytruckers.view.cli;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.Colors;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.Component;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CliShipBoard extends CliElement {

    private final List<List<CliComponent>> componentMatrix; // ALL FINAL
    Point upLeft;

    public CliShipBoard(ClientModel model, Colors color) throws IOException {
        super(model);

        upLeft = model.getUpLeft();
        componentMatrix = model.getShipsProperty().get(color).stream()
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

        int height = componentMatrix.size();
        int width = componentMatrix.getFirst().size();
        int componentHeight = 3; // TODO: remove magic number
        int componentWidth = 5; // TODO: remove magic number

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
                    row.append(componentMatrix.get(i).get(k).getDescription().get(j));
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
}