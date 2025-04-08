package it.polimi.ingsw.galaxytruckers.model.adventureCards;

import it.polimi.ingsw.galaxytruckers.model.FlightBoard;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.Cabin;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.Connector;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.model.state.DrawCardState;
import it.polimi.ingsw.galaxytruckers.model.state.GameState;
import javafx.scene.image.Image;

import java.awt.*;
import java.util.Map;

import static java.lang.Math.abs;

public class EpidemicCard extends AdventureCard {


    public EpidemicCard(Image image, Level level) {
        super(image, level);
    }

    @Override
    public GameState nextStep() {
        for(ShipBoard shipBoard : flightBoard.getOrderedShips()) {

            // check currentShipboard for cabins
            Map<Point, Cabin> cabins = shipBoard.getCabins();
            for (Map.Entry<Point, Cabin> cabinEntry : cabins.entrySet()) {
                for (int i = 0; i < 4; i++) {
                    Point infectionOrigin = new Point(
                            cabinEntry.getKey().x + (i % 2 * 2 - 1),
                            cabinEntry.getKey().y + (1 - abs(i % 2 * 2 - 1))
                    );
                    if (cabins.containsKey(infectionOrigin) // infectionOrigin is a cabin
                            && cabinEntry.getValue().getConnectors().get(i) != Connector.NONE // they are connected
                            && shipBoard.getCabins().get(infectionOrigin).getNumResidents() > 0 // somebody infecting
                            && shipBoard.getCabins().get(cabinEntry.getKey()).getNumResidents() > 0 // somebody to infect
                        ) {
                            shipBoard.loseCrew(cabinEntry.getKey(), 1);
                            break;
                        }
                    }
                }

//             if the for above doesn't work here is the hard coded version
//
//            if (cabin.getValue().getConnectors().get(cabin.getValue().getOrientation()) != Connector.NONE) {
//                if(cabins.containsKey(new Point(cabin.getKey().x, cabin.getKey().y + 1))){
//                    shipBoard.loseCrew(cabin.getKey(), 1);
//                }
//            }
//            else if (cabin.getValue().getConnectors().get(1 + cabin.getValue().getOrientation()) != Connector.NONE) {
//                if(cabins.containsKey(new Point(cabin.getKey().x + 1, cabin.getKey().y))){
//                    shipBoard.loseCrew(cabin.getKey(), 1);
//                }
//            }
//            else if (cabin.getValue().getConnectors().get(2 + cabin.getValue().getOrientation()) != Connector.NONE) {
//                if(cabins.containsKey(new Point(cabin.getKey().x, cabin.getKey().y - 1))){
//                    shipBoard.loseCrew(cabin.getKey(), 1);
//                }
//            }
//            else if (cabin.getValue().getConnectors().get(3 + cabin.getValue().getOrientation()) != Connector.NONE) {
//                if(cabins.containsKey(new Point(cabin.getKey().x - 1, cabin.getKey().y))){
//                    shipBoard.loseCrew(cabin.getKey(), 1);
//                }
//            }
            }
        return new DrawCardState();
    }
}
