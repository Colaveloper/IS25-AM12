package it.polimi.ingsw.galaxytruckers.model;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.GameColor;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.SurrenderCause;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.awt.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class EnabledSurrenderPolicyTest {
    EnabledSurrenderPolicy enabledSurrenderPolicy;
    GameEventListener gameEventListener;
    SurrenderShipBoardStub ship;
    FlightBoardStub flightBoard;

    @BeforeEach
    void setUp() {
        ship = new SurrenderShipBoardStub();
        flightBoard = new FlightBoardStub();
        enabledSurrenderPolicy = new EnabledSurrenderPolicy();
        gameEventListener = Mockito.mock(GameEventListener.class);
    }

    @Test
    void setEventListener() {
        enabledSurrenderPolicy.setEventListener(gameEventListener);
        assertEquals(gameEventListener, enabledSurrenderPolicy.getListener());
    }

    @Test
    void isSurrenderEnabled() {
        assertTrue(enabledSurrenderPolicy.isSurrenderEnabled());
    }

    @Test
    void requestSurrender() {
        enabledSurrenderPolicy.requestSurrender(ship, null);
        assertEquals(Set.of(ship), enabledSurrenderPolicy.getRequests());
    }

    @Test
    void requestSurrenderIfAlreadySurrenderedThrowsException() {
        enabledSurrenderPolicy.requestSurrender(ship, null);
        assertThrows(IllegalStateException.class, () -> enabledSurrenderPolicy.requestSurrender(ship, null));
        enabledSurrenderPolicy.confirmSurrender(flightBoard);
        assertThrows(IllegalStateException.class, () -> enabledSurrenderPolicy.requestSurrender(ship, null));
    }

    @Test
    void requestSurrenderGeneratesEvent() {
        ship.setCrewSize(0);
        enabledSurrenderPolicy.setEventListener(gameEventListener);
        enabledSurrenderPolicy.requestSurrender(ship, SurrenderCause.REQUEST);
        Mockito.verify(gameEventListener, Mockito.times(1)).notifySurrenderRequestEvent(ship, SurrenderCause.REQUEST);
    }

    @Test
    void requestSurrenderDoesNotGenerateEventIfNoSurrenders() {
        ship.setCrewSize(1);
        enabledSurrenderPolicy.setEventListener(gameEventListener);
        enabledSurrenderPolicy.confirmSurrender(flightBoard);
        Mockito.verifyNoInteractions(gameEventListener);
    }

    @Test
    void confirmSurrenderRemovesPlayerWhoRequestedToSurrender() {
        ship.setCrewSize(1);
        flightBoard.addShip(ship);
        enabledSurrenderPolicy.requestSurrender(ship,null);
        checkSurrender();
    }

    @Test
    void confirmSurrenderRemovesPlayersWithNoCrewSize() {
        ship.setCrewSize(0);
        flightBoard.addShip(ship);
        checkSurrender();
    }

    @Test
    void confirmSurrenderRemovesPlayersWhoAreLapped() {
        ship.setCrewSize(1);
        flightBoard.addLappedShip(ship);
        checkSurrender();
    }

    @Test
    void confirmSurrenderGeneratesEvent() {
        enabledSurrenderPolicy.setEventListener(gameEventListener);
        enabledSurrenderPolicy.requestSurrender(ship, SurrenderCause.REQUEST);
        enabledSurrenderPolicy.confirmSurrender(flightBoard);
        Mockito.verify(gameEventListener,Mockito.times(1)).notifySurrenderEvent(List.of(ship));
    }

    void checkSurrender() {
        Set<ShipBoard> returnValue = enabledSurrenderPolicy.confirmSurrender(flightBoard);
        assertEquals(Set.of(ship), returnValue);
        assertEquals(Set.of(ship), enabledSurrenderPolicy.getSurrenderedShips());
        assertEquals(Set.of(ship), flightBoard.removedShips);
    }


}

class SurrenderShipBoardStub extends ShipBoard{
    int crewSize;

    public SurrenderShipBoardStub() {
        super(GameColor.RED);
    }

    @Override
    protected boolean containsPoint(Point point) {
        return true;
    }

    public void setCrewSize(int crewSize) {
        this.crewSize = crewSize;
    }

    @Override
    public int getCrewSize() {
        return crewSize;
    }
}

class FlightBoardStub extends FlightBoard {
    Map<ShipBoard, Integer> shipToPlace = new HashMap<>();
    Set<ShipBoard> removedShips = new HashSet<>();
    Set<ShipBoard> lappedShips = new HashSet<>();

    public FlightBoardStub() {}

    /**
     * @return the flightboard's length
     */
    @Override
    protected int getLoopLength() {
        return 0;
    }

    /**
     * @return a map containing for each ship still in play their position
     * on the flightBoard
     */
    @Override
    public Map<ShipBoard, Integer> getShipToPlace() {
        return shipToPlace;
    }

    /**
     * Removes a set of shipboards from the flightboard
     *
     * @param shipsToRemove the {@link Set} of ships to remove
     */
    @Override
    public void removeShips(Set<ShipBoard> shipsToRemove) {
        removedShips.addAll(shipsToRemove);
    }

    /**
     * @return a {@link Set} of shipboards that have been lapped on
     * the flightboard
     */
    @Override
    public Set<ShipBoard> getLappedShips() {
        return lappedShips;
    }

    public void addShip(ShipBoard shipBoard) {
        this.shipToPlace.put(shipBoard,0);
    }

    public void addLappedShip(ShipBoard shipBoard) {
        this.lappedShips.add(shipBoard);
    }
}