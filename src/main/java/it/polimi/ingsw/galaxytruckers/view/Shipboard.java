package it.polimi.ingsw.galaxytruckers.view;

import javafx.scene.image.Image;

import java.awt.*;
import java.util.Map;

public class Shipboard implements Physical{

    private Component[][] componentMap;
    private Point lastPosition;
    private int credits;
    private int losses;
    private int firepower;
    private int numBatteries;
    private int crewSize;
    private int rows = 5;
    private int columns = 7;


    public Shipboard() {
        componentMap = new Component[columns][rows];
        Component emptyComponent = new Component(ComponentType.NONE);
        for (int i = 0; i < columns; i++) {       //ship columns
            for (int j = 0; j < rows; j++) {   //ship rows
                componentMap[i][j] = emptyComponent;
            }
        }
    }

    public void setComponentMap(Map<Point, Component> componentMap) {
        for (Point p : componentMap.keySet()) {
            this.componentMap[p.x][p.y] = componentMap.get(p);
        }
    }

    public Image getImage() {
        return null;
    }
    public String getDescription() {
        StringBuilder description = new StringBuilder();
        for (int matRow = 0; matRow < rows; matRow++) {                    //all components by rows from matrix
            // single component rows
            description.append("\t\t");
            for (int matColumn = 0; matColumn < columns; matColumn++) {   //all component columns from matrix
                if (componentMap[matColumn][matRow].getSymbol() == "empty") {
                    description.append("     ");
                    continue;
                }
                description.append("╭─" + componentMap[matColumn][matRow].getConnector(0) + "─╮");                //single comp columns
            }

            description.append("\n\t" + matRow + "\t");
            for (int matColumn = 0; matColumn < columns; matColumn++) {   //all component columns from matrix
                if (componentMap[matColumn][matRow].getSymbol() == "empty") {
                    description.append("     ");
                    continue;
                }
                description.append(componentMap[matColumn][matRow].getConnector(3) +
                        componentMap[matColumn][matRow].getSymbol() +
                        componentMap[matColumn][matRow].getConnector(1)
                );                //single comp columns
            }
            System.out.println();

            description.append("\n\t\t");
            for (int matColumn = 0; matColumn < columns; matColumn++) {   //all component columns from matrix
                if (componentMap[matColumn][matRow].getSymbol() == "empty") {
                    description.append("     ");
                    continue;
                }
                description.append("╰─" + componentMap[matColumn][matRow].getConnector(2) + "─╯");                //single comp columns
            }
            description.append("\n");
        }
        description.append("\n");
        description.append("\t\t  0    1    2    3    4    5    6");
        return description.toString();
    }

}
