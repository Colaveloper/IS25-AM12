package it.polimi.ingsw.galaxytruckers.view.screens;

import it.polimi.ingsw.galaxytruckers.network.shared.VirtualServer;
import it.polimi.ingsw.galaxytruckers.view.cli.CliComponentBank;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import javafx.scene.layout.Pane;

import java.awt.*;
import java.io.IOException;

public class ShipBuildingScreen implements ScreenStrategy {
    CliComponentBank componentBank;

    public ShipBuildingScreen(ClientModel model) {
        componentBank = new CliComponentBank(model);
    }

    @Override
    public void showCLI(ClientModel model) {

        componentBank.getDescription().forEach(System.out::println);
        model.getFlightBoard().getDescription().forEach(System.out::println);
        model.getComponentBank().getDescription().forEach(System.out::println);
        model.getAllShips().getDescription().forEach(System.out::println);

        // always allowed
        System.out.println("C       \tGet New covered component");
        System.out.println("U [i]   \tGet i-th uncovered component");
        System.out.println("S [i]   \tGet i-th stashed component");         // NOT IN Levels.TEST
        System.out.println("F [i]   \tGet i-th forecast deck");             // NOT IN Levels.TEST

        // allowed if existsUnwelded
        if (model.existsUnwelded()) {
            System.out.println("R       \tReject current component");
            System.out.println("P [x] [y] \tPlace current component in position x, y");
            System.out.println("S       \tStash current component");        // NOT IN Levels.TEST
            System.out.println("L       \tRotate current component left");
        }

        // allowed if hourglassTime==0 and flipsLeft>1
        // (or flipsLeft==1 and building is over, see FinishBuildingScreen)
        System.out.println("H       \tFlip hourglass");                     // NOT IN Levels.TEST
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
        root.getChildren().add(model.getComponentBank().getNode(server));
    }
}
