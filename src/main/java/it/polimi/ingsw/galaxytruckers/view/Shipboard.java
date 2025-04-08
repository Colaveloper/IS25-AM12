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
        componentMap = new Component[7][5];
        //for (int i = 0; i < 6; i++) {
        //    for (int j = 0; j < 4; j++) {

//            }
 //       }
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
        for (int matRow = 0; matRow < 5; matRow++) {                    //all components by rows from matrix
            // single component rows
            description.append("\t\t");
            for (int matColumn = 0; matColumn < 7; matColumn++) {   //all component columns from matrix
                //TODO: if(component != vuoto) else print space
                //TODO: get connector in print
                description.append("╭─" + componentMap[matColumn][matRow].getConnector(0) + "─╮");                //single comp columns
            }

            description.append("\n\t\t");
            for (int matColumn = 0; matColumn < 7; matColumn++) {   //all component columns from matrix
                //TODO: if(component != vuoto) else print space
                //TODO: get component type in print (batteries could print quantity too)
                description.append(componentMap[matColumn][matRow].getConnector(3) +
                        componentMap[matColumn][matRow].getSymbol() +
                        componentMap[matColumn][matRow].getConnector(1)
                );                //single comp columns
            }
            System.out.println();

            description.append("\n\t\t");
            for (int matColumn = 0; matColumn < 7; matColumn++) {   //all component columns from matrix
                //TODO: if(component != vuoto) else print space
                //TODO: get connector in print
                description.append("╰─" + componentMap[matColumn][matRow].getConnector(2) + "─╯");                //single comp columns
            }
            description.append("\n");
        }
        description.append("\n");
        return description.toString();
    }

}
