package it.polimi.ingsw.galaxytruckers.view.cliScreens;

import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.cliElements.CliAllShips;
import it.polimi.ingsw.galaxytruckers.view.cliElements.CliAllShipsHandAndStash;
import it.polimi.ingsw.galaxytruckers.view.cliElements.CliComponents.CliComponentBank;
import it.polimi.ingsw.galaxytruckers.view.cliElements.CliForecast;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.state.SecondShipBuildingState;

public class CliSecondShipBuildingScreen extends CliScreen {
    private SecondShipBuildingState gameState;

    private final CliComponentBank cliComponentBank;
    private final CliForecast cliForecast;
    private final CliAllShipsHandAndStash cliAllShips;


    public CliSecondShipBuildingScreen(ClientModel model, ControllerToServer controller, SecondShipBuildingState gameState) {
        super(model, controller, gameState);

        cliComponentBank = new CliComponentBank(gameState.getComponentBank());
        //cliComponentBank.addObserver(this::render); // todo: delegate to controller
        cliForecast = new CliForecast(gameState.getLockedForecastsProperty());
        //cliForecast.addObserver(this::render); // todo: delegate to controller
        cliAllShips = new CliAllShipsHandAndStash(model.getShipToPlayer());
        //cliAllShips.addObserver(this::render); // todo: delegate to controller
        gameState = gameState;
    }

    @Override
    public void render() {
        cliComponentBank.getDescription().forEach(System.out::println);
        cliForecast.getDescription().forEach(System.out::println);
        cliFlightBoard.getDescription().forEach(System.out::println);
        cliAllShips.getDescription().forEach(System.out::println);
        printActions();
    }

    @Override
    public void parseAndInvoke(String input) {
        String[] parts = input.split("\\s+");
        switch (parts[0]) {
            case "C":
                controller.requestRandComponent();
                break;

            case "U":
                if (parts.length == 2) {
                    int index = Integer.parseInt(parts[1]) - 1;
                    int componentId = gameState.getComponentBank().getUncoveredComponentsProperty().get(index).getId();
                    controller.requestComponent(componentId);
                }
                break;

            case "S":
                if (parts.length == 1) {
                    controller.stashComponent();
                } else if (parts.length == 2) {
                    int index = Integer.parseInt(parts[1]);
                    controller.grabStashedComponent(index);
                }
                break;

            case "F":
                if (parts.length == 2) {
                    int index = Integer.parseInt(parts[1]);
                    controller.acquireForecast(index);
                }
                break;

            case "R":
                controller.rejectComponent();
                break;

            case "P":
                if (parts.length == 3) {
                    controller.placeComponent(getPoint(input), 0); //todo add orientation
                }
                break;

            case "L":
                //model.rotateCurrentComponentLeft();
                //todo rotate component in model
                break;

            case "H":
                controller.flipHourglass();
                break;

            case "E":
                if (parts.length == 2) {
                    int index = Integer.parseInt(parts[1]);
                    controller.placeShipOnFlightboard(index);
                }
                break;

            default:
                // should be impossible
                System.out.println("Invalid command.");
                break;
        }
    }
}
