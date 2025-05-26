//package it.polimi.ingsw.galaxytruckers.view.screens;
//
//import it.polimi.ingsw.galaxytruckers.network.shared.VirtualServer;
//import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
//import javafx.scene.layout.Pane;
//
//import java.io.IOException;
//
//public class PlanetScreen implements ScreenFactory{
//
//    @Override
//    public void showCLI(ClientModel model) {
//        System.out.println("Please write the number of the planet you wish to land on, then press enter.");
//        System.out.println("For example, to land on the second planet type: 2");
//        model.getPlanets().getDescription().forEach(System.out::println);
//
//        if (model.isMyTurn()) {
//            System.out.println("Please write the number of the planet you wish to land on, then press enter.");
//            System.out.println("For example, to land on the second planet type: 2");
//        }
//        else {
//            System.out.println(model.getCurrentPlayerNickname() + " is choosing");
//        }
//    }
//
//    @Override
//    public boolean isFormatLegal(ClientModel model, String input) {
//        return false; // TODO
//    }
//
//    @Override
//    public void parseAndInvoke(ClientModel model, String input, VirtualServer server) throws IOException {
//        // TODO
//    }
//
//    @Override
//    public void showGUI(ClientModel model, Pane root, VirtualServer server) {
//
//    }
//}
