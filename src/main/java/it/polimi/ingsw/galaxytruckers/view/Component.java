package it.polimi.ingsw.galaxytruckers.view;

import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.List;

public class Component implements Physical{
    private final ComponentType type;
    private List<Integer> connectors = new ArrayList<>();   //TODO: enum for connector type?

    public Component(List<Integer> connectors, ComponentType type) {
        this.connectors = connectors;
        this.type = type;
    }

    public Component(ComponentType type) {
        this.type = type;
    }

    public String getConnector(int connectorDirection) {
        switch (connectorDirection) {               //switch direction once
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
    public List<String> getDescription() {
        List<String> lines = new ArrayList<>();
        if (type == ComponentType.NONE) {
            lines.add(0, "     ");
            lines.add(1, "     ");
            lines.add(2, "     ");
        } else {
            lines.add(0, "╭─" + getConnector(0) + "─╮");
            lines.add(1, getConnector(3)+" "+type.getSymbol()+" "+getConnector(1));
            lines.add(2, "╰─" + getConnector(2) + "─╯");
        }
        return lines;
    }
}
