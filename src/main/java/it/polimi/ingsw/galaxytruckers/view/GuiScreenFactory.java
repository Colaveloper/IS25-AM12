package it.polimi.ingsw.galaxytruckers.view;

import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.guiScreens.*;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.MetaState;
import it.polimi.ingsw.galaxytruckers.view.model.state.*;
import javafx.scene.Parent;
import javafx.scene.layout.HBox;

public class GuiScreenFactory implements ScreenFactory<GuiScreen> {

    @Override
    public GuiScreen createScreen(MetaState metaState, ClientModel model, ControllerToServer controller) {
        return switch (metaState) {
            case REGISTER -> new GuiNicknameChoiceScreen(model, controller);
            case JOINORCREATE -> new GuiJoinOrCreateScreen(model, model.getActiveLobbies().keySet(), controller);
            case CREATION -> new GuiGameCreationScreen(model, controller);
            case INLOBBY -> new GuiLobbyScreen(model, controller);
            case INGAME -> createScreen(model.getGame().getCurrentState(), model, controller);
        };
    }

    @Override
    public GuiScreen createScreen(GameState gameState, ClientModel model, ControllerToServer controller) {
        return switch (gameState) {
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
}
