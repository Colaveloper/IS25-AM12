package it.polimi.ingsw.galaxytruckers.model.state;

import it.polimi.ingsw.galaxytruckers.model.Game;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.FourColors;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.SecondShipBoard;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;
class GameStateTest {
    GameState testState;
    Game game;
    ShipBoard ship1;

    @BeforeEach
    void setup(){
        game = new Game(Level.SECOND);
        ship1 = new SecondShipBoard(FourColors.BLUE);
    }

    @Test
    void setGame() {
        testState = new AdventureState();
        testState.setGame(game);
        assertEquals(game, testState.game);
    }

    @Test
    void endGameThrowsExceptionWhenUsingInvalidState() {
        testState = new AdventureState();
        assertThrows(IllegalStateException.class, () -> testState.endGame());
    }

    @Test
    void giveUpThrowsExceptionWhenUsingInvalidState() {
        testState = new GameState() {
        };
        assertThrows(IllegalStateException.class, () -> testState.giveUp(ship1));
    }

    @Nested
    class AdventureStateMethodsTest{
        @BeforeEach
        void setup(){
            testState = new AdventureState();
        }

        @Test
        void activateComponentThrowsExceptionWhenUsingInvalidState() {
            assertThrows(IllegalStateException.class, () -> testState.activateComponent(ship1, new Point(7,7)));
        }

        @Test
        void spendBatteriesThrowsExceptionWhenUsingInvalidState() {
            assertThrows(IllegalStateException.class, () -> testState.spendBatteries(ship1, new Point(7,7), 2));
        }

        @Test
        void grabRewardThrowsExceptionWhenUsingInvalidState() {
            assertThrows(IllegalStateException.class, () -> testState.grabReward(ship1));
        }

        @Test
        void chooseShipPieceThrowsExceptionWhenUsingInvalidState() {
            assertThrows(IllegalStateException.class, () -> testState.chooseShipPiece(ship1, 2));
        }

        @Test
        void drawCardThrowsExceptionWhenUsingInvalidState() {
            assertThrows(IllegalStateException.class, () -> testState.drawCard(ship1));
        }

        @Test
        void loseCrewThrowsExceptionWhenUsingInvalidState() {
            assertThrows(IllegalStateException.class, () -> testState.loseCrew(ship1, new Point(7,7)));
        }

        @Test
        void loseGoodThrowsExceptionWhenUsingInvalidState() {
            assertThrows(IllegalStateException.class, () -> testState.loseGood(ship1, new Point(7,7)));
        }

        @Test
        void addGoodThrowsExceptionWhenUsingInvalidState() {
            assertThrows(IllegalStateException.class, () -> testState.addGood(ship1, new Point(7, 7), GoodsType.RED));
        }

        @Test
        void removeGoodThrowsExceptionWhenUsingInvalidState() {
            assertThrows(IllegalStateException.class, () -> testState.removeGood(ship1, new Point(7, 7), GoodsType.BLUE));
        }

        @Test
        void goNextThrowsExceptionWhenUsingInvalidState() {
            assertThrows(IllegalStateException.class, () -> testState.goNext(ship1));
        }

        @Test
        void choosePlanetThrowsExceptionWhenUsingInvalidState() {
            assertThrows(IllegalStateException.class, () -> testState.choosePlanet(ship1, 0));
        }
    }

    @Nested
    class ShipBuildingStatesTest {

        @BeforeEach
        void setup() {
            testState = new GameState() {
            };
        }

        @Test
        void requestRandComponentThrowsExceptionWhenUsingInvalidState() {
            assertThrows(IllegalStateException.class, () -> testState.requestRandComponent(ship1));
        }

        @Test
        void requestComponentThrowsExceptionWhenUsingInvalidState() {
            assertThrows(IllegalStateException.class, () -> testState.requestComponent(ship1, 42));
        }

        @Test
        void rejectComponentThrowsExceptionWhenUsingInvalidState() {
            assertThrows(IllegalStateException.class, () -> testState.rejectComponent(ship1));
        }

        @Test
        void stashComponentThrowsExceptionWhenUsingInvalidState() {
            assertThrows(IllegalStateException.class, () -> testState.stashComponent(ship1));
        }

        @Test
        void grabStashedComponentThrowsExceptionWhenUsingInvalidState() {
            assertThrows(IllegalStateException.class, () -> testState.grabStashedComponent(ship1, 1));
        }

        @Test
        void placeComponentThrowsExceptionWhenUsingInvalidState() {
            assertThrows(IllegalStateException.class, () -> testState.placeComponent(ship1, new Point(3, 3), 0));
        }

        @Test
        void flipHourglassThrowsExceptionWhenUsingInvalidState() {
            assertThrows(IllegalStateException.class, () -> testState.flipHourglass(ship1));
        }

        @Test
        void placeShipOnFlightBoardThrowsExceptionWhenUsingInvalidState() {
            assertThrows(IllegalStateException.class, () -> testState.placeShipOnFlightBoard(ship1, 5));
        }

        @Test
        void testPlaceShipOnFlightBoardThrowsExceptionWhenUsingInvalidState() {
            assertThrows(IllegalStateException.class, () -> testState.placeShipOnFlightBoard(ship1));
        }

        @Test
        void acquireForecastThrowsExceptionWhenUsingInvalidState() {
            assertThrows(IllegalStateException.class, () -> testState.acquireForecast(ship1, 0));
        }

        @Test
        void releaseForecastThrowsExceptionWhenUsingInvalidState() {
            assertThrows(IllegalStateException.class, () -> testState.releaseForecast(ship1));
        }

        @Test
        void removeComponentThrowsExceptionWhenUsingInvalidState() {
            assertThrows(IllegalStateException.class, () -> testState.removeComponent(ship1, new Point(2, 2)));
        }

        @Test
        void initializeCabinThrowsExceptionWhenUsingInvalidState() {
            assertThrows(IllegalStateException.class, () -> testState.initializeCabin(ship1, CrewType.HUMAN));
        }
    }
}