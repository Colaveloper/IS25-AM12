package it.polimi.ingsw.galaxytruckers.view.cliScreens;

import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.guiScreens.GuiScreen;
//import it.polimi.ingsw.galaxytruckers.view.guiScreens.GuiShipBuildingScreen;
import it.polimi.ingsw.galaxytruckers.view.guiScreens.GuiShipPieceChoiceScreen;
import it.polimi.ingsw.galaxytruckers.view.guiScreens.GuiValidationScreen;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.state.*;

public class ScreenFactory {

    public CliScreen createCliScreen(ClientModel model, GameState state, ControllerToServer controller) {
        switch (state) {
            case AdventureState adventureState -> {
                switch (adventureState) {
                    case ActivateState activateState -> {
                        switch (activateState) {
                            case DeclareEnginePowerState declareEnginePowerState -> {
                                return new CliPointSelectionScreen(model, controller, state);
                            }
                            case DeclareFirePowerState declareFirePowerState -> {
                                return new CliPointSelectionScreen(model, controller, state);
                            }
                            case HandleProjectileState handleProjectileState -> {
                                return new CliProjectilesScreen(model, controller, state);
                            }
                        }

                    }
                    case AddGoodsState addGoodsState -> {
                        return new CliGoodsScreen(model, controller, state);
                    }
                    case ChoosePlanetState choosePlanetState -> {
                        return new CliPlanetScreen(model, controller, state);
                    }
                    case ChooseShipPieceState chooseShipPieceState -> {
                        return new CliShipPieceChoiceScreen(model, controller, state);
                    }
                    case DrawCardState drawCardState -> {
                        return new CliNewCardScreen(model, controller, state);
                    }
                    case GrabRewardState grabRewardState -> {
                        return new CliRewardScreen(model, controller, state);
                    }
                    case RemoveCrewState removeCrewState -> {
                        return new CliRemoveCrewScreen(model, controller, state);
                    }
                    case RemoveGoodsState removeGoodsState -> {
                        return new CliGoodsScreen(model, controller, state);
                    }
                }
            }
            case ShipBuildingState shipBuildingState -> {
                switch (shipBuildingState) {
                    case SecondShipBuildingState secondShipBuildingState -> {
                        return new CliShipBuildingScreen(model, controller, state);
                    }
                    case TestShipBuildingState testShipBuildingState -> {
                        return new CliShipBuildingScreen(model, controller, state);
                    }
                }
            }
            case ShipCorrectionState shipCorrectionState -> {
                return new CliValidationScreen(model, controller, state);
            }
            case ShipInitializationState shipInitializationState -> {
                return new CliCrewInitializationScreen(model, controller, state);
            }
        }
    }

    public GuiScreen createGuiScreen(ClientModel model, GameState state, ControllerToServer controller) {

        return null;
//        switch(state){
//            case AdventureState adventureState -> {
//                switch (adventureState){
//                    case ActivateState activateState -> {
//                        return new GuiPointSelectionScreen(model, controller, state);
//                    }
//                    case AddGoodsState addGoodsState -> {
//                        return new GuiGoodsScreen(model, controller, state);
//                    }
//                    case ChoosePlanetState choosePlanetState -> {
//                        return new GuiPlanetScreen(model, controller, state);
//                    }
//                    case ChooseShipPieceState chooseShipPieceState -> {
//                        return new GuiShipPieceChoiceScreen(model, controller, state);
//                    }
//                    case DrawCardState drawCardState -> {
//                        return new GuiNewCardScreen(model, controller, state);
//                    }
//                    case GrabRewardState grabRewardState -> {
//                        return new GuiRewardScreen(model, controller, state);
//                    }
//                    case RemoveCrewState removeCrewState -> {
//                        return new GuiRemoveCrewScreen(model, controller, state);
//                    }
//                    case RemoveGoodsState removeGoodsState -> {
//                        return new GuiGoodsScreen(model, controller, state);
//                    }
//                }
//            }
//            case ShipBuildingState shipBuildingState -> {
//                switch (shipBuildingState){
//                    case SecondShipBuildingState secondShipBuildingState -> {
//                        return new GuiShipBuildingScreen(model, controller, state);
//                    }
//                    case TestShipBuildingState testShipBuildingState -> {
//                        return new GuiShipBuildingScreen(model, controller, state);
//                    }
//                }
//            }
//            case ShipCorrectionState shipCorrectionState -> {
//                return new GuiValidationScreen(model, controller, state);
//            }
//            case ShipInitializationState shipInitializationState -> {
//                return new GuiCrewInitialization(model, controller, state);
//            }
//        }
    }
}

