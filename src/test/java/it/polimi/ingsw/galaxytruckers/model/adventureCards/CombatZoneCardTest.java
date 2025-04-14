package it.polimi.ingsw.galaxytruckers.model.adventureCards;

import it.polimi.ingsw.galaxytruckers.model.FlightBoard;
import it.polimi.ingsw.galaxytruckers.model.SecondFlightBoard;
import it.polimi.ingsw.galaxytruckers.model.adventureCards.check.CombatZoneCheck;
import it.polimi.ingsw.galaxytruckers.model.adventureCards.check.CrewSizeCheck;
import it.polimi.ingsw.galaxytruckers.model.adventureCards.check.EnginePowerCheck;
import it.polimi.ingsw.galaxytruckers.model.adventureCards.check.FirePowerCheck;
import it.polimi.ingsw.galaxytruckers.model.adventureCards.penalty.*;
import it.polimi.ingsw.galaxytruckers.model.adventureCards.projectiles.BigFire;
import it.polimi.ingsw.galaxytruckers.model.adventureCards.projectiles.Projectile;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Colors;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.model.state.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CombatZoneCardTest {
    CombatZoneCard card;
    List<ShipBoard> shipBoards;
    List<Projectile> projectiles;
    List<CombatZoneCheck> checks;
    List<Penalty> penalties;

    @BeforeEach
    void setUp() {
        checks = List.of(CrewSizeCheck.getInstance(),
                EnginePowerCheck.getInstance(),
                FirePowerCheck.getInstance(),
                CrewSizeCheck.getInstance());
        projectiles = List.of(new BigFire(0), new BigFire(0));
        penalties = List.of(new FlightDaysLoss(1),
                new CrewLoss(1),
                new GoodsLoss(1),
                new ProjectileThreat(new ArrayList<>(projectiles)));
        ShipBoard shipBoard1 = new ShipBoard(Colors.BLUE) {
            @Override
            protected boolean containsPoint(Point point) {
                return false;
            }

            @Override
            public int getCrewSize() {
                return 1;
            }

            @Override
            public int getFirePower() {
                return 1;
            }

            @Override
            public int getEnginePower() {
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

            @Override
            public int getFirePower() {
                return 2;
            }

            @Override
            public int getEnginePower() {
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

            @Override
            public int getFirePower() {
                return 3;
            }

            @Override
            public int getEnginePower() {
                return 3;
            }
        };
        shipBoards = List.of(shipBoard1, shipBoard2, shipBoard3);
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
        assertInstanceOf(DeclareEnginePowerState.class, testState);
    }

    @Test
    void checkWithAvailableActionReturnsActionForEachPlayer() {
        for (int i = 0; i < shipBoards.size(); i++) {
            GameState testState = card.nextStep();
            assertInstanceOf(DeclareEnginePowerState.class, testState);
        }
    }

    @Test
    void penaltyWithRelatedActionReturnsAction() {
        for (int i = 0; i < shipBoards.size(); i++) {
            card.nextStep();
        }
        GameState testState = card.nextStep();
        assertInstanceOf(RemoveCrewState.class, testState);
    }

    @Test
    void penaltyWithNoMoreActionsSkipsToNextCheck() {
        for (int i = 0; i < shipBoards.size(); i++) {
            card.nextStep();
        }
        card.nextStep();
        GameState testState = card.nextStep();
        assertInstanceOf(DeclareFirePowerState.class, testState);
    }

    @Test
    void penaltyWithMoreThanOneActionReturnsEveryAction() {
        GameState testState = null;
        for (int times = 0; times < 2; times++) {
            for (int i = 0; i < shipBoards.size(); i++) {
                card.nextStep();
            }
            card.nextStep();
        }
        for (int i = 0; i < projectiles.size(); i++) {
            testState = card.nextStep();
            assertInstanceOf(HandleProjectileState.class, testState);
        }
    }

    @Test
    void nextStepWhenCardIsOverReturnsDraw() {
        for (int times = 0; times < 2; times++) {
            for (int i = 0; i < shipBoards.size(); i++) {
                card.nextStep();
            }
            card.nextStep();
        }
        for (int i = 0; i < projectiles.size(); i++) {
            card.nextStep();
        }
        assertInstanceOf(DrawCardState.class, card.nextStep());
    }

    @Test
    void checksAreCorrectlyAssigned() {
        assertEquals(checks, card.getChecks());
    }

    @Test
    void penaltiesAreCorrectlyAssigned() {
        assertEquals(penalties, card.getPenalties());
    }
}