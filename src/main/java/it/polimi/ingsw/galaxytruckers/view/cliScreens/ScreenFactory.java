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
                        case ActivateState s -> switch (s) {
                            case DeclareEnginePowerState s -> new CliPointSelectionScreen(model, controller, s);
                            case DeclareFirePowerState s -> new CliPointSelectionScreen(model, controller, s);
                            case HandleProjectileState handleProjectileState -> new CliProjectilesScreen(model, controller, s);
                        };
                        case AddGoodsState addGoodsState -> new CliGoodsScreen(model, controller, gameState);
                        case ChoosePlanetState choosePlanetState -> new CliPlanetScreen(model, controller, gameState);
                        case ChooseShipPieceState chooseShipPieceState -> new CliShipPieceChoiceScreen(model, controller, gameState);
                        case DrawCardState drawCardState -> new CliNewCardScreen(model, controller, gameState);
                        case GrabRewardState grabRewardState -> new CliRewardScreen(model, controller, gameState);
                        case RemoveCrewState removeCrewState -> new CliRemoveCrewScreen(model, controller, gameState);
                        case RemoveGoodsState removeGoodsState -> new CliGoodsScreen(model, controller, gameState);
                    };
                    case ShipBuildingState shipBuildingState -> switch (shipBuildingState) {
                        case SecondShipBuildingState secondShipBuildingState -> new CliShipBuildingScreen(model, controller, gameState);
                        case TestShipBuildingState testShipBuildingState -> new CliShipBuildingScreen(model, controller, gameState);
                    };
                    case ShipCorrectionState shipCorrectionState -> new CliValidationScreen(model, controller, gameState);
                    case ShipInitializationState shipInitializationState -> new CliCrewInitializationScreen(model, controller, gameState);
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

