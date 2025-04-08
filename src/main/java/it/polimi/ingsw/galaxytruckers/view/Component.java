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

    public String getSymbol() {
        switch (type) {
            case CANNON:
                return " ▲ ";
            case CABIN:
                return " ● ";
            case ENGINE:
                return " ⊓ ";
            case LIFE_SUPPORT:
                return " Ѫ ";
            case BATTERY:
                return " Θ ";
            case STORAGE:
                return " ▞ ";
            case SHIELD:
                return " S ";
            case NONE:
                return "empty";
            default:
                return "error";
        }

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
    public String getDescription() {
        return "";
    }
}
