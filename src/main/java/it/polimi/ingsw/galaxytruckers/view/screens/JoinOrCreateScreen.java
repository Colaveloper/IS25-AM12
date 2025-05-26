//package it.polimi.ingsw.galaxytruckers.view.screens;
//
//import it.polimi.ingsw.galaxytruckers.network.client.ClientController;
//import it.polimi.ingsw.galaxytruckers.view.cliScreens.CliJoinOrCreateScreen;
//import it.polimi.ingsw.galaxytruckers.view.cliScreens.CliScreen;
//import it.polimi.ingsw.galaxytruckers.view.guiScreens.GuiJoinOrCreateScreen;
//import it.polimi.ingsw.galaxytruckers.view.guiScreens.GuiNicknameChoiceScreen;
//import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
//
//import java.io.IOException;
//
//public class JoinOrCreateScreen extends ScreenFactory {
//    @Override
//    public GuiScreen getGuiScreen(ClientModel model, ClientController controller) {
//        return new GuiJoinOrCreateScreen(model, controller);
//    }
//
//    @Override
//    public CliScreen getCliScreen(ClientModel model, ClientController controller) throws IOException {
//        return new CliJoinOrCreateScreen(model, controller);
//    }
//}
