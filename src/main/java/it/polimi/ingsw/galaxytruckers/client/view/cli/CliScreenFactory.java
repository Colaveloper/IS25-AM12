package it.polimi.ingsw.galaxytruckers.client.view.cli;

import it.polimi.ingsw.galaxytruckers.client.controller.ClientControllerInterface;
import it.polimi.ingsw.galaxytruckers.client.view.ScreenFactory;
import it.polimi.ingsw.galaxytruckers.client.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.client.model.MetaState;
import it.polimi.ingsw.galaxytruckers.client.model.state.*;
import it.polimi.ingsw.galaxytruckers.client.view.cli.cliScreens.*;

/**
 * Factory class for creating CLI screens based on the current game state.
 * Implements the ScreenFactory interface for CLI screens.
 */
public class CliScreenFactory implements ScreenFactory<CliScreen> {

    @Override
    public CliScreen createScreen(MetaState metaState, ClientModel model, ClientControllerInterface controller) {
        return switch (metaState) {
            case REGISTER -> new CliNicknameChoiceScreen(model, controller);
            case JOINORCREATE -> new CliJoinOrCreateScreen(model, controller);
            case CREATION -> new CliGameCreationScreen(model, controller);
            case INLOBBY -> new CliLobbyScreen(model, controller);
            case INGAME -> createScreen(model.getGame().getCurrentState(), model, controller);
            case ENDGAME -> new CliEndGameScreen(model, controller);
        };
    }

    @Override
    public CliScreen createScreen(GameState gameState, ClientModel model, ClientControllerInterface controller) {
        return switch (gameState) {
            case ShipBuildingState shipBuildingState -> switch (shipBuildingState) {
                case SecondShipBuildingState secondShipBuildingState -> new CliSecondShipBuildingScreen(model, controller, secondShipBuildingState);
                case TestShipBuildingState testShipBuildingState -> new CliTestShipBuildingScreen(model, controller, testShipBuildingState);
            };
            case ShipCorrectionState shipCorrectionState -> new CliValidationScreen(model, controller, shipCorrectionState);
            case ShipInitializationState shipInitializationState -> new CliCrewInitializationScreen(model, controller, shipInitializationState);
            case AdventureState s -> switch (s) {
                case ActivateState activateState -> switch (activateState) {
                    case DeclareEnginePowerState declareEnginePowerState -> new CliDeclareEnginePowerScreen(model, controller, declareEnginePowerState);
                    case DeclareFirePowerState declareFirePowerState -> new CliDeclareFirePowerScreen(model, controller, declareFirePowerState);
                    case HandleProjectileState handleProjectileState -> new CliProjectilesScreen(model, controller, handleProjectileState);
                };
                case AddGoodsState addGoodsState -> new CliGoodsScreen(model, controller, addGoodsState);
                case ChoosePlanetState choosePlanetState -> new CliPlanetScreen(model, controller, choosePlanetState);
                case ChooseShipPieceState chooseShipPieceState -> new CliShipPieceChoiceScreen(model, controller, chooseShipPieceState);
                case DrawCardState drawCardState -> new CliNewCardScreen(model, controller, drawCardState);
                case GrabRewardState grabRewardState -> new CliRewardScreen(model, controller, grabRewardState);
                case RemoveCrewState removeCrewState -> new CliRemoveCrewScreen(model, controller, removeCrewState);
                case RemoveGoodsState removeGoodsState -> new CliLoseGoodsScreen(model, controller, removeGoodsState);
                default -> throw new IllegalStateException("Unexpected AdventureState: " + s);
            };
            default -> throw new IllegalStateException("Unexpected GameState: " + gameState);
        };
    }
}
