package it.polimi.ingsw.galaxytruckers.client.view.gui;

import it.polimi.ingsw.galaxytruckers.client.controller.ClientControllerInterface;
import it.polimi.ingsw.galaxytruckers.client.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.client.model.MetaState;
import it.polimi.ingsw.galaxytruckers.client.model.state.*;
import it.polimi.ingsw.galaxytruckers.client.view.ScreenFactory;
import it.polimi.ingsw.galaxytruckers.client.view.gui.guiScreens.*;

public class GuiScreenFactory implements ScreenFactory<GuiScreen> {

    @Override
    public GuiScreen createScreen(MetaState metaState, ClientModel model, ClientControllerInterface controller) {
        return switch (metaState) {
            case REGISTER -> new GuiNicknameChoiceScreen(model, controller);
            case JOINORCREATE -> new GuiJoinOrCreateScreen(model, model.getActiveLobbies().keySet(), controller);
            case CREATION -> new GuiGameCreationScreen(model, controller);
            case INLOBBY -> new GuiLobbyScreen(model, controller);
            case INGAME -> createScreen(model.getGame().getCurrentState(), model, controller);
            case ENDGAME -> new GuiEndGameScreen(model, controller);
        };
    }

    @Override
    public GuiScreen createScreen(GameState gameState, ClientModel model, ClientControllerInterface controller) {
        return switch (gameState) {
            case ShipBuildingState shipBuildingState -> switch (shipBuildingState) {
                case SecondShipBuildingState secondShipBuildingState -> new GuiSecondShipBuildingScreen(model, controller, secondShipBuildingState);
                case TestShipBuildingState testShipBuildingState -> new GuiTestShipBuildingScreen(model, controller, testShipBuildingState);
            };
            case ShipCorrectionState shipCorrectionState -> new GuiCorrectionScreen(model, controller, shipCorrectionState);
            case ShipInitializationState shipInitializationState -> new GuiCrewInitializationScreen(model, controller, shipInitializationState);
            case AdventureState s -> switch (s) {
                case ActivateState activateState -> switch (activateState) {
                    case DeclareEnginePowerState declareEnginePowerState -> new GuiDeclareEnginePowerScreen(model, controller, declareEnginePowerState);
                    case DeclareFirePowerState declareFirePowerState -> new GuiDeclareFirePowerScreen(model, controller, declareFirePowerState);
                    case HandleProjectileState handleProjectileState -> new GuiProjectilesScreen(model, controller, handleProjectileState);
                };
                case AddGoodsState addGoodsState -> new GuiGoodsScreen(model, controller, addGoodsState);
                case ChoosePlanetState choosePlanetState -> new GuiPlanetScreen(model, controller, choosePlanetState);
                case ChooseShipPieceState chooseShipPieceState -> new GuiShipPieceChoiceScreen(model, controller, chooseShipPieceState);
                case DrawCardState drawCardState -> new GuiNewCardScreen(model, controller, drawCardState);
                case GrabRewardState grabRewardState -> new GuiRewardScreen(model, controller, grabRewardState);
                case RemoveCrewState removeCrewState -> new GuiRemoveCrewScreen(model, controller, removeCrewState);
                case RemoveGoodsState removeGoodsState -> new GuiLoseGoodsScreen(model, controller, removeGoodsState);
                default -> throw new IllegalStateException("Unexpected AdventureState: " + s);
            };
            default -> throw new IllegalStateException("Unexpected GameState: " + gameState);
        };
    }
}
