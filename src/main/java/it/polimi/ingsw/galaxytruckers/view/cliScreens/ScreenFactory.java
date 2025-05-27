package it.polimi.ingsw.galaxytruckers.view.cliScreens;

import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.guiScreens.GuiScreen;
//import it.polimi.ingsw.galaxytruckers.view.guiScreens.GuiShipBuildingScreen;
//import it.polimi.ingsw.galaxytruckers.view.guiScreens.GuiShipBuildingScreen;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.MetaState;
import it.polimi.ingsw.galaxytruckers.view.model.state.*;

public class ScreenFactory {

    public CliScreen createCliScreen(ClientModel model, ControllerToServer controller) {
        MetaState metaState = model.getMetaState().getValue();
        return switch (metaState) {
            case REGISTER -> new CliNicknameChoiceScreen(model, controller);
            case JOINORCREATE -> new CliJoinOrCreateScreen(model, controller);
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

    public GuiScreen createGuiScreen(ClientModel model, GameState state, ControllerToServer controller) {

        switch(state){
            case AdventureState adventureState -> {
                switch (adventureState){
                    case ActivateState activateState -> {
                        return null;
//                        new GuiPointSelectionScreen(model, controller, state);
                    }
                    case AddGoodsState addGoodsState -> {
                        return null;
//                        new GuiGoodsScreen(model, controller, state);
                    }
                    case ChoosePlanetState choosePlanetState -> {
                        return null;
//                        new GuiPlanetScreen(model, controller, state);
                    }
                    case ChooseShipPieceState chooseShipPieceState -> {
                        return null;
//                        new GuiShipPieceChoiceScreen(model, controller, state);
                    }
                    case DrawCardState drawCardState -> {
                        return null;
//                        new GuiNewCardScreen(model, controller, state);
                    }
                    case GrabRewardState grabRewardState -> {
                        return null;
//                        new GuiRewardScreen(model, controller, state);
                    }
                    case RemoveCrewState removeCrewState -> {
                        return null;
//                        new GuiRemoveCrewScreen(model, controller, state);
                    }
                    case RemoveGoodsState removeGoodsState -> {
                        return null;
//                        new GuiGoodsScreen(model, controller, state);
                    }
                }
            }
            case ShipBuildingState shipBuildingState -> {
                switch (shipBuildingState){
                    case SecondShipBuildingState secondShipBuildingState -> {
                        return null;
//                        new GuiShipBuildingScreen(model, controller, state);
                    }
                    case TestShipBuildingState testShipBuildingState -> {
                        return null;
//                        new GuiShipBuildingScreen(model, controller, state);
                    }
                }
            }
            case ShipCorrectionState shipCorrectionState -> {
                return null;
//                new GuiValidationScreen(model, controller, state);
            }
            case ShipInitializationState shipInitializationState -> {
                return null;
//                new GuiCrewInitialization(model, controller, state);
            }
        }
    }
}

