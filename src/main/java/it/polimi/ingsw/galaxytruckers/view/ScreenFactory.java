package it.polimi.ingsw.galaxytruckers.view;

import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.cliScreens.*;
import it.polimi.ingsw.galaxytruckers.view.guiScreens.*;
//import it.polimi.ingsw.galaxytruckers.view.guiScreens.GuiSecondShipBuildingScreen;
//import it.polimi.ingsw.galaxytruckers.view.guiScreens.GuiSecondShipBuildingScreen;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.MetaState;
import it.polimi.ingsw.galaxytruckers.view.model.state.*;

public class ScreenFactory {

    public CliScreen createCliScreen(ClientModel model, ControllerToServer controller) {
        MetaState metaState = model.getMetaState();
        return switch (metaState) {
            case REGISTER -> new CliNicknameChoiceScreen(model, controller);
            case JOINORCREATE -> new CliJoinOrCreateScreen(model, controller);
            case CREATION -> new CliGameCreationScreen(model, controller);
            case INLOBBY -> new CliLobbyScreen(model, controller);
            case INGAME -> {
                GameState gameState = model.getGame().getCurrentState();
                yield switch (gameState) {
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
                    };
                    case ShipBuildingState shipBuildingState -> switch (shipBuildingState) {
                        case SecondShipBuildingState secondShipBuildingState -> new CliSecondShipBuildingScreen(model, controller, secondShipBuildingState);
                        case TestShipBuildingState testShipBuildingState -> new CliTestShipBuildingScreen(model, controller, testShipBuildingState);
                    };
                    case ShipCorrectionState shipCorrectionState -> new CliValidationScreen(model, controller, shipCorrectionState);
                    case ShipInitializationState shipInitializationState -> new CliCrewInitializationScreen(model, controller, shipInitializationState);
                };
            }
        };
    }

    public GuiScreen createGuiScreen(ClientModel model, ControllerToServer controller) {

        MetaState metaState = model.getMetaState();
        return switch (metaState) {
            case REGISTER -> new GuiNicknameChoiceScreen(model, controller);
            case JOINORCREATE -> new GuiJoinOrCreateScreen(model, controller);
            case CREATION -> new GuiGameCreationScreen(model, controller);
            case INLOBBY -> new GuiLobbyScreen(model, controller);
            case INGAME -> {
                GameState gameState = model.getGame().getCurrentState();
                yield switch (gameState) {
                    case ShipBuildingState shipBuildingState -> switch (shipBuildingState) {
                        case SecondShipBuildingState secondShipBuildingState -> new GuiSecondShipBuildingScreen(model, controller, secondShipBuildingState);
                        case TestShipBuildingState testShipBuildingState -> null;
//                                new GuiTestShipBuildingScreen(model, controller, testShipBuildingState);
                    };
                    case ShipCorrectionState shipCorrectionState -> null;
//                            new GuiValidationScreen(model, controller, shipCorrectionState);
                    case ShipInitializationState shipInitializationState -> null;
//                            new GuiCrewInitializationScreen(model, controller, shipInitializationState);
                    case AdventureState s -> switch (s) {
                        case ActivateState activateState -> switch (activateState) {
                            case DeclareEnginePowerState declareEnginePowerState -> null;
//                                    new GuiDeclareEnginePowerScreen(model, controller, declareEnginePowerState);
                            case DeclareFirePowerState declareFirePowerState -> null;
//                                    new GuiDeclareFirePowerScreen(model, controller, declareFirePowerState);
                            case HandleProjectileState handleProjectileState -> null;
//                                    new GuiProjectilesScreen(model, controller, handleProjectileState);
                        };
                        case AddGoodsState addGoodsState -> null;
//                                new GuiGoodsScreen(model, controller, addGoodsState);
                        case ChoosePlanetState choosePlanetState -> null;
//                                new GuiPlanetScreen(model, controller, choosePlanetState);
                        case ChooseShipPieceState chooseShipPieceState -> null;
//                                new GuiShipPieceChoiceScreen(model, controller, chooseShipPieceState);
                        case DrawCardState drawCardState -> null;
//                                new GuiNewCardScreen(model, controller, drawCardState);
                        case GrabRewardState grabRewardState -> null;
//                                new GuiRewardScreen(model, controller, grabRewardState);
                        case RemoveCrewState removeCrewState -> null;
//                                new GuiRemoveCrewScreen(model, controller, removeCrewState);
                        case RemoveGoodsState removeGoodsState -> null;
//                                new GuiLoseGoodsScreen(model, controller, removeGoodsState);
                    };
                };
            }
        };
    }
}

