package it.polimi.ingsw.galaxytruckers.server.model.adventureCards;

import it.polimi.ingsw.galaxytruckers.server.model.*;
import it.polimi.ingsw.galaxytruckers.server.model.adventureCards.check.CombatZoneCheck;
import it.polimi.ingsw.galaxytruckers.server.model.adventureCards.check.CrewSizeCheck;
import it.polimi.ingsw.galaxytruckers.server.model.adventureCards.check.EnginePowerCheck;
import it.polimi.ingsw.galaxytruckers.server.model.adventureCards.check.FirePowerCheck;
import it.polimi.ingsw.galaxytruckers.server.model.adventureCards.penalty.*;
import it.polimi.ingsw.galaxytruckers.server.model.adventureCards.projectiles.BigFire;
import it.polimi.ingsw.galaxytruckers.server.model.adventureCards.projectiles.Projectile;
import it.polimi.ingsw.galaxytruckers.server.model.state.*;
import it.polimi.ingsw.galaxytruckers.shared.enums.GameColor;
import it.polimi.ingsw.galaxytruckers.shared.enums.Level;
import it.polimi.ingsw.galaxytruckers.server.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.shared.enums.Direction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CombatZoneCardTest {
    Game game;
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
        projectiles = List.of(new BigFire(Direction.UP), new BigFire(Direction.UP));
        penalties = List.of(new FlightDaysLoss(1),
                new CrewLoss(1),
                new GoodsLoss(1),
                new ProjectileThreat(new ArrayList<>(projectiles)));
        ShipBoard shipBoard1 = new SecondShipBoardForTesting(GameColor.BLUE) {
            @Override
            protected boolean containsPoint(Point point) {
                return true;
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
        ShipBoard shipBoard2 = new SecondShipBoardForTesting(GameColor.RED) {
            @Override
            protected boolean containsPoint(Point point) {
                return true;
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
        ShipBoard shipBoard3 = new SecondShipBoardForTesting(GameColor.GREEN) {
            @Override
            protected boolean containsPoint(Point point) {
                return true;
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
        FlightBoard flightBoard = new SecondFlightBoardForTesting(3);
        flightBoard.placeShipOnFlightBoard(shipBoard1,1);
        flightBoard.placeShipOnFlightBoard(shipBoard2,3);
        flightBoard.placeShipOnFlightBoard(shipBoard3,6);

        game = new GameStub(Level.SECOND) {
            @Override public FlightBoard getFlightBoard() {
                return flightBoard;
            }
        };

        card = new CombatZoneCard(game, Level.SECOND, checks, penalties, 1);
        card.initialize();
    }

    @Test
    void getNextStateWithOnePlayerLeftReturnsDrawCard() {
        game.getFlightBoard().removeShips(new HashSet<>(shipBoards.subList(0, shipBoards.size()-1)));
        assertInstanceOf(DrawCardState.class, card.getNextState());
    }

    @Test
    void checkWithNoAvailableActionImmediatelyGivesPenalty() {
        GameState testState = card.getNextState();
        assertInstanceOf(DeclareEnginePowerState.class, testState);
    }

    @Test
    void checkWithAvailableActionReturnsActionForEachPlayer() {
        for (int i = 0; i < shipBoards.size(); i++) {
            GameState testState = card.getNextState();
            assertInstanceOf(DeclareEnginePowerState.class, testState);
        }
    }

    @Test
    void penaltyWithRelatedActionReturnsAction() {
        for (int i = 0; i < shipBoards.size(); i++) {
            card.getNextState();
        }
        GameState testState = card.getNextState();
        assertInstanceOf(RemoveCrewState.class, testState);
    }

    @Test
    void penaltyWithNoMoreActionsSkipsToNextCheck() {
        for (int i = 0; i < shipBoards.size(); i++) {
            card.getNextState();
        }
        card.getNextState();
        GameState testState = card.getNextState();
        assertInstanceOf(DeclareFirePowerState.class, testState);
    }

    @Test
    void penaltyWithMoreThanOneActionReturnsEveryAction() {
        GameState testState = null;
        for (int times = 0; times < 2; times++) {
            for (int i = 0; i < shipBoards.size(); i++) {
                card.getNextState();
            }
            card.getNextState();
        }
        for (int i = 0; i < projectiles.size(); i++) {
            testState = card.getNextState();
            assertInstanceOf(HandleProjectileState.class, testState);
        }
    }

    @Test
    void getNextStateWhenCardIsOverReturnsDraw() {
        for (int times = 0; times < 2; times++) {
            for (int i = 0; i < shipBoards.size(); i++) {
                card.getNextState();
            }
            card.getNextState();
        }
        for (int i = 0; i < projectiles.size(); i++) {
            card.getNextState();
        }
        assertInstanceOf(DrawCardState.class, card.getNextState());
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