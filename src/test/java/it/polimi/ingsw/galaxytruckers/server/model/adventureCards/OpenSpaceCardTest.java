package it.polimi.ingsw.galaxytruckers.server.model.adventureCards;

import it.polimi.ingsw.galaxytruckers.server.model.*;
import it.polimi.ingsw.galaxytruckers.shared.enums.GameColor;
import it.polimi.ingsw.galaxytruckers.shared.enums.Level;
import it.polimi.ingsw.galaxytruckers.server.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.server.model.shipBuilding.Engine;
import it.polimi.ingsw.galaxytruckers.server.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.server.model.state.ActivateState;
import it.polimi.ingsw.galaxytruckers.server.model.state.DrawCardState;
import it.polimi.ingsw.galaxytruckers.server.model.state.GameState;
import it.polimi.ingsw.galaxytruckers.shared.enums.Direction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class OpenSpaceCardTest {
    OpenSpaceCard openSpaceCard;
    List<ShipBoard> ships;
    Game game;
    GameEventListener listener;


    @BeforeEach
    void setUp() {
        ships = new ArrayList<>();
        // non-zero engine power required, otherwise the ships are required to give up
        ShipBoard ship1 = new SecondShipBoardForTesting(GameColor.RED);
        ship1.addWeldedComponent(new Engine(), new Point(8,7), Direction.UP);
        ShipBoard ship2 = new SecondShipBoardForTesting(GameColor.BLUE);
        ship2.addWeldedComponent(new Engine(), new Point(8,7), Direction.UP);
        ships.addAll(List.of(ship1, ship2));
        FlightBoard flightBoardStub = new SecondFlightBoardForTesting(2);
        for (ShipBoard shipBoard : ships) {
            flightBoardStub.placeShipOnFlightBoard(shipBoard);
            shipBoard.initializeCabin(new Point(7,7), CrewType.HUMAN);
        }
        game = new GameStub(Level.SECOND);
        game.setFlightBoard(flightBoardStub);
        openSpaceCard = new OpenSpaceCard(game, Level.SECOND, 1);
        openSpaceCard.initialize();
    }

    void setupShipWithNoEnginePower(){
        ships.getFirst().removeComponent(new Point(8,7));
    }

    @Test
    void getNextStateWhenThereArePlayersLeftReturnsActivate() {
        GameState testState = openSpaceCard.getNextState();
        assertInstanceOf(ActivateState.class, testState);
        testState = openSpaceCard.getNextState();
        assertInstanceOf(ActivateState.class, testState);
    }

    @Test
    void getNextStateWhenPlayersAreOverReturnsDrawCard() {
        openSpaceCard.getNextState();
        openSpaceCard.getNextState();
        GameState testState = openSpaceCard.getNextState();

        assertInstanceOf(DrawCardState.class, testState);
    }

    @Test
    void shipWithNoEnginePowerIsForcedToGiveUp() {
        setupShipWithNoEnginePower();
        openSpaceCard.getNextState();
        openSpaceCard.getNextState();
        game.getSurrenderPolicy().confirmSurrender(game.getFlightBoard());
        assertEquals(Set.of(ships.getFirst()), game.getSurrenderPolicy().getSurrenderedShips());
    }
}