package it.polimi.ingsw.galaxytruckers.view;

import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.List;

public class Component implements Physical{
    private final String symbol;
    private List<Integer> connectors = new ArrayList<>();   //TODO: enum for connector type?

    public Component(List<Integer> connectors, String symbol) {   //TODO:passare i simboli già ruotati dal json
        this.connectors = connectors;
        this.symbol = symbol;                                  //TODO: placeholder
    }

    public String getSymbol() {
        return symbol;
    }

    public String getConnector(int connectorIndex) {
        switch (connectorIndex) {               //switch direction once
            case 0:     //up
                switch (connectors.get(0)) {    //switch type for each direction
                    case 0:
                        return "─";
                    case 1:
                        return "┴";
                    case 2:
                        return "╨";
                    case 3:
                        return "╩";
                }
            case 1:
                switch (connectors.get(1)) {
                    case 0:
                        return "│";
                    case 1:
                        return "├";
                    case 2:
                        return "╞";
                    case 3:
                        return "╠";
                }
            case 2:
                switch (connectors.get(2)) {
                    case 0:
                        return "─";
                    case 1:
                        return "┬";
                    case 2:
                        return "╥";
                    case 3:
                        return "╦";
                }
            case 3:
                switch (connectors.get(3)) {
                    case 0:
                        return "│";
                    case 1:
                        return "┤";
                    case 2:
                        return "╡";
                    case 3:
                        return "╣";
                }
            default:
                return "error";
        }
    }

    @Override
    public Image getImage() {
        return null;
    }

    @Override
    public String getDescription() {
        return "";
    }
}
