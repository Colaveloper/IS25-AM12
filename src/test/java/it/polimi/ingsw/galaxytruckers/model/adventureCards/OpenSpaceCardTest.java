package it.polimi.ingsw.galaxytruckers.model.adventureCards;

import it.polimi.ingsw.galaxytruckers.model.*;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GameColor;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.Engine;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.SecondShipBoard;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.model.state.ActivateState;
import it.polimi.ingsw.galaxytruckers.model.state.DrawCardState;
import it.polimi.ingsw.galaxytruckers.model.state.GameState;
import it.polimi.ingsw.galaxytruckers.view.Direction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
        ShipBoard ship1 = new SecondShipBoard(GameColor.RED);
        ship1.addWeldedComponent(new Engine(), new Point(8,7), Direction.UP);
        ShipBoard ship2 = new SecondShipBoard(GameColor.BLUE);
        ship2.addWeldedComponent(new Engine(), new Point(8,7), Direction.UP);
        ships.addAll(List.of(ship1, ship2));
        FlightBoard flightBoardStub = new SecondFlightBoard(2);
        for (ShipBoard shipBoard : ships) {
            flightBoardStub.placeShipOnFlightBoard(shipBoard);
            shipBoard.initializeCabin(new Point(7,7), CrewType.HUMAN);
        }
        game = new Game(Level.SECOND);
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