package it.polimi.ingsw.galaxytruckers.view;



import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;

import java.util.List;

public class MockView {

    private List<ShipBoard> ships;
    private ClientGameModel clientGameModel;






    public static void main(String[] args) {
        ClientGameModel game = new ClientGameModel();
//        Printer printer = new Printer();


        System.out.println(game);

//        printer.printAdventureDrawState();
    }
}
