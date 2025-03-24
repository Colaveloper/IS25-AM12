package it.polimi.ingsw.galaxytruckers.adventureCards;

import it.polimi.ingsw.galaxytruckers.FlightBoard;
import it.polimi.ingsw.galaxytruckers.adventureCards.utils.AdventureCard;
import it.polimi.ingsw.galaxytruckers.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.shipBuilding.Cabin;
import it.polimi.ingsw.galaxytruckers.shipBuilding.Connector;
import it.polimi.ingsw.galaxytruckers.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.state.ActivateState;
import it.polimi.ingsw.galaxytruckers.state.DrawCardState;
import it.polimi.ingsw.galaxytruckers.state.GameState;
import javafx.scene.image.Image;

import java.awt.*;
import java.util.Map;

import static java.lang.Math.abs;

public class EpidemicCard extends AdventureCard {


    public EpidemicCard(Image image, Level level, FlightBoard flightBoard) {
        super(image, level, flightBoard);
    }

    @Override
    public GameState nextStep() {
        for(ShipBoard shipBoard : flightBoard.getOrderedShips()) {

            // check currentShipboard for cabins
            Map<Point, Cabin> cabins = shipBoard.getCabins();
            for (Map.Entry<Point, Cabin> cabin : cabins.entrySet()) {
                for (int i = 0; i < 4; i++) {
                    if (cabin.getValue().getConnectors().get(i + cabin.getValue().getOrientation()) != Connector.NONE) {//TODO: does it count rotation?
                        if (cabins.containsKey(new Point(
                                cabin.getKey().x + (i % 2 * 2 - 1),
                                cabin.getKey().y + (1 - abs(i % 2 * 2 - 1))))
                        ) {
                            shipBoard.loseCrew(cabin.getKey(), 1);
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
        }

        return new DrawCardState();
    }
}
