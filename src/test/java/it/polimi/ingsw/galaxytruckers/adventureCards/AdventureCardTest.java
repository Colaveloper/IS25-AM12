package it.polimi.ingsw.galaxytruckers.adventureCards;

import it.polimi.ingsw.galaxytruckers.shipBuilding.*;
import it.polimi.ingsw.galaxytruckers.shipBuilding.Component;
import javafx.application.Platform;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import java.awt.*;
import java.util.Arrays;

public class AdventureCardTest {
    protected Component component;
    protected ComponentBank componentBank = new ComponentBank() {
        @Override
        public Component getRanComponent() {
            return component;
        }
    };

    private void addComponent(ShipBoard shipBoard, Point point) {
        shipBoard.requestRandComponent();
        shipBoard.placeComponent(point);
        shipBoard.weldLastComponent();
    }

    protected void buildLargeShip(ShipBoard shipBoard) {
        component = new Cannon(null, Arrays.asList(Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL));
        addComponent(shipBoard, new Point(8,6));
        addComponent(shipBoard, new Point(10,7));
        addComponent(shipBoard, new Point(6,5));

        component = new Cabin(null, Arrays.asList(Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL));
        addComponent(shipBoard, new Point(7,7));
        addComponent(shipBoard, new Point(8,7));
        addComponent(shipBoard, new Point(9,8));

        component = new Cabin(null, Arrays.asList(Connector.UNIVERSAL, Connector.NONE, Connector.UNIVERSAL, Connector.UNIVERSAL));
        addComponent(shipBoard, new Point(6,7));

        component = new Shield(null, Arrays.asList(Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL));
        addComponent(shipBoard, new Point(6,6));
        addComponent(shipBoard, new Point(10,9));
        addComponent(shipBoard, new Point(4,8));

        component = new Engine(null, Arrays.asList(Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL));
        addComponent(shipBoard, new Point(7,8));
        addComponent(shipBoard, new Point(9,9));
        addComponent(shipBoard, new Point(4,7));

        component = new CargoHold(null, Arrays.asList(Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL), 2, true);
        addComponent(shipBoard, new Point(9,7));
        addComponent(shipBoard, new Point(7,6));
        addComponent(shipBoard, new Point(5,7));
    }
}
