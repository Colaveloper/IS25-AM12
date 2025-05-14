package it.polimi.ingsw.galaxytruckers.view.screens;

import it.polimi.ingsw.galaxytruckers.network.shared.VirtualServer;
import it.polimi.ingsw.galaxytruckers.view.CliFlightBoard;
import it.polimi.ingsw.galaxytruckers.view.cli.CliAllShips;
import it.polimi.ingsw.galaxytruckers.view.cli.CliComponentBank;
import it.polimi.ingsw.galaxytruckers.view.cli.CliElement;
import it.polimi.ingsw.galaxytruckers.view.gui.GuiComponentBank;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.scene.layout.Pane;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ShipBuildingScreen extends ScreenStrategy {
//    CliComponentBank componentBank;
//    CliFlightBoard flightBoard;
    CliAllShips allShips;

    public ShipBuildingScreen(ClientModel model) throws IOException {
        super(model);

//        componentBank = new CliComponentBank(model);
//        componentBank.addListener(this);
//
//        flightBoard = new CliFlightBoard(model);
//        flightBoard.addListener(this);

        allShips = new CliAllShips(model);
        allShips.addListener(this);
    }

    @Override
    public List<String> getNewDescription() throws IOException {
        List<String> output = new ArrayList<>();

//        output.addAll(componentBank.getDescription());
//        output.addAll(flightBoard.getDescription());
        output.addAll(allShips.getDescription());

        output.add("C       \tGet New covered component");
        output.add("U [i]   \tGet i-th uncovered component");
        output.add("S [i]   \tGet i-th stashed component");         // NOT IN Levels.TEST
        output.add("F [i]   \tGet i-th forecast deck");             // NOT IN Levels.TEST

        if (model.existsUnwelded()) {
            output.add("R       \tReject current component");
            output.add("P [x] [y] \tPlace current component in position x, y");
            output.add("S       \tStash current component");        // NOT IN Levels.TEST
            output.add("L       \tRotate current component left");
        }

        output.add("H       \tFlip hourglass");                     // NOT IN Levels.TEST

        return output;
    }

    @Override
    public boolean isLegalInput(ClientModel model, String input) {
        return input.matches("^(C|U \\d+|S(?: \\d+)?|F \\d+|R|P \\d+ \\d+|L|H)$");
    }

    @Override
    public void parseAndInvoke(ClientModel model, String input, VirtualServer server) throws IOException {
        String[] parts = input.split("\\s+");
        switch (parts[0]) {
            case "C":
                server.requestRandComponent();
                break;

            case "U":
                if (parts.length == 2) {
                    int index = Integer.parseInt(parts[1]);
                    server.requestComponent(index);
                }
                break;

            case "S":
                if (parts.length == 1) {
                    server.stashComponent();
                } else if (parts.length == 2) {
                    int index = Integer.parseInt(parts[1]);
                    server.grabStashedComponent(index);
                }
                break;

            case "F":
                if (parts.length == 2) {
                    int index = Integer.parseInt(parts[1]);
                    server.acquireForecast(index);
                }
                break;

            case "R":
                server.rejectComponent();
                break;

            case "P":
                if (parts.length == 3) {
                    int x = Integer.parseInt(parts[1]);
                    int y = Integer.parseInt(parts[2]);
                    server.placeComponent(new Point(x, y));
                }
                break;

            case "L":
                model.rotateCurrentComponentLeft();
                break;

            case "H":
                server.flipHourglass();
                break;

            default:
                // should be impossible
                System.out.println("Invalid command.");
                break;
        }
    }

    @Override
    public void showGUI(ClientModel model, Pane root, VirtualServer server) throws IOException {
        root.getChildren().add(new GuiComponentBank(model, server).getNode());
    }
}
