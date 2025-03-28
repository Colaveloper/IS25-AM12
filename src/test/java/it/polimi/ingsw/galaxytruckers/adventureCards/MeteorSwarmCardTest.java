package it.polimi.ingsw.galaxytruckers.adventureCards;

import it.polimi.ingsw.galaxytruckers.FlightBoard;
import it.polimi.ingsw.galaxytruckers.adventureCards.utils.AdventureCard;
import it.polimi.ingsw.galaxytruckers.adventureCards.utils.BigMeteor;
import it.polimi.ingsw.galaxytruckers.adventureCards.utils.Projectile;
import it.polimi.ingsw.galaxytruckers.adventureCards.utils.SmallMeteor;
import it.polimi.ingsw.galaxytruckers.enumTypes.Colors;
import it.polimi.ingsw.galaxytruckers.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.shipBuilding.*;
import it.polimi.ingsw.galaxytruckers.shipBuilding.Component;
import it.polimi.ingsw.galaxytruckers.state.ActivateState;
import it.polimi.ingsw.galaxytruckers.state.ChooseShipPieceState;
import it.polimi.ingsw.galaxytruckers.state.DrawCardState;
import it.polimi.ingsw.galaxytruckers.state.GameState;
import javafx.scene.image.Image;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MeteorSwarmCardTest {
    MeteorSwarmCard meteorSwarmCard;
    FlightBoard flightBoard;
    List<Projectile> safeProjectiles;
    Projectile damagingProjectile;

    SecondShipBoard ship1;
    SecondShipBoard ship2;
    List<ShipBoard> ships;
    GameState testState;

    @BeforeEach
    void setUp() {
        ship1 = new SecondShipBoard(Colors.RED) {
            @Override
            public List<Set<Point>> getConnectedSets() {
                return List.of(Set.of(), Set.of()); // breaking
            }
        };
        ship2 = new SecondShipBoard(Colors.BLUE) {
            @Override
            public List<Set<Point>> getConnectedSets() {
                return List.of(Set.of()); // not breaking
            }
        };
        ships = new ArrayList<>(List.of(ship1, ship2));
        flightBoard = new FlightBoard(null) {
            @Override
            public Image getImage() {
                return null;
            }

            @Override
            public List<ShipBoard> getOrderedShips() {
                return new ArrayList<>(ships);
            }

            @Override
            public boolean placeShipOnFlightBoard(ShipBoard shipBoard, int startingPosition) {
                return true;
            }

            @Override
            protected int getLoopLength() {
                return 0;
            }
        };
        safeProjectiles = new ArrayList<>(List.of(
                new SmallMeteor(()->6,0),
                new SmallMeteor(()->9,0),
                new BigMeteor(()->8, 1),
                new BigMeteor(()->5, 2)
        ));
        damagingProjectile = new SmallMeteor(()->1,0) {
            @Override
            public boolean fireAt(ShipBoard shipBoard) {
                return true;
            }
        };
        meteorSwarmCard = new MeteorSwarmCard(null, Level.FIRST, flightBoard,  safeProjectiles);
    }

    @Test
    void nextStepReturnsActivateThenDrawCardIfNoBreaking() {
        for (Projectile projectile : safeProjectiles) {
            for (ShipBoard shipBoard : ships) {
                testState = meteorSwarmCard.nextStep();
                assertEquals(meteorSwarmCard.getCurrentShipBoard(), shipBoard);
                assertInstanceOf(ActivateState.class, testState);
            }
        }
        testState = meteorSwarmCard.nextStep();
        assertInstanceOf(DrawCardState.class, testState);
    }

    @Test
    void nextStepReturnsChoosePieceIfBreaking() {
        meteorSwarmCard = new MeteorSwarmCard(null, Level.FIRST, flightBoard,  List.of(damagingProjectile));
        meteorSwarmCard.nextStep(); // useless activation
        testState = meteorSwarmCard.nextStep(); // ship broke: let the player choose what piece to keep
        assertInstanceOf(ChooseShipPieceState.class, testState);
        meteorSwarmCard.nextStep(); // useless activation
        testState = meteorSwarmCard.nextStep(); // ship did not broke, all meteor finished
        assertInstanceOf(DrawCardState.class, testState);
    }
}