package it.polimi.ingsw.galaxytruckers.model.adventureCards;

import it.polimi.ingsw.galaxytruckers.model.FlightBoard;
import it.polimi.ingsw.galaxytruckers.model.SecondFlightBoard;
import it.polimi.ingsw.galaxytruckers.model.adventureCards.check.CombatZoneCheck;
import it.polimi.ingsw.galaxytruckers.model.adventureCards.check.CrewSizeCheck;
import it.polimi.ingsw.galaxytruckers.model.adventureCards.check.EnginePowerCheck;
import it.polimi.ingsw.galaxytruckers.model.adventureCards.check.FirePowerCheck;
import it.polimi.ingsw.galaxytruckers.model.adventureCards.penalty.*;
import it.polimi.ingsw.galaxytruckers.model.adventureCards.projectiles.BigFire;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Colors;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.model.state.GameState;
import it.polimi.ingsw.galaxytruckers.model.state.RemoveCrewState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CombatZoneCardTest {
    CombatZoneCard card;

    @BeforeEach
    void setUp() {
        List<CombatZoneCheck> checks = List.of(CrewSizeCheck.getInstance(),
                EnginePowerCheck.getInstance(),
                FirePowerCheck.getInstance(),
                CrewSizeCheck.getInstance());
        List<Penalty> penalties = List.of(new FlightDaysLoss(1),
                new CrewLoss(1),
                new GoodsLoss(1),
                new ProjectileThreat(List.of(new BigFire(0), new BigFire(0))));
        ShipBoard shipBoard1 = new ShipBoard(Colors.BLUE) {
            @Override
            protected boolean containsPoint(Point point) {
                return false;
            }

            @Override
            public int getCrewSize() {
                return 1;
            }
        };
        ShipBoard shipBoard2 = new ShipBoard(Colors.RED) {
            @Override
            protected boolean containsPoint(Point point) {
                return false;
            }

            @Override
            public int getCrewSize() {
                return 2;
            }
        };
        ShipBoard shipBoard3 = new ShipBoard(Colors.GREEN) {
            @Override
            protected boolean containsPoint(Point point) {
                return false;
            }

            @Override
            public int getCrewSize() {
                return 3;
            }
        };
        FlightBoard flightBoard = new SecondFlightBoard(Set.of(shipBoard1, shipBoard2, shipBoard3));
        flightBoard.placeShipOnFlightBoard(shipBoard1,1);
        flightBoard.placeShipOnFlightBoard(shipBoard2,3);
        flightBoard.placeShipOnFlightBoard(shipBoard3,6);

        card = new CombatZoneCard(Level.FIRST, checks, penalties);
        card.initialize(flightBoard);
    }

    @Test
    void checkWithNoAvailableActionImmediatelyGivesPenalty() {
        GameState testState = card.nextStep();
        assertInstanceOf(RemoveCrewState.class, testState);
    }
}